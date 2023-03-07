package com.example.paymentapp.peresentation.details

import android.os.Bundle
import android.util.Half.toFloat
import android.util.Log
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
import com.example.paymentapp.data.models.BaseModel
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
    private lateinit var model: BaseModel
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
        setViews()
        setupHistoryRV()
        setInitials()
        setOnClicks()
        setOnChangeLogic()
    }


    private fun splitRatio(): Double {
        binding.apply {
            return if (ratioEditText.text.contains(" ")) {
                val s = ratioEditText.text.toString().split(" ")
                (s[0].toDouble() / 100)

            } else if (ratioEditText.text.contains("%")) {
                val s = ratioEditText.text.toString().split("%")
                (s[0].toDouble() / 100)
            } else {
                ratioEditText.text.toString().toDouble() / 100
            }
        }
    }

    private fun ratioChanged(percentage: Double) {
        binding.apply {
            priceAfterTaxEditText.setText(((viewModel.priceBefore.toDouble() - incomeEditText.text.toString()
                .toDouble()) * (1 + percentage)).toString())
            priceTaxEditText.setText(((viewModel.priceBefore.toDouble() - incomeEditText.text.toString()
                .toDouble()) * percentage).toString())
            monthlyPay()
        }
    }

    private fun monthlyPay() {
        binding.apply {
            montthlyPayEditText.text =
                ((priceAfterTaxEditText.text.toString().toDouble() / allCostEditText.text.toString()
                    .toDouble()).roundToInt().toString())
        }
    }

    private fun setInitials() {
        viewModel.name = model.name
        viewModel.phone = model.phoneNumber
        viewModel.income = model.income
        viewModel.priceBefore = model.priceWithoutAddition
        viewModel.benefits = model.addintionPercentage.toString()
        viewModel.benefitsValue = model.additionMoney
        viewModel.priceAfter = model.priceAfterAddition
        viewModel.totalInstallmentsNumber = model.numberOfTotalInstallments.toString()
        viewModel.payiedInstallmentsNumber = model.numberOfPaidInstallments.toString()
        viewModel.payiedInstallmentsValue = model.valueOfPayInstallments
        viewModel.comingInstallmentsNumber = model.numberOfComingInstallments.toString()
        viewModel.comingInstallmentsVlaue = model.valueOfComingInstallments
        viewModel.dayOfPaying = model.monthlyDayOfPaying
        viewModel.startDate = model.startDate
        viewModel.carModel = model.nameOfBoughtItems
        viewModel.monthlyPayValue = model.monthlyPay
        viewModel.myNote = model.note
    }

    private fun setOnChangeLogic() {
        binding.apply {
            clientName.addTextChangedListener {
                if (it.toString().isNotEmpty()) {
                    saveBtn.isEnabled = true
                    viewModel.name = it.toString()
                } else {
                    saveBtn.isEnabled = false
                    viewModel.name = model.name
                }
            }

            numberEditText.addTextChangedListener {
                if (it.toString().isNotEmpty()) {
                    saveBtn.isEnabled = true
                    viewModel.phone = it.toString()
                } else {
                    saveBtn.isEnabled = false
                    viewModel.phone = model.phoneNumber
                }
            }

            incomeEditText.addTextChangedListener {
                if (it.toString().isNotEmpty()) {
                    saveBtn.isEnabled = true
                    ratioChanged(splitRatio())
                } else {
                    saveBtn.isEnabled = false
                    apply {
                        priceAfterTaxEditText.setText(((viewModel.priceBefore.toDouble()) * (1 + splitRatio())).toString())
                        priceTaxEditText.setText(((viewModel.priceBefore.toDouble()) * splitRatio()).toString())
                        monthlyPay()
                        incomeEditText.setText("0")
                    }
                }
            }

            ratioEditText.addTextChangedListener {
                if (it.toString().isNotEmpty()) {
                    saveBtn.isEnabled = true
                    viewModel.benefits = it.toString()
                    ratioChanged(splitRatio())
                } else {
                    saveBtn.isEnabled = false
                    viewModel.benefits = model.priceWithoutAddition
                    priceAfterTaxEditText.text =
                        (viewModel.priceBefore.toDouble() - model.income).toString()
                    priceTaxEditText.text = "0.0"
                    ratioEditText.setText("0")
                }
            }

            allCostEditText.addTextChangedListener {
                if (it.toString().isNotEmpty()) {
                    saveBtn.isEnabled = true
                    viewModel.totalInstallmentsNumber = it.toString()
                    remainingInstallmentEditText.setText((allCostEditText.text.toString()
                        .toDouble() - paidInstallmentEditText.text.toString()
                        .toDouble()).toString())
                    monthlyPay()
                } else {
                    saveBtn.isEnabled = false
                }
            }

            payDayEditText.addTextChangedListener {
                if (it.toString().isNotEmpty()) {
                    saveBtn.isEnabled = true
                    viewModel.dayOfPaying = it.toString()
                } else {
                    saveBtn.isEnabled = false
                    viewModel.dayOfPaying = model.monthlyDayOfPaying
                }
            }

            startDateEditText.addTextChangedListener {
                if (it.toString().isNotEmpty()) {
                    saveBtn.isEnabled = true
                    viewModel.startDate = it.toString()
                } else {
                    saveBtn.isEnabled = false
                    viewModel.startDate = model.startDate
                }
            }

            modelOfCarEditText.addTextChangedListener {
                if (it.toString().isNotEmpty()) {
                    saveBtn.isEnabled = true
                    viewModel.carModel = it.toString()
                } else {
                    saveBtn.isEnabled = false
                    viewModel.carModel = model.nameOfBoughtItems
                }
            }

            theNote.addTextChangedListener {
                if (it.toString().isNotEmpty()) {
                    saveBtn.isEnabled = true
                    viewModel.myNote = it.toString()
                } else {
                    saveBtn.isEnabled = false
                    viewModel.myNote = model.note
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
            //findNavController().navigateUp()
        }


        binding.btnPay.setOnClickListener {

            if (model.historyList.size == model.numberOfTotalInstallments) {
                Toast.makeText(requireContext(), "تم سداد جميع الاقساط", Toast.LENGTH_SHORT).show()
            } else {
                lifecycleScope.launch(Dispatchers.Main) {
                    it.isClickable = false
                    try {
                        if (model.numberOfPaidInstallments < model.numberOfTotalInstallments) {
                            val calendar = Calendar.getInstance()
                            val day = calendar.get(Calendar.DAY_OF_MONTH)
                            val month = calendar.get(Calendar.MONTH)
                            val year = calendar.get(Calendar.YEAR)
                            val currentDate = "${year}/${month + 1}/${day}"
                            viewModel.addDateToItem(model, currentDate)
                            adapter.notifyDataSetChanged()
                            setPayment()
                        }
                    } catch (_: Exception) {
                    }
                    it.isClickable = true
                }
            }
        }

        binding.btnDidnotPay.setOnClickListener {

            if (model.historyList.size == 0) {
                Toast.makeText(requireContext(), "لا يوجد اقساط مدفوعه", Toast.LENGTH_SHORT).show()
            } else {
                lifecycleScope.launch(Dispatchers.Main) {
                    it.isClickable = false
                    if (model.numberOfComingInstallments < model.numberOfTotalInstallments) {
                        try {
                            viewModel.removeLastDateFromItem(model)
                            adapter.notifyDataSetChanged()
                            setViews()
                            //saveNewData()
                        } catch (_: Exception) {
                        }
                        it.isClickable = true
                    }
                }
            }
        }

        binding.backArrow.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupHistoryRV() {
        myList = model.historyList
        adapter = HistoryAdapter(myList)
        binding.rvHistoryView.adapter = adapter
        binding.rvHistoryView.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun setPayment() {

        binding.apply {
            //قيمة الاقساط المسددة
            allCostPaidEditText.setText(model.valueOfPayInstallments.toDouble().roundToInt()
                .toString())
            //عدد الاقساط المدفوعه
            paidInstallmentEditText.setText(model.numberOfPaidInstallments.toString())
            //قيمة الاقساط المتبقيه
            costRemainingInstallmentEditText.setText(
                (model.priceAfterAddition.toDouble() - model.valueOfPayInstallments.toDouble()).roundToInt()
                    .toString())
            //عدد اللاقساط المتبقيه
            remainingInstallmentEditText.setText("${model.numberOfTotalInstallments - model.numberOfPaidInstallments}")

        }

    }

    private fun setViews() {
        model = args.model
        binding.apply {
            //اسم العميل
            clientName.setText(model.name)
            //رقم العميل
            numberEditText.setText(model.phoneNumber)
            //المقدم
            incomeEditText.setText(model.income.toString().toDouble().roundToInt().toString())
            //السعر قبل الزياده
            priceBeforeTaxEditText.setText(model.priceWithoutAddition.toDouble().roundToInt()
                .toString())
            //نسبة الفائدة
            ratioEditText.setText("${model.addintionPercentage} %")
            //قيمة الفائدة
            priceTaxEditText.setText(model.additionMoney)
            //السعر بعد الفائدة
            priceAfterTaxEditText.setText(model.priceAfterAddition)
            // الاقساط الكلية عدد
            allCostEditText.setText(model.numberOfTotalInstallments.toString())
            //قيمة الاقساط المسددة
            allCostPaidEditText.setText(model.valueOfPayInstallments.toDouble().roundToInt().toString())
            //عدد الاقساط المدفوعه
            paidInstallmentEditText.setText(model.numberOfPaidInstallments.toString())
            //قيمة الاقساط المتبقيه
            costRemainingInstallmentEditText.setText(
                (model.priceAfterAddition.toDouble() - model.valueOfPayInstallments.toDouble()).roundToInt().toString())
            //عدد اللاقساط المتبقيه
            remainingInstallmentEditText.setText("${model.numberOfTotalInstallments - model.numberOfPaidInstallments}")
            //يوم السداد
            payDayEditText.setText(model.monthlyDayOfPaying)
            //تاريخ البداية
            startDateEditText.setText(model.startDate)
            //تاريخ النهايه
            //endDateEditText.setText("not Yet")
            // موديل السيارة
            modelOfCarEditText.setText(model.nameOfBoughtItems)
            //note
            theNote.setText(model.note)
            //القسط
            montthlyPayEditText.setText(model.monthlyPay.toDouble().roundToInt().toString())
        }
    }

    private fun saveNewData() {
        GlobalScope.launch {
            viewModel.saveData(model)
        }
    }

    private fun checkIfDataChanged(): Boolean =
        viewModel.isNewData(model)
}