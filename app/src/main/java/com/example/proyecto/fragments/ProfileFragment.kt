package com.example.proyecto.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.proyecto.R
import com.example.proyecto.api.User

class ProfileFragment : Fragment() {
    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getSerializable("User") as? User
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user?.let {
            loadFragment(MapsFragment.newInstance(it)) // Pasar el usuario a MapsFragment
        }
    }

    private fun loadFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.mapsContainer, fragment) // Asegurar que coincida con el ID del XML
            .commit()
    }

    companion object {
        @JvmStatic
        fun newInstance(u: User) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("User", u)
                }
            }
    }
}