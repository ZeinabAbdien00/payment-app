package com.example.paymentapp.peresentation.password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.paymentapp.data.dataStore.DataStoreImpl
import com.example.paymentapp.databinding.FragmentResetPasswordDialogBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ResetPasswordDialog : DialogFragment() {

    @Inject
    lateinit var dataStoreImpl: DataStoreImpl

    private lateinit var binding: FragmentResetPasswordDialogBinding
    private val args: ResetPasswordDialogArgs by navArgs()
    private lateinit var oldPassword: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentResetPasswordDialogBinding.inflate(layoutInflater)
        oldPassword = args.oldPassword
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDimentions()
        setOnClicks()
    }

    private fun setOnClicks() {
        binding.addBtn.setOnClickListener {
            if (binding.oldPassword.getText()
                    .toString() == oldPassword && binding.newPassword.getText().toString() != ""
            ) {
                lifecycleScope.launch(Dispatchers.Main) {
                    dataStoreImpl.setPassword(binding.newPassword.getText().toString())
                    Toast.makeText(requireActivity(),
                        "تم تغيير كلمة المرور بنجاح",
                        Toast.LENGTH_SHORT)
                        .show()
                    findNavController().navigateUp()
                }
            } else {
                if (binding.oldPassword.getText().toString() != oldPassword)
                    Toast.makeText(requireContext(),
                        "كلمة المرور القديمة خاطئة",
                        Toast.LENGTH_SHORT)
                        .show()
                else
                    Toast.makeText(requireContext(), "ادخل كلمة مرور جديدة", Toast.LENGTH_SHORT)
                        .show()

            }
        }

        binding.cancelBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setDimentions() {
        val metrics = resources.displayMetrics
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        this.dialog!!.window!!.setLayout(((9 * width) / 10), (7 * height) / 10)
    }

}