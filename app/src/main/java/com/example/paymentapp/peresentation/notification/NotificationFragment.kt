package com.example.paymentapp.peresentation.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.paymentapp.databinding.FragmentNotificationBinding
import com.example.paymentapp.peresentation.recyclerView.HomeAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationFragment : Fragment() {

    private lateinit var binding: FragmentNotificationBinding
    private lateinit var adapter: HomeAdapter
    private val viewModel: NotificationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentNotificationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
    }

    private fun setObservers() {
        viewModel.notificationsList.observe(viewLifecycleOwner) {
            try {
                if (viewModel.firstData.value == true) {
                    viewModel.setFirstData(false)
                    setupRecyclerView()
                }
            } catch (_: Exception) {
            }
        }

        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.getAllFromRoom().collect() {
                if (viewModel.firstData.value == false) {
                    lifecycleScope.launch {
                        viewModel.resetArrayList(it)
                        try {
                            binding.notificationsRecycler.adapter!!.notifyDataSetChanged()
                        } catch (_: Exception) {
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        val list = viewModel.getList()
        adapter = HomeAdapter(list)
        binding.notificationsRecycler.adapter = adapter
        adapter.setOnItemClickListener(object : HomeAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                findNavController().navigate(
                    NotificationFragmentDirections.actionNotificationsFragmentToDetailsFragment(
                        viewModel.notificationsList.value!![position]
                    )
                )
            }
        })
        binding.notificationsRecycler.layoutManager = LinearLayoutManager(requireActivity())
    }

    override fun onResume() {
        super.onResume()
        try {
            setupRecyclerView()
        } catch (_: Exception) {

        }
    }
}