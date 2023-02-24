package com.example.paymentapp.peresentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.paymentapp.R
import com.example.paymentapp.data.models.BaseModel

import com.example.paymentapp.databinding.FragmentHomeBinding
import com.example.paymentapp.peresentation.RecyclerView.HomeAdapter

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var adapter: HomeAdapter
    private lateinit var list: ArrayList<BaseModel>
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

        setupRecyclerView()
        setupOnClick()
    }

    private fun setupOnClick() {
        binding.addClient.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddClientDialog())
        }
    }

    private fun setupRecyclerView() {
        list = ArrayList()
        list.add(createFakeData())
        list.add(createFakeData())
        list.add(createFakeData())
        adapter = HomeAdapter(list)
        binding.homeRecyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : HomeAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                Toast.makeText(requireActivity()," $position ",Toast.LENGTH_SHORT).show()
            }
        })
        binding.homeRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun createFakeData():BaseModel{
        return BaseModel(
            "Ibrahim",
            "01045687563",
            "2500",
            0.0f,
            0,
            "12/3/2023",
            "12/1/2023"
        )
    }
}