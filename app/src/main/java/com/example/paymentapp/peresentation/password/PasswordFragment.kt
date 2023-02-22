package com.example.paymentapp.peresentation.password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.paymentapp.databinding.FragmentPasswordBinding
import kotlinx.coroutines.launch

class PasswordFragment : Fragment() {

    private val viewModel : PasswordViewModel by viewModels()
    private lateinit var binding: FragmentPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPasswordBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClicks()
    }

    private fun setOnClicks() {
        //edit text . onTextChange
        val password = "0000"
        lifecycleScope.launch {
            if (password == viewModel.getPassword()){
                findNavController().navigate(PasswordFragmentDirections.actionPasswordFragmentToHomeFragment())
            }
        }
    }
}