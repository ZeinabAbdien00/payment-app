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
import kotlinx.coroutines.GlobalScope
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
        setInitials()
        setOnChangeLogic()
    }

    private fun setInitials() {
        viewModel.phone = model.phoneNumber
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
        binding.numberEditText.doAfterTextChanged {
            if (it.toString().isNotEmpty()) {
                viewModel.phone = it.toString()
            } else {
                viewModel.phone = model.phoneNumber
                binding.numberEditText.hint = model.phoneNumber
            }
        }

        binding.priceBeforeTaxEditText.doAfterTextChanged {
            if (it.toString().isNotEmpty()) {
                viewModel.priceBefore = it.toString()
            } else {
                viewModel.priceBefore = model.priceWithoutAddition
                binding.priceBeforeTaxEditText.hint = model.priceWithoutAddition
            }
        }
        binding.ratioEditText.doAfterTextChanged {
            if (it.toString().isNotEmpty()) {
                viewModel.benefits = it.toString()
            } else {
                viewModel.benefits = model.priceWithoutAddition
                binding.ratioEditText.hint = model.addintionPercentage.toString()
            }
        }

        binding.priceTaxEditText.doAfterTextChanged {
            if (it.toString().isNotEmpty()) {
                viewModel.benefitsValue = it.toString()
            } else {
                viewModel.benefitsValue = model.additionMoney
                binding.priceTaxEditText.hint = model.additionMoney.toString()
            }
        }

        binding.priceAfterTaxEditText.doAfterTextChanged {
            if (it.toString().isNotEmpty()) {
                viewModel.priceAfter = it.toString()
            } else {
                viewModel.priceAfter = model.priceAfterAddition
                binding.priceAfterTaxEditText.hint = model.priceAfterAddition.toString()
            }
        }

        binding.allCostEditText.doAfterTextChanged {
            if (it.toString().isNotEmpty()) {
                viewModel.totalInstallmentsNumber = it.toString()
            } else {
                viewModel.totalInstallmentsNumber = model.numberOfTotalInstallments.toString()
                binding.allCostEditText.hint = model.numberOfTotalInstallments.toString()
            }
        }

        binding.allCostPaidEditText.doAfterTextChanged {
            if (it.toString().isNotEmpty()) {
                viewModel.payiedInstallmentsValue = it.toString()
            } else {
                viewModel.payiedInstallmentsValue = model.valueOfPayInstallments.toString()
                binding.allCostPaidEditText.hint = model.valueOfPayInstallments.toString()
            }
        }
        binding.paidInstallmentEditText.doAfterTextChanged {
            if (it.toString().isNotEmpty()) {
                viewModel.payiedInstallmentsNumber = it.toString()
            } else {
                viewModel.payiedInstallmentsNumber = model.numberOfPaidInstallments.toString()
                binding.paidInstallmentEditText.hint = model.numberOfPaidInstallments.toString()
            }
        }

        binding.costRemainingInstallmentEditText.doAfterTextChanged {
            if (it.toString().isNotEmpty()) {
                viewModel.comingInstallmentsVlaue = it.toString()
            } else {
                viewModel.comingInstallmentsVlaue = model.valueOfComingInstallments.toString()
                binding.costRemainingInstallmentEditText.hint =
                    model.valueOfComingInstallments.toString()
            }
        }

        binding.remainingInstallmentEditText.doAfterTextChanged {
            if (it.toString().isNotEmpty()) {
                viewModel.comingInstallmentsVlaue = it.toString()
            } else {
                viewModel.comingInstallmentsVlaue = model.valueOfComingInstallments.toString()
                binding.remainingInstallmentEditText.hint =
                    model.valueOfComingInstallments.toString()
            }
        }

        binding.payDayEditText.doAfterTextChanged {
            if (it.toString().isNotEmpty()) {
                viewModel.dayOfPaying = it.toString()
            } else {
                viewModel.dayOfPaying = model.monthlyDayOfPaying.toString()
                binding.payDayEditText.hint = model.monthlyPay.toString()
            }
        }

        binding.startDateEditText.doAfterTextChanged {
            if (it.toString().isNotEmpty()) {
                viewModel.startDate = it.toString()
            } else {
                viewModel.startDate = model.startDate.toString()
                binding.startDateEditText.hint = model.startDate.toString()
            }
        }

        binding.modelOfCarEditText.doAfterTextChanged {
            if (it.toString().isNotEmpty()) {
                viewModel.carModel = it.toString()
            } else {
                viewModel.carModel = model.nameOfBoughtItems.toString()
                binding.modelOfCarEditText.hint = model.nameOfBoughtItems.toString()
            }
        }

        binding.montthlyPayEditText.doAfterTextChanged {
            if (it.toString().isNotEmpty()) {
                viewModel.monthlyPayValue = it.toString()
            } else {
                viewModel.monthlyPayValue = model.monthlyPay.toString()
                binding.montthlyPayEditText.hint = model.monthlyPay.toString()
            }
        }

        binding.theNote.doAfterTextChanged {
            if (it.toString().isNotEmpty()) {
                viewModel.myNote = it.toString()
            } else {
                viewModel.myNote = model.note.toString()
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

    private fun saveNewData() {
        GlobalScope.launch {
            viewModel.saveData(model)
        }
    }
}