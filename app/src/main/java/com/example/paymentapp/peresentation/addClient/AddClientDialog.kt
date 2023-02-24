package com.example.paymentapp.peresentation.addClient

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.viewModelScope
import com.example.paymentapp.databinding.FragmentAddClientDialogBinding
import com.example.paymentapp.peresentation.home.HomeViewModel
import kotlinx.coroutines.launch


class AddClientDialog(private val viewModel: HomeViewModel) : DialogFragment() {

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

        this.dialog!!.window!!.setLayout(((9*width)/10) , (9*height)/10 )

        setOnClickListeners()

    }
    private fun setOnClickListeners(){
        binding.cancelBtn.setOnClickListener{
          this.dismiss()
        }

        binding.addBtn.setOnClickListener {
            viewModel.viewModelScope.launch {
                viewModel.setNewItemInserted(true)
                viewModel.insertToRoom(viewModel.createFakeData("namee"))
                this@AddClientDialog.dismiss()
            }
        }
    }


}