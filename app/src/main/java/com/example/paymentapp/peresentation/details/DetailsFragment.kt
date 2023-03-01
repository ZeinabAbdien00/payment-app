package com.example.paymentapp.peresentation.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.paymentapp.data.models.BaseModel
import com.example.paymentapp.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment() {

    private val args: DetailsFragmentArgs by navArgs()
    private lateinit var binding: FragmentDetailsBinding
    private lateinit var model: BaseModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews()

    }

    private fun setViews() {
        model = args.model
        binding.apply {
            numberEditText.setText(model.phoneNumber)
            //السعر قبل الزياده
            priceBeforeTaxEditText.setText(model.priceWithoutAddition)
            //نسبة الفائدة
            ratioEditText.setText(model.addintionPercentage.toString())
            //قيمة الفائدة
            priceTaxEditText.setText(model.additionMoney)
            //السعر بعد الفائدة
            priceAfterTaxEditText.setText(model.priceAfterAddition)
            //السعر الاجمالي
            allCostEditText.setText(model.numberOfTotalInstallments.toString())
            //قيمة الاقساط المسددة
            allCostPaidEditText.setText(model.valueOfPayInstallments)
            //عدد الاقساط المدفوعه
            paidInstallmentEditText.setText(model.numberOfPaidInstallments.toString())
            //قيمة الاقساط المتبقيه
            costRemainingInstallmentEditText.setText((
                    model.priceAfterAddition.toFloat() - model.valueOfPayInstallments.toFloat()
                    ).toString())
            //عدد اللاقساط المتبقيه
            remainingInstallmentEditText.setText(model.numberOfComingInstallments.toString())
            //يوم السداد
            payDayEditText.setText(model.monthlyDayOfPaying)
            //تاريخ البداية
            startDateEditText.setText(model.startDate)
            //تاريخ النهايه
            endDateEditText.setText("not Yet")
            // موديل السيارة
            modelOfCarEditText.setText(model.nameOfBoughtItems)
        }
    }
}