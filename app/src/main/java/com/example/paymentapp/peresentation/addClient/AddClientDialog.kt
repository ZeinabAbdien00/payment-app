package com.example.paymentapp.peresentation.addClient

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.viewModelScope
import com.example.paymentapp.data.models.BaseModel
import com.example.paymentapp.databinding.FragmentAddClientDialogBinding
import com.example.paymentapp.peresentation.home.HomeViewModel
import kotlinx.coroutines.launch
import java.util.*


class AddClientDialog(private val viewModel: HomeViewModel) : DialogFragment() {

    private lateinit var binding: FragmentAddClientDialogBinding
    private lateinit var currentDate: String
    private lateinit var name: String
    private lateinit var phoneNumber: String
    private lateinit var itemName: String
    private var price = 0.0f
    private var benefits = 0.0f
    private var numberOfMonths = 1
    private var monthlyPay = 0.0f
    private var fullPrice = 0.0f
    private var today: Int = 0
    private var income: Float = 0.0f


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        requireActivity().setFinishOnTouchOutside(false)
        binding = FragmentAddClientDialogBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews()
        setOnClickListeners()
    }

    @SuppressLint("SimpleDateFormat")
    private fun setViews() {
        val metrics = resources.displayMetrics
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        this.dialog!!.window!!.setLayout(((9 * width) / 10), (9 * height) / 10)

        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        currentDate = "${year}/${month}/${day}"
        today = day
        binding.startDatePicker.updateDate(year, month, day)
    }

    private fun setOnClickListeners() {
        binding.cancelBtn.setOnClickListener {
            this.dismiss()
        }

        binding.nameEditText.doAfterTextChanged {
            name = it.toString()
        }

        binding.itemNameText.doAfterTextChanged {
            itemName = it.toString()
        }

        binding.numberEditText.doAfterTextChanged {
            phoneNumber = it.toString()
        }

        binding.priceEditText.doAfterTextChanged {
            price = if (it.toString().isNotEmpty()) {
                it.toString().toFloat()
            } else {
                0.0f
            }

            fullPrice = price + (price * benefits / 100)
            binding.fullPrice.text = String.format("%.2f", fullPrice)
        }

        binding.BenefitEditText.doAfterTextChanged {
            benefits = if (it.toString().isNotEmpty()) {
                it.toString().toFloat()
            } else {
                0.0f
            }
            fullPrice = (price-income) + ((price-income) * benefits / 100)
            binding.fullPrice.text = String.format("%.2f", fullPrice)

            monthlyPay = (fullPrice )/ numberOfMonths
            binding.installmentText.text = String.format("%.2f", monthlyPay)

        }

        binding.monthEditText.doAfterTextChanged {
            numberOfMonths = if (it.toString().isNotEmpty() && it.toString().toInt() > 0) {
                it.toString().toInt()
            } else {
                1
            }
            monthlyPay = (fullPrice) / numberOfMonths
            binding.installmentText.text = String.format("%.2f", monthlyPay)
        }


        binding.fullPrice.doAfterTextChanged {
            monthlyPay = (fullPrice / numberOfMonths)
            binding.installmentText.text = String.format("%.2f", monthlyPay)
        }

        binding.startDatePicker.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
            currentDate = "${year}/${monthOfYear + 1}/${dayOfMonth}"
            today = dayOfMonth
        }

        binding.incomeDialogEditText.doAfterTextChanged {
            if (it!!.isNotEmpty()) {
                income =it.toString().toFloat()
                monthlyPay = (fullPrice- income) / numberOfMonths
                binding.installmentText.text = String.format("%.2f", monthlyPay)

                fullPrice = (price-income)
                binding.fullPrice.text = String.format("%.2f", fullPrice)
            } else {
                income =0.0f
            }
        }

        binding.addBtn.setOnClickListener {
            viewModel.viewModelScope.launch {
                try {


                    if (today == 29) today = 28
                    else if (today == 30 || today == 31) today = 1

                    viewModel.setNewItemInserted(true)
                    val model = BaseModel(
                        name = name,
                        phoneNumber = phoneNumber,
                        priceWithoutAddition = price.toString(),
                        priceAfterAddition = fullPrice.toString(),
                        addintionPercentage = benefits,
                        numberOfTotalInstallments = numberOfMonths,
                        monthlyDayOfPaying = today.toString(),
                        startDate = currentDate,
                        nameOfBoughtItems = itemName,
                        monthlyPay = monthlyPay.toString(),
                        additionMoney = (fullPrice - price).toString(),
                        income = income
                    )
                    model.historyList = ArrayList()
                    viewModel.insertToRoom(model)
                    this@AddClientDialog.dismiss()
                } catch (E: Exception) {
                    Toast.makeText(requireActivity(), "ادخل جميع البيانات", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }


    }


}