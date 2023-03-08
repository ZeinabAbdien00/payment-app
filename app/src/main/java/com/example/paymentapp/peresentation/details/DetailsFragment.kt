package com.example.paymentapp.peresentation.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.paymentapp.databinding.FragmentDetailsBinding
import com.example.paymentapp.peresentation.recyclerView.HistoryAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.roundToInt

class DetailsFragment : Fragment() {

    private val args: DetailsFragmentArgs by navArgs()
    private lateinit var binding: FragmentDetailsBinding
    private lateinit var adapter: HistoryAdapter
    private lateinit var myList: ArrayList<String>
    private val viewModel: DetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDetailsBinding.inflate(layoutInflater)
        binding.saveBtn.isEnabled = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitials()
        setViews()
        setOnClicks()
        setOnChangeLogic()
        setupHistoryRV()
    }

    private fun setData() {
        binding.apply {
            //قيمة الاقساط المدفوعه
            allCostPaidEditText.setText((viewModel.payiedInstallmentsValue).roundToInt().toString())
            //عدد الاقساط المدفوعه
            paidInstallmentEditText.setText(viewModel.payiedInstallmentsNumber.toString())
            //قيمة الاقساط المتبقيه
            costRemainingInstallmentEditText.setText(
                viewModel.comingInstallmentsVlaue.roundToInt().toString()
            )
            //عدد اللاقساط المتبقيه
            remainingInstallmentEditText.setText((viewModel.comingInstallmentsNumber).toString())  //قيمة الاقساط المدفوعه
        }
    }

    private fun setInitials() {
        viewModel.setMyModel(args.model)
        viewModel.historyArray = viewModel.model.historyList
        viewModel.name = viewModel.model.name
        viewModel.phone = viewModel.model.phoneNumber
        viewModel.income = viewModel.model.income
        viewModel.priceBefore = viewModel.model.priceWithoutAddition.toDouble()
        viewModel.benefits = viewModel.model.addintionPercentage
        viewModel.benefitsValue = viewModel.model.additionMoney.toDouble()
        viewModel.priceAfter = viewModel.model.priceAfterAddition.toDouble()
        viewModel.totalInstallmentsNumber = viewModel.model.numberOfTotalInstallments
        //عدد الاقساط المدفوعه
        viewModel.payiedInstallmentsNumber = viewModel.model.payNumber
        //قيمه القساط المدفوعه
        viewModel.payiedInstallmentsValue = viewModel.model.payValue
        //عدد الاقساط المتبقيه
        viewModel.comingInstallmentsNumber =
            viewModel.model.numberOfTotalInstallments - viewModel.model.payNumber
        //قيمة الاقساط المتبقيه
        viewModel.comingInstallmentsVlaue =
            viewModel.model.priceAfterAddition.toDouble() - viewModel.model.payValue
        viewModel.dayOfPaying = viewModel.model.monthlyDayOfPaying
        viewModel.startDate = viewModel.model.startDate
        viewModel.carModel = viewModel.model.nameOfBoughtItems
        viewModel.monthlyPayValue = viewModel.model.monthlyPay.toDouble()
        viewModel.myNote = viewModel.model.note
        viewModel.laterMonth = viewModel.model.numberOfLateMoneyMonths
    }

    private fun setOnChangeLogic() {
        binding.apply {

            laterMonthEditText.addTextChangedListener {
                if (it.toString().isNotEmpty()) {
                    saveBtn.isEnabled = true
                    viewModel.laterMonth = it.toString().toInt()
                } else {
                    saveBtn.isEnabled = false
                    //viewModel.laterMonth = viewModel.model.numberOfLateMoneyMonths
                }
            }

            clientName.addTextChangedListener {
                if (it.toString().isNotEmpty()) {
                    saveBtn.isEnabled = true
                    viewModel.name = it.toString()
                } else {
                    saveBtn.isEnabled = false
                    viewModel.name = viewModel.model.name
                }
            }

            numberEditText.addTextChangedListener {
                if (it.toString().isNotEmpty()) {
                    saveBtn.isEnabled = true
                    viewModel.phone = it.toString()
                } else {
                    saveBtn.isEnabled = false
                    viewModel.phone = viewModel.model.phoneNumber
                }
            }

            modelOfCarEditText.addTextChangedListener {
                if (it.toString().isNotEmpty()) {
                    saveBtn.isEnabled = true
                    viewModel.carModel = it.toString()
                } else {
                    saveBtn.isEnabled = false
                    viewModel.carModel = viewModel.model.nameOfBoughtItems
                }
            }

            theNote.addTextChangedListener {
                if (it.toString().isNotEmpty()) {
                    saveBtn.isEnabled = true
                    viewModel.myNote = it.toString()
                } else {
                    saveBtn.isEnabled = false
                    viewModel.myNote = viewModel.model.note
                }
            }

            payDayEditText.addTextChangedListener {
                if (it.toString().isNotEmpty()) {
                    saveBtn.isEnabled = true
                    viewModel.dayOfPaying = it.toString()
                } else {
                    saveBtn.isEnabled = false
                    viewModel.dayOfPaying = viewModel.model.monthlyDayOfPaying
                }
            }
        }
    }

    private fun setOnClicks() {

        binding.saveBtn.setOnClickListener {
            if (!checkIfDataChanged()) {
                saveNewData()
                binding.saveBtn.isEnabled = false
            }
            findNavController().navigateUp()
        }

        binding.btnPay.setOnClickListener {

            if (viewModel.model.historyList.size == viewModel.model.numberOfTotalInstallments) {
                Toast.makeText(requireContext(), "تم سداد جميع الاقساط", Toast.LENGTH_SHORT).show()
            } else {
                it.isClickable = false
                lifecycleScope.launch(Dispatchers.Main) {
                    try {
                        val calendar = Calendar.getInstance()
                        val day = calendar.get(Calendar.DAY_OF_MONTH)
                        val month = calendar.get(Calendar.MONTH)
                        val year = calendar.get(Calendar.YEAR)
                        val currentDate = "${year}/${month + 1}/${day}"
                        viewModel.addDateToItem(currentDate)
                        adapter.notifyDataSetChanged()
                        setData()
                    } catch (_: Exception) {
                    }
                    it.isClickable = true
                    binding.saveBtn.isEnabled = true
                }
            }
        }

        binding.btnDidnotPay.setOnClickListener {

            if (viewModel.model.historyList.size == 0) {
                Toast.makeText(requireContext(), "لا يوجد اقساط مدفوعه", Toast.LENGTH_SHORT).show()
            } else {
                lifecycleScope.launch(Dispatchers.Main) {
                    it.isClickable = false

                    try {
                        viewModel.removeLastDateFromItem()
                        adapter.notifyDataSetChanged()
                        setData()
                    } catch (_: Exception) {
                    }

                    it.isClickable = true
                    binding.saveBtn.isEnabled = true
                }
            }
        }

        binding.backArrow.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupHistoryRV() {
        myList = viewModel.historyArray
        adapter = HistoryAdapter(myList)
        binding.rvHistoryView.adapter = adapter
        binding.rvHistoryView.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun setViews() {
        setData()
        binding.apply {
            //اسم العميل
            clientName.setText(viewModel.model.name)
            //رقم العميل
            numberEditText.setText(viewModel.model.phoneNumber)
            //المقدم
            incomeEditText.setText(viewModel.model.income.toString().toDouble().roundToInt()
                .toString())
            //السعر قبل الزياده
            priceBeforeTaxEditText.setText(viewModel.model.priceWithoutAddition.toDouble()
                .roundToInt().toString())
            //نسبة الفائدة
            ratioEditText.setText("${viewModel.model.addintionPercentage} %")
            //قيمة الفائدة
            priceTaxEditText.setText(viewModel.model.additionMoney.toDouble().roundToInt()
                .toString())
            //السعر بعد الفائدة
            priceAfterTaxEditText.setText(viewModel.model.priceAfterAddition.toDouble().roundToInt()
                .toString())
            // الاقساط الكلية عدد
            allCostEditText.setText(viewModel.model.numberOfTotalInstallments.toString())
            //يوم السداد
            payDayEditText.setText(viewModel.model.monthlyDayOfPaying)
            //تاريخ البداية
            startDateEditText.setText(viewModel.model.startDate)
            // موديل السيارة
            modelOfCarEditText.setText(viewModel.model.nameOfBoughtItems)
            //note
            theNote.setText(viewModel.model.note)
            //القسط
            montthlyPayEditText.setText(viewModel.model.monthlyPay)
            laterMonthEditText.setText(viewModel.model.numberOfLateMoneyMonths.toString())
        }
    }

    private fun saveNewData() {
        GlobalScope.launch {
            viewModel.saveData()
        }
    }

    private fun checkIfDataChanged(): Boolean =
        viewModel.isNewData()
}