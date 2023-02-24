package com.example.paymentapp.peresentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.paymentapp.R
import com.example.paymentapp.databinding.FragmentHomeBinding
import com.example.paymentapp.peresentation.RecyclerView.HomeAdapter
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var adapter: HomeAdapter
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        setupOnClick()
    }

    private fun setObservers() {
        viewModel.dataList.observe(viewLifecycleOwner) {
            try {
            if (viewModel.firstData.value == true) {
                setupRecyclerView()
                viewModel.setFirstData(false)
                return@observe
            }
            }catch (_:Exception){}
        }
    }

    private fun setupOnClick() {
        binding.addClient.setOnClickListener {
            // findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddClientDialog())
            lifecycleScope.launch {
                viewModel.insertToRoom(viewModel.createFakeData("hhh"))
                adapter.notifyItemInserted(viewModel.dataList.value!!.size-1)
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = HomeAdapter(viewModel.dataList.value!!)
        binding.homeRecyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : HomeAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                Toast.makeText(requireActivity(), " $position ", Toast.LENGTH_SHORT).show()
            }
        })
        binding.homeRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }


}