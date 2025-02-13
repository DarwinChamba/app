package com.example.appcitas.fragmen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.appcitas.R
import com.example.appcitas.databinding.FragmentInicioBinding
import com.example.appcitas.databinding.FragmentPacienteBinding


class PacienteFragment : Fragment() {

private lateinit var binding: FragmentPacienteBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPacienteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.AGREGARCITAS.setOnClickListener {
            findNavController().navigate(R.id.action_pacienteFragment_to_citaFragment)

        }
    }

}