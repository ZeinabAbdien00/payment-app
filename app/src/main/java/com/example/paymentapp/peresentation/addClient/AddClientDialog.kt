package com.example.paymentapp.peresentation.addClient

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.viewModelScope
import com.example.paymentapp.data.models.BaseModel
import com.example.paymentapp.databinding.FragmentAddClientDialogBinding
import com.example.paymentapp.peresentation.home.HomeViewModel
import kotlinx.coroutines.launch
import java.util.*


class AddClientDialog(private val viewModel: HomeViewModel) : DialogFragment() {

    lateinit var binding: FragmentAddClientDialogBinding
    private lateinit var currentDate: String
    private lateinit var name: String
    private lateinit var phoneNumebr: String
    private lateinit var itemName : String
    private var price = 0.0f
    private var penfits = 0.0f
    private var numberOfMonths = 1
    private var monthlyPay = 0.0f
    private var fullPrice = 0.0f
    private var today : Int =0


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
               itemName=it.toString()
        }

        binding.numberEditText.doAfterTextChanged {
            phoneNumebr = it.toString()
        }

        binding.priceEditText.doAfterTextChanged {
            price = if (it.toString().isNotEmpty()){
                it.toString().toFloat()
            }else{
                0.0f
            }

            fullPrice = price + (price * penfits / 100)
            binding.fullPrice.text = String.format("%.2f",fullPrice)
        }

        binding.BenefitEditText.doAfterTextChanged {
            penfits=if (it.toString().isNotEmpty()){
            it.toString().toFloat()
            }else{
                0.0f
            }
                fullPrice = price + (price * penfits / 100)
                binding.fullPrice.text = String.format("%.2f",fullPrice)
        }

        binding.monthEditText.doAfterTextChanged {
            numberOfMonths=if (it.toString().isNotEmpty() && it.toString().toInt() > 0){
                it.toString().toInt()
            }else{
                1
            }
                monthlyPay = fullPrice/numberOfMonths
            binding.installmentText.text=String.format("%.2f",monthlyPay)
        }


        binding.fullPrice.doAfterTextChanged {
            monthlyPay = (fullPrice/numberOfMonths)
            binding.installmentText.text=String.format("%.2f",monthlyPay)
        }

        binding.startDatePicker.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
            currentDate = "${year}/${monthOfYear}/${dayOfMonth}"
            today=dayOfMonth
        }

        binding.addBtn.setOnClickListener {
            viewModel.viewModelScope.launch {
                try {
                    viewModel.setNewItemInserted(true)
                val model = BaseModel(
                    name = name,
                    phoneNumber = phoneNumebr,
                    priceWithoutAddition = price.toString(),
                    priceAfterAddition = fullPrice.toString(),
                    addintionPercentage = penfits,
                    numberOfTotalInstallments = numberOfMonths,
                    monthlyDayOfPaying = today.toString(),
                    startDate = currentDate,
                    nameOfBoughtItems = itemName,
                    monthlyPay = monthlyPay.toString()
                )
                viewModel.insertToRoom(model)
                this@AddClientDialog.dismiss()
            }catch (E:Exception){
                    Toast.makeText(requireActivity(),"ادخل جميع البيانات",Toast.LENGTH_SHORT).show()
                }
            }

        }


    }


}