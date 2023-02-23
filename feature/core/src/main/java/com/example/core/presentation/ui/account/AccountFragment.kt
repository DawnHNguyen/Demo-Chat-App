package com.example.core.presentation.ui.account

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import com.example.common.presentation.BaseFragment
import com.example.common.presentation.utils.AsteriskPasswordTransformationMethod
import com.example.common.utils.hideKeyboard
import com.example.core.R
import com.example.core.databinding.FragmentAccountBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class AccountFragment : BaseFragment<FragmentAccountBinding>() {
    override fun getLayoutId(): Int = R.layout.fragment_account

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.accountParentView.setOnClickListener { hideKeyboard() }

        val cal = Calendar.getInstance()
        binding.editTextMainDob.setText("${cal.get(Calendar.DAY_OF_MONTH)}/${cal.get(Calendar.MONTH) + 1}/${cal.get(Calendar.YEAR)}")

        setDatePicker()

        binding.editTextMainPassword.transformationMethod = AsteriskPasswordTransformationMethod()
    }

    private fun setDatePicker() {
        binding.editTextMainDob.setOnClickListener {
            val cal = Calendar.getInstance()
            hideKeyboard()
            val dateSetListener = DatePickerDialog.OnDateSetListener{ _, year, monthOfYear, dayOfMonth ->
                binding.editTextMainDob.setText("$dayOfMonth/${monthOfYear + 1}/$year")
            }
            DatePickerDialog(
                this.requireContext(),
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }
}