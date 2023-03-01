package com.example.paymentapp.peresentation.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.paymentapp.data.models.BaseModel
import com.example.paymentapp.databinding.FragmentDetailsBinding
import com.example.paymentapp.peresentation.recyclerView.HistoryAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews()
        setupHistoryRV()
        setOnClicks()
    }

    private fun setOnClicks() {
        binding.btnPay.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Main) {
                it.isClickable = false
                try {
                    val calendar = Calendar.getInstance()
                    val day = calendar.get(Calendar.DAY_OF_MONTH)
                    val month = calendar.get(Calendar.MONTH)
                    val year = calendar.get(Calendar.YEAR)
                    val currentDate = "${year}/${month}/${day}"
                    viewModel.addDateToItem(model, currentDate)
                    adapter.notifyDataSetChanged()
                } catch (_: Exception) {
                }
                it.isClickable = true
            }
        }

        binding.btnDidnotPay.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Main) {
                it.isClickable = false
                try {
                    viewModel.removeLastDateFromItem(model)
                    adapter.notifyDataSetChanged()
                } catch (_: Exception) {
                }
                it.isClickable = true
            }
        }
    }

    private fun setupHistoryRV() {
            myList = model.historyList
            adapter = HistoryAdapter(myList)
            binding.rvHistoryView.adapter = adapter
            binding.rvHistoryView.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun setViews() {
        model = args.model
        binding.apply {
            numberEditText.setText(model.phoneNumber)
            //السعر قبل الزياده
            priceBeforeTaxEditText.setText(model.priceWithoutAddition)
            //نسبة الفائدة
            ratioEditText.setText("${model.addintionPercentage} %")
            //قيمة الفائدة
            priceTaxEditText.setText(model.additionMoney)
            //السعر بعد الفائدة
            priceAfterTaxEditText.setText(model.priceAfterAddition)
            // الاقساط الكلية عدد
            allCostEditText.setText(model.numberOfTotalInstallments.toString())
            //قيمة الاقساط المسددة
            allCostPaidEditText.setText(model.valueOfPayInstallments)
            //عدد الاقساط المدفوعه
            paidInstallmentEditText.setText(model.numberOfPaidInstallments.toString())
            //قيمة الاقساط المتبقيه
            costRemainingInstallmentEditText.setText(
                (
                        model.priceAfterAddition.toFloat() - model.valueOfPayInstallments.toFloat()
                        ).toString()
            )
            //عدد اللاقساط المتبقيه
            remainingInstallmentEditText.setText(
                "${model.numberOfTotalInstallments - model.numberOfPaidInstallments}"
            )
            //يوم السداد
            payDayEditText.setText(model.monthlyDayOfPaying)
            //تاريخ البداية
            startDateEditText.setText(model.startDate)
            //تاريخ النهايه
//            endDateEditText.setText("not Yet")
            // موديل السيارة
            modelOfCarEditText.setText(model.nameOfBoughtItems)
        }
    }
}