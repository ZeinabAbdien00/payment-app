package com.example.paymentapp.peresentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.paymentapp.R
import com.example.paymentapp.data.models.BaseModel
import com.example.paymentapp.databinding.FragmentHomeBinding
import com.example.paymentapp.peresentation.RecyclerView.HomeAdapter
import com.example.paymentapp.peresentation.addClient.AddClientDialog
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var adapter: HomeAdapter
    private lateinit var secondAdapter: HomeAdapter
    private lateinit var searchArrayList: ArrayList<BaseModel>
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
        setSearchAdapter()
        setObservers()
        setupOnClick()
    }

    private fun setSearchAdapter() {
        searchArrayList = ArrayList()
        secondAdapter = HomeAdapter(searchArrayList)
        secondAdapter.setOnItemClickListener(object : HomeAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                Toast.makeText(requireActivity(), " $position ", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setObservers() {
        viewModel.dataList.observe(viewLifecycleOwner) {
            try {
                if (viewModel.firstData.value == true) {
                    setupRecyclerView()
                    viewModel.setFirstData(false)
                }
            } catch (_: Exception) {
            }
        }

        viewModel.newItemInserted.observe(viewLifecycleOwner) {
            if (it) {
                val position = viewModel.dataList.value!!.size
                adapter.notifyItemInserted(position)
                binding.homeRecyclerView.smoothScrollToPosition(position)
                viewModel.setNewItemInserted(false)
            }
        }
    }

    private fun setupOnClick() {
        binding.addClient.setOnClickListener {
           //  findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddClientDialog())
            AddClientDialog(viewModel).show(
                childFragmentManager, null)
        }

        //search feature
        binding.search.doAfterTextChanged {
            val text = it.toString()
            if (text.isNotEmpty()) {
                searchArrayList.clear()
                searchArrayList.addAll(viewModel.dataList.value!!
                    .filter { it.name.contains(text) } as ArrayList<BaseModel>
                )
                binding.homeRecyclerView.adapter = secondAdapter
            } else {//if serach bar is empty restore all data
                lifecycleScope.launch {
                    binding.homeRecyclerView.adapter = adapter
                }
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