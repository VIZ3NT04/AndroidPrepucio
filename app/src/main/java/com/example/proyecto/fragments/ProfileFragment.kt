package com.example.proyecto.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.proyecto.R
import com.example.proyecto.api.User

class ProfileFragment : Fragment() {
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getSerializable("User") as User
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        return inflater.inflate(R.layout.fragment_favorites, container, false)
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