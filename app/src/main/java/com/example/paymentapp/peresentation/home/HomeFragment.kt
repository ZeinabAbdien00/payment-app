package com.example.paymentapp.peresentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        setupOnclicks()
    }

    private fun setupOnclicks() {
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
        binding.homeRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }

    fun createFakeData():BaseModel{
        return BaseModel(
            "asdfa",
            "sdsgsdg",
            "sdgsdg",
            0.0f,
            0,
            "daf",
            "adfa"
        )
    }
}