package com.example.wanderlog.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.wanderlog.R
import com.google.android.material.slider.RangeSlider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.Calendar
import java.util.Locale


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AddTripFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var selectedDepartureDate: Calendar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_add_trip, container, false)
        val departureEditText = view.findViewById<TextInputEditText>(R.id.departureDateTextInputEditText)
        val departureDateLayout = view.findViewById<TextInputLayout>(R.id.departureDateTextInputLayout)
        val arrivingEditText = view.findViewById<TextInputEditText>(R.id.arrivingDateTextInputEditText)
        val arrivingDateLayout = view.findViewById<TextInputLayout>(R.id.arrivingDateTextInputLayout)
        val priceRangeSlider = view.findViewById<RangeSlider>(R.id.priceRangeSlider)
        val minPriceEditText = view.findViewById<EditText>(R.id.minPriceEditText)
        val maxPriceEditText = view.findViewById<EditText>(R.id.maxPriceEditText)

        departureDateLayout.setEndIconOnClickListener {
            showDatePickerDialog(departureEditText, isDeparture = true)
        }

        departureEditText.setOnClickListener {
            showDatePickerDialog(departureEditText, isDeparture = true)
        }

        arrivingDateLayout.setEndIconOnClickListener {
            showDatePickerDialog(arrivingEditText, isDeparture = false)
        }

        arrivingEditText.setOnClickListener {
            showDatePickerDialog(arrivingEditText, isDeparture = false)
        }

        updateSlider(priceRangeSlider, minPriceEditText, maxPriceEditText)

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddTripFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun showDatePickerDialog(editText: TextInputEditText, isDeparture: Boolean) {
        val calendar = Calendar.getInstance()
        if (isDeparture) {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(requireContext(), { _, selectedYear, monthOfYear, dayOfMonth ->
                selectedDepartureDate = Calendar.getInstance().apply {
                    set(selectedYear, monthOfYear, dayOfMonth)
                }
                val selectedDate = String.format(Locale.getDefault(), "%02d-%02d-%04d", dayOfMonth, monthOfYear + 1, selectedYear)
                editText.setText(selectedDate)
            }, year, month, day)

            dpd.show()
        } else {
            selectedDepartureDate?.let { departureDate ->
                val dpd = DatePickerDialog(requireContext(), { _, selectedYear, monthOfYear, dayOfMonth ->
                    val selectedDate = String.format(Locale.getDefault(), "%02d-%02d-%04d", dayOfMonth, monthOfYear + 1, selectedYear)
                    editText.setText(selectedDate)
                }, departureDate.get(Calendar.YEAR), departureDate.get(Calendar.MONTH), departureDate.get(Calendar.DAY_OF_MONTH))

                dpd.datePicker.minDate = departureDate.timeInMillis
                dpd.show()
            }
        }
    }

    private fun updateSlider(
        priceRangeSlider: RangeSlider,
        minPriceEditText: EditText,
        maxPriceEditText: EditText
    ) {
        priceRangeSlider.addOnChangeListener { slider, _, _ ->
            val values = slider.values
            minPriceEditText.setText(String.format(Locale.US, "$%.0f", values[0]))
            maxPriceEditText.setText(String.format(Locale.US, "$%.0f", values[1]))
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                s?.toString()?.let { text ->
                    if (text.isNotEmpty()) {
                        try {
                            val value = text.replace("$", "").trim().toFloat()
                            if (minPriceEditText.hasFocus()) {
                                priceRangeSlider.values = listOf(value, priceRangeSlider.values[1])
                            } else if (maxPriceEditText.hasFocus()) {
                                priceRangeSlider.values = listOf(priceRangeSlider.values[0], value)
                            }
                        } catch (e: NumberFormatException) {
                            // Handle the number format exception, e.g., reset to default values or show an error
                        }
                    }
                }
            }
        }

        minPriceEditText.addTextChangedListener(textWatcher)
        maxPriceEditText.addTextChangedListener(textWatcher)
    }

}