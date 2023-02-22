package com.example.paymentapp.peresentation.addClient

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.paymentapp.R
import com.example.paymentapp.databinding.FragmentAddClientDialogBinding


class AddClientDialog : DialogFragment() {

    lateinit var binding: FragmentAddClientDialogBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        requireActivity().setFinishOnTouchOutside(false)
        binding= FragmentAddClientDialogBinding.inflate(layoutInflater)
        return binding.root

        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val metrics = resources.displayMetrics
        val width = metrics.widthPixels
        val height = metrics.heightPixels


        //this.dialog!!.window!!.setLayout((width) , (4 * height) / 6)
        this.dialog!!.window!!.setLayout(((9*width)/10) , (9*height)/10 )

        setOnClickListeners()

    }
    private fun setOnClickListeners(){
        binding.cancelBtn.setOnClickListener{
            findNavController().navigateUp()
        }


    }


}