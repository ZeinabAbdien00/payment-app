package com.example.paymentapp.peresentation.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.paymentapp.databinding.FragmentNotificationBinding
import com.example.paymentapp.peresentation.RecyclerView.HomeAdapter
import com.example.paymentapp.peresentation.home.HomeFragmentDirections

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
        viewModel.notificationsList.observe(viewLifecycleOwner){
            try {
                if (viewModel.firstData.value == true) {
                    setupRecyclerView()
                    viewModel.setFirstData(false)
                }
            } catch (_: Exception) {
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = HomeAdapter(viewModel.notificationsList.value!!)
        binding.notificationsRecycler.adapter = adapter
        adapter.setOnItemClickListener(object : HomeAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToDetailsFragment(
                            viewModel.notificationsList.value!![position]
                        )
                    )
            }
        })
        binding.notificationsRecycler.layoutManager = LinearLayoutManager(requireActivity())
    }

}