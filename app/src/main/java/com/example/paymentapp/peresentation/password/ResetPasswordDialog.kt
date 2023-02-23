package com.example.paymentapp.peresentation.password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.paymentapp.databinding.FragmentResetPasswordDialogBinding

class ResetPasswordDialog : DialogFragment() {

    private lateinit var binding: FragmentResetPasswordDialogBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResetPasswordDialogBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDimentions()
    }

    private fun setDimentions() {
        val metrics = resources.displayMetrics
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        this.dialog!!.window!!.setLayout(((9*width)/10) , (7*height)/10 )
    }

}