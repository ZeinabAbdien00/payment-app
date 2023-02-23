package com.example.paymentapp.peresentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.paymentapp.R
import com.example.paymentapp.data.models.BaseModel
import com.example.paymentapp.databinding.FragmentHomeBinding
import com.example.paymentapp.peresentation.RecyclerView.HomeAdapter

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var adapter: HomeAdapter
    private lateinit var list:ArrayList<BaseModel>
    private lateinit var binding: FragmentHomeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        list = ArrayList()
        adapter = HomeAdapter(list)
        binding.homeRecyclerView.adapter = adapter
        binding.homeRecyclerView.layoutManager = LinearLayoutManager(requireActivity())


        binding.addClient.setOnClickListener{
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddClientDialog())
        }
    }
}