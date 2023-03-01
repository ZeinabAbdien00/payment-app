package com.example.paymentapp.peresentation.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
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

    private var phone = ""
    private var priceBefore = ""
    private var benefits = ""
    private var benefitsValue = ""
    private var priceAfter = ""
    private var totalInstallmentsNumber = ""
    private var payiedInstallmentsNumber = ""
    private var payiedInstallmentsValue = ""
    private var comingInstallmentsVlaue = ""
    private var comingInstallmentsNumber = ""
    private var dayOfPaying = ""
    private var startDate = " "
    private var carModel = ""
    private var monthlyPayValue = ""
    private var note=""


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
        setInitials()
        setOnChangeLogic()
    }

    private fun setInitials() {
        phone = model.phoneNumber
        priceBefore = model.priceWithoutAddition
        benefits = model.addintionPercentage.toString()
        benefitsValue = model.additionMoney
        priceAfter = model.priceAfterAddition
        totalInstallmentsNumber = model.numberOfTotalInstallments.toString()
        payiedInstallmentsNumber = model.numberOfPaidInstallments.toString()
        payiedInstallmentsValue = model.valueOfPayInstallments
        comingInstallmentsNumber = model.numberOfComingInstallments.toString()
        comingInstallmentsVlaue = model.valueOfComingInstallments
        dayOfPaying = model.monthlyDayOfPaying
        startDate = model.startDate
        carModel = model.nameOfBoughtItems
        monthlyPayValue = model.monthlyPay
        note = model.note
    }

    private fun setOnChangeLogic() {
        binding.numberEditText.doAfterTextChanged {
            if (it.toString().isNotEmpty()) {
                phone = it.toString()
            } else {
                phone = model.phoneNumber
                binding.numberEditText.hint = model.phoneNumber
            }
        }

        binding.priceBeforeTaxEditText.doAfterTextChanged {
            if (it.toString().isNotEmpty()) {
                priceBefore = it.toString()
            } else {
                priceBefore = model.priceWithoutAddition
                binding.priceBeforeTaxEditText.hint = model.priceWithoutAddition
            }
        }
        binding.ratioEditText.doAfterTextChanged {
            if (it.toString().isNotEmpty()) {
                benefits = it.toString()
            } else {
                benefits = model.priceWithoutAddition
                binding.ratioEditText.hint = model.addintionPercentage.toString()
            }
        }

        binding.priceTaxEditText.doAfterTextChanged {
            if (it.toString().isNotEmpty()) {
                benefitsValue = it.toString()
            } else {
                benefitsValue = model.additionMoney
                binding.priceTaxEditText.hint = model.additionMoney.toString()
            }
        }

        binding.priceAfterTaxEditText.doAfterTextChanged {
            if (it.toString().isNotEmpty()) {
                priceAfter = it.toString()
            } else {
                priceAfter = model.priceAfterAddition
                binding.priceAfterTaxEditText.hint = model.priceAfterAddition.toString()
            }
        }

        binding.allCostEditText.doAfterTextChanged {
            if (it.toString().isNotEmpty()) {
                totalInstallmentsNumber = it.toString()
            } else {
                totalInstallmentsNumber = model.numberOfTotalInstallments.toString()
                binding.allCostEditText.hint = model.numberOfTotalInstallments.toString()
            }
        }

        binding.allCostPaidEditText.doAfterTextChanged {
            if (it.toString().isNotEmpty()) {
                payiedInstallmentsValue = it.toString()
            } else {
                payiedInstallmentsValue = model.valueOfPayInstallments.toString()
                binding.allCostPaidEditText.hint = model.valueOfPayInstallments.toString()
            }
        }
        binding.paidInstallmentEditText.doAfterTextChanged {
            if (it.toString().isNotEmpty()) {
                payiedInstallmentsNumber = it.toString()
            } else {
                payiedInstallmentsNumber = model.numberOfPaidInstallments.toString()
                binding.paidInstallmentEditText.hint = model.numberOfPaidInstallments.toString()
            }
        }

        binding.costRemainingInstallmentEditText.doAfterTextChanged {
            if (it.toString().isNotEmpty()) {
                comingInstallmentsVlaue = it.toString()
            } else {
                comingInstallmentsVlaue = model.valueOfComingInstallments.toString()
                binding.costRemainingInstallmentEditText.hint = model.valueOfComingInstallments.toString()
            }
        }

        binding.remainingInstallmentEditText.doAfterTextChanged {
            if (it.toString().isNotEmpty()) {
                comingInstallmentsVlaue = it.toString()
            } else {
                comingInstallmentsVlaue = model.valueOfComingInstallments.toString()
                binding.remainingInstallmentEditText.hint = model.valueOfComingInstallments.toString()
            }
        }

        binding.payDayEditText.doAfterTextChanged {
            if (it.toString().isNotEmpty()) {
                dayOfPaying = it.toString()
            } else {
                dayOfPaying = model.monthlyDayOfPaying.toString()
                binding.payDayEditText.hint = model.monthlyPay.toString()
            }
        }

        binding.startDateEditText.doAfterTextChanged {
            if (it.toString().isNotEmpty()) {
                startDate = it.toString()
            } else {
                startDate = model.startDate.toString()
                binding.startDateEditText.hint = model.startDate.toString()
            }
        }

        binding.modelOfCarEditText.doAfterTextChanged {
            if (it.toString().isNotEmpty()) {
                carModel = it.toString()
            } else {
                carModel = model.nameOfBoughtItems.toString()
                binding.modelOfCarEditText.hint = model.nameOfBoughtItems.toString()
            }
        }

        binding.montthlyPayEditText.doAfterTextChanged {
            if (it.toString().isNotEmpty()) {
                monthlyPayValue = it.toString()
            } else {
                monthlyPayValue = model.monthlyPay.toString()
                binding.montthlyPayEditText.hint = model.monthlyPay.toString()
            }
        }

        binding.theNote.doAfterTextChanged {
            if (it.toString().isNotEmpty()) {
                note = it.toString()
            } else {
                note = model.note.toString()
                binding.theNote.hint = model.note.toString()
            }
        }


    }

    private fun setOnClicks() {
        binding.btnPay.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Main) {
                it.isClickable = false
                try {
                    if (model.numberOfPaidInstallments < model.numberOfTotalInstallments) {
                        val calendar = Calendar.getInstance()
                        val day = calendar.get(Calendar.DAY_OF_MONTH)
                        val month = calendar.get(Calendar.MONTH)
                        val year = calendar.get(Calendar.YEAR)
                        val currentDate = "${year}/${month}/${day}"
                        viewModel.addDateToItem(model, currentDate)
                        adapter.notifyDataSetChanged()
                        setViews()
                    }
                } catch (_: Exception) {
                }
                it.isClickable = true
            }
        }

        binding.btnDidnotPay.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Main) {
                it.isClickable = false
                if (model.numberOfComingInstallments < model.numberOfTotalInstallments) {
                    try {
                        viewModel.removeLastDateFromItem(model)
                        adapter.notifyDataSetChanged()
                        setViews()
                    } catch (_: Exception) {
                    }
                    it.isClickable = true
                }
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
            //note
            theNote.setText(model.note)
            //القسط
            montthlyPayEditText.setText(model.monthlyPay)
        }
    }
}