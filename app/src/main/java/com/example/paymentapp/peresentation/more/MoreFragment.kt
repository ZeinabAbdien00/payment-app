package com.example.paymentapp.peresentation.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.paymentapp.R
import com.example.paymentapp.databinding.FragmentMoreBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MoreFragment : Fragment() {

    private lateinit var binding: FragmentMoreBinding
    private val viewModel: MoreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMoreBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.passwordLinear.setOnClickListener {
            binding.usePasswordSwitch.isChecked = !binding.usePasswordSwitch.isChecked
            lifecycleScope.launch {
                viewModel.dataStore.setUsePassword(binding.usePasswordSwitch.isChecked)
            }
        }

        binding.resetPasswordLinear.setOnClickListener {
            //todo:Mohamed
            // add dialog fragment
         }

        binding.notificationLinear.setOnClickListener {

            //todo:Suzan
            //always make sure useNotifications is true before sending any notifications

            binding.notificationsSwitch.isChecked = !binding.notificationsSwitch.isChecked
            lifecycleScope.launch {
                viewModel.dataStore.setUseNotifications(binding.notificationsSwitch.isChecked)
            }
        }
    }

    private fun setViews() {
        lifecycleScope.launch(Dispatchers.Main) {
            binding.apply {
                usePasswordSwitch.isChecked = viewModel.isUsePassword()
                notificationsSwitch.isChecked = viewModel.isUseNotifications()
            }
        }
    }


    private fun showDialog(
        messege: String,
        positiveTxt: String,
        negativeTxt: String,
        logic: () -> Unit,
    ) {
        MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogCustom)
            .setMessage(messege)
            .setCancelable(false)
            .setPositiveButton(positiveTxt) { dialog, _ ->
                logic()
            }
            .setNegativeButton(negativeTxt) { dialog, _ ->
                dialog.dismiss()
            }
            .show()

    }

}