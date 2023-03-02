package com.example.paymentapp.peresentation.home

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.paymentapp.R
import com.example.paymentapp.data.models.BaseModel
import com.example.paymentapp.databinding.FragmentHomeBinding
import com.example.paymentapp.globalUse.SwipeToDeleteCallback
import com.example.paymentapp.peresentation.addClient.AddClientDialog
import com.example.paymentapp.peresentation.recyclerView.HomeAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


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
        setupSwipeToDelete()
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
                childFragmentManager, null
            )
        }

        //search feature
        binding.search.doAfterTextChanged {
            val text = it.toString()
            val calendar = Calendar.getInstance()
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            if (text.isNotEmpty()) {
                searchArrayList.clear()
                viewModel.setIsSearch(true)
                searchArrayList.addAll(viewModel.dataList.value!!
                    .filter { it.name.contains(text) }.sortedByDescending {
                        day - it.monthlyDayOfPaying.toInt()
                    }
                )
                // searchArrayList.sortByDescending { it.numberOfLateMoneyMonths }

                binding.homeRecyclerView.adapter = secondAdapter
            } else {//if search bar is empty restore all data
                viewModel.setIsSearch(false)
                binding.homeRecyclerView.adapter = adapter

            }
        }

        //sorting feature
        binding.sort.setOnClickListener {
            sortItems()
            binding.homeRecyclerView.adapter!!.notifyItemRangeChanged(
                0,
                if (viewModel.isSearch.value == true)
                    searchArrayList.size
                else viewModel.dataList.value!!.size
            )
        }

    }

    private fun sortItems() {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        if (viewModel.isSearch.value == false) {
            if (viewModel.normalMode.value == true) {
                viewModel.setIsNormalMode(false)
                viewModel.dataList.value!!.sortByDescending { day - it.monthlyDayOfPaying.toInt() }
                //  viewModel.dataList.value!!.sortByDescending { it.numberOfLateMoneyMonths}
            } else {
                viewModel.setIsNormalMode(true)
                viewModel.dataList.value!!.sortBy { day - it.monthlyDayOfPaying.toInt() }
                //  viewModel.dataList.value!!.sortBy { it.numberOfLateMoneyMonths}
            }
        } else {
            if (viewModel.normalMode.value == true) {
                viewModel.setIsNormalMode(false)
                searchArrayList.sortByDescending { day - it.monthlyDayOfPaying.toInt() }
                // searchArrayList.sortByDescending { it.numberOfLateMoneyMonths}
            } else {
                viewModel.setIsNormalMode(true)
                searchArrayList.sortBy { day - it.monthlyDayOfPaying.toInt() }
                // searchArrayList.sortBy { it.numberOfLateMoneyMonths}
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = HomeAdapter(viewModel.dataList.value!!)
        binding.homeRecyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : HomeAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                if (viewModel.isSearch.value == true) {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToDetailsFragment(
                            searchArrayList[position]
                        )
                    )
                } else {
                    findNavController().navigate(
                        HomeFragmentDirections
                            .actionHomeFragmentToDetailsFragment(viewModel.dataList.value!![position])
                    )
                }
            }
        })
        binding.homeRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun setupSwipeToDelete() {
        val swipeToDeleteCallback: SwipeToDeleteCallback =
            object : SwipeToDeleteCallback(requireContext()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
                    removeAfterSwiped(viewHolder)
                }
            }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.homeRecyclerView)
    }

    private fun removeAfterSwiped(viewHolder: RecyclerView.ViewHolder) {

        val dialogName: AlertDialog.Builder =
            AlertDialog.Builder(requireContext(), R.style.AlertDialogCustom)
        dialogName.setTitle("هل تريد حذف هذا العنصر ؟")

        dialogName.setPositiveButton("نعم",
            DialogInterface.OnClickListener { dialogInterface, i ->
                lifecycleScope.launch {
                    val position = viewHolder.adapterPosition
                    if (viewModel.isSearch.value == false) {
                        viewModel.removeItemOf(position)
                    } else {
                        removePositionFromSearch(position)
                    }
                    withContext(Dispatchers.Main) {
                        binding.homeRecyclerView.adapter!!.notifyItemRemoved(position)
                        val snackbar = Snackbar
                            .make(
                                binding.homeParent,
                                "تم الحذف",
                                Snackbar.LENGTH_LONG
                            )
                        snackbar.show()
                    }
                }
                dialogInterface.cancel()
            })

        dialogName.setNegativeButton("لا",
            DialogInterface.OnClickListener { dialogInterface, i ->
                val position = viewHolder.adapterPosition
                adapter.notifyItemChanged(position)
                dialogInterface.cancel()
            })
        dialogName.show()

    }

    private fun removePositionFromSearch(position: Int) {
        lifecycleScope.launch {
            val item = searchArrayList[position]
            searchArrayList.remove(item)
            viewModel.removeItemFromDataList(item)
            viewModel.deleteFromRoom(item)
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            setupRecyclerView()
        } catch (_: Exception) {

        }
    }

}