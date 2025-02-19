package com.example.proyecto.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.proyecto.R
import com.example.proyecto.api.Category
import com.example.proyecto.api.Product
import com.example.proyecto.api.RetrofitInstance
import com.example.proyecto.api.User
import com.example.proyecto.databinding.FragmentSellBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class SellFragment : Fragment() {

    private lateinit var binding: FragmentSellBinding
    private lateinit var user: User
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getSerializable("User") as? User ?: run {
                Log.e("SellFragment", "Error: No se pasó el usuario correctamente")
                return
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSellBinding.inflate(inflater, container, false)

        val spinner: Spinner = binding.spCategory

        // Obtener las categorías y configurarlas en el Spinner
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val categories = withContext(Dispatchers.IO) {
                    RetrofitInstance.api.listCategories()
                }

                if (categories.isNotEmpty()) {
                    val adapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        categories
                    )
                    spinner.adapter = adapter
                } else {
                    Log.e("SellFragment", "Error: No se recibieron categorías.")
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("SellFragment", "Error HTTP ${e.code()}: $errorBody")
            }
        }
        binding.btnAddPhotos.setOnClickListener {
            seleccionarImagen()
        }


        // Evento al hacer clic en el botón de venta
        binding.btnSell.setOnClickListener {
            val txtDesc = binding.txtDescProd.text.toString()
            val txtPrice = binding.txtPriceProd.text.toString()
            val txtName = binding.txtNameProd.text.toString()
            val txtAnt = binding.txtAntiquity.text.toString()
            val txtCategory = binding.spCategory.selectedItem as Category

            var correct = true

            // Validaciones de campos vacíos
            if (txtDesc.isEmpty()) {
                binding.txtDescProd.error = "La descripcion no puede estar vacia"
                correct = false
            }

            if (txtPrice.isEmpty() || txtPrice.toFloatOrNull() == null) {
                binding.txtPriceProd.error = "El precio debe ser un número válido"
                correct = false
            }

            if (txtName.isEmpty()) {
                binding.txtNameProd.error = "El nombre no puede estar vacio"
                correct = false
            }

            if (txtCategory == null) {
                Toast.makeText(context, "Por favor selecciona una categoría.", Toast.LENGTH_SHORT).show()
                correct = false
            }

            // Aquí activas la selección de la imagen


            // Si todos los campos son correctos, se procesa el producto
            if (correct) {
                val fotoUri: Uri? = selectedImageUri

                viewLifecycleOwner.lifecycleScope.launch {
                    try {
                        val newProduct = Product(
                            id = -1,
                            name = txtName,
                            description = txtDesc,
                            categoria = txtCategory,
                            price = txtPrice.toFloat(),
                            usuario = user,
                            antiquity = txtAnt,
                        )

                        val product = RetrofitInstance.api.addProduct(newProduct)

                        // Si se seleccionó una imagen, la subimos
                        fotoUri?.let {
                            subirImagen(it, product.id) // Subimos la imagen después de crear el producto
                        }

                        Toast.makeText(
                            context,
                            "Se ha añadido el producto: ${product.name}",
                            Toast.LENGTH_SHORT
                        ).show()

                    } catch (e: HttpException) {
                        val errorBody = e.response()?.errorBody()?.string()
                        Log.e("SellFragment", "Error HTTP ${e.code()}: $errorBody")
                    }
                }
            }
        }

        return binding.root
    }

    // Función para abrir la galería y seleccionar una imagen
    private fun seleccionarImagen() {
        Log.e("ODOO", "Seleccionar imagen llamada")
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"  // Para solo imágenes
        imagePickerLauncher.launch(intent)
    }

    // Registro de la actividad para capturar el resultado de la selección de imagen
    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            selectedImageUri = data?.data // Guardamos la URI de la imagen seleccionada
        }
    }

    private fun subirImagen(imageUri: Uri, articuloId: Int) {
        try {
            // Redimensionar la imagen antes de subirla
            val resizedUri = redimensionarImagen(imageUri) ?: run {
                Log.e("SellFragment", "Error: No se pudo redimensionar la imagen.")
                return
            }

            // Obtener el contentResolver con el contexto adecuado
            val inputStream = requireContext().contentResolver.openInputStream(resizedUri)
            if (inputStream == null) {
                Log.e("SellFragment", "Error: No se pudo abrir el archivo de imagen redimensionada.")
                return
            }

            // Crear un archivo temporal en el cacheDir del contexto
            val tempFile = File(requireContext().cacheDir, "upload_resized.jpg")
            tempFile.outputStream().use { output ->
                inputStream.copyTo(output) // Copiar los datos al archivo temporal
            }

            // Verificar que el archivo se haya creado correctamente
            if (!tempFile.exists() || tempFile.length() == 0L) {
                Log.e("SellFragment", "Error: El archivo temporal está vacío o no se pudo crear.")
                return
            }

            Log.d("SellFragment", "Archivo temporal creado en: ${tempFile.absolutePath}")

            // Crear el requestFile con el tipo de medio adecuado
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), tempFile)
            val body = MultipartBody.Part.createFormData("file", tempFile.name, requestFile)

            // Realizar la solicitud de subida de imagen
            RetrofitInstance.api.subirImagen(articuloId, body).enqueue(object : Callback<Map<String, String>> {
                override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                    if (response.isSuccessful) {
                        val imageUrl = response.body()?.get("imageUrl")
                        Log.d("SellFragment", "Imagen subida exitosamente: $imageUrl")
                    } else {
                        Log.e("SellFragment", "Error en la respuesta del servidor: ${response.code()} ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                    Log.e("SellFragment", "Error al subir la imagen: ${t.message}")
                }
            })
        } catch (e: Exception) {
            Log.e("SellFragment", "Error inesperado: ${e.message}")
        }
    }

    private fun redimensionarImagen(uri: Uri): Uri? {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)

        val maxWidth = 1024  // Ancho máximo
        val maxHeight = 1024  // Alto máximo

        val width = bitmap.width
        val height = bitmap.height

        val newWidth = if (width > height) maxWidth else (maxHeight * width) / height
        val newHeight = if (height > width) maxHeight else (maxWidth * height) / width

        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false)

        // Guardar la imagen redimensionada en un archivo temporal
        val tempFile = File(requireContext().cacheDir, "resized_image.jpg")
        val outputStream = FileOutputStream(tempFile)
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream) // Comprimir con calidad 80%

        return Uri.fromFile(tempFile)
    }



    companion object {
        @JvmStatic
        fun newInstance(u: User) =
            SellFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("User", u)
                }
            }
    }
}
