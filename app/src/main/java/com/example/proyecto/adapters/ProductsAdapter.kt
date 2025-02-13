package com.example.proyecto.adapters
/*
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.R
import com.example.proyecto.databinding.ItemListMovementsBinding
import com.example.proyecto.pojo.Movimiento
import java.text.SimpleDateFormat

class MovementsAdapter (private val movimientos: ArrayList<Movimiento>?, private val listener: OnClickListener):
    RecyclerView.Adapter<MovementsAdapter.ViewHolder>() {

    private lateinit var context: Context

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemListMovementsBinding.bind(view)
        fun setListener(movimiento: Movimiento){
            binding.root.setOnClickListener {
                listener.onClick(movimiento)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_list_movements, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movimientos!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movimiento = movimientos?.get(position) as Movimiento
        val formateador = SimpleDateFormat("dd-MM-yyyy")

        with(holder) {
            setListener(movimiento)
            binding.txtMov.text = movimiento.getDescripcion()
            binding.txtInfoMov.text = formateador.format(movimiento.getFechaOperacion()) + " Importe: " + movimiento.getImporte().toString()
            binding.txtInfoMov.setTextColor(ContextCompat.getColor(context, R.color.red))
        }
    }
}*/