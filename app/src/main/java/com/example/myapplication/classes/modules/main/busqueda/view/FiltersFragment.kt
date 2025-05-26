package com.example.myapplication.classes.modules.main.busqueda.view

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.R
import com.example.myapplication.classes.components.MyCustomChip
import com.example.myapplication.classes.components.MyCustomChipGroup
import com.example.myapplication.classes.models.API.Genre
import com.example.myapplication.databinding.FragmentFiltersBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Calendar
import kotlin.toString

class FiltersFragment(
    private val filters: List<Genre>,
    private var filtersApplied: List<Int>,
    private var orderApplied: String,
    private var datesApplied: String,
    private val onFilterSelected: (selectedFilters: List<Int>, dates: String, selectedOrder: String) -> Unit
): BottomSheetDialogFragment() {

    private lateinit var binding: FragmentFiltersBinding
    private var dates: String = "&"
    private var selectedOrder: String = ""
    val selectedFilters = mutableListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFiltersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filters.forEach { actualGenre ->
            val chip = MyCustomChip(requireContext())
            chip.bind(actualGenre, filtersApplied)

            chip.setListenersChecked(chip.existsSection)

            binding.chipGroupFilters.addView(chip)
        }

        binding.chipGroupFilters.setOnCheckedStateChangeListener { group, checkedIds ->
            for (i in 0 until group.childCount){
                val chip = group.getChildAt(i) as MyCustomChip
                chip.setListenersChecked(checkedIds.contains(chip.id))
            }
        }
        setBeforeData()

        setListeners()
    }

    private fun setBeforeData(){

        val orderType = orderApplied.substringBefore(".").trim()
        val orderUpDown = orderApplied.substringAfter(orderType).trim()


        binding.chipGroupOrder.applyOrder(orderType)
        binding.chipGroupHighLow.applyOrder(orderUpDown)

        val dates = datesApplied.split("&")
        if(!dates.get(0).isNullOrEmpty())
            binding.etStartDate.setText(dates.get(0))
        if(!dates.get(1).isNullOrEmpty())
            binding.etEndDate.setText(dates.get(1))

    }


    private fun setListeners() {
        binding.apply {
            chipGroupOrder.setOnCheckedStateChangeListener { group, checkedIds ->
                for (i in 0 until group.childCount) {
                    val chip = group.getChildAt(i) as MyCustomChip
                    chip.setListenersChecked(checkedIds.contains(chip.id))
                }
            }

            chipGroupHighLow.setOnCheckedStateChangeListener { group, checkedIds ->
                for (i in 0 until group.childCount) {
                    val chip = group.getChildAt(i) as MyCustomChip
                    chip.setListenersChecked(checkedIds.contains(chip.id))
                }
            }

            btCalendarStart.setOnClickListener {
                showDatePicker { date ->
                    etStartDate.setText(date)
                }
            }

            btCalendarEnd.setOnClickListener {
                showDatePicker { date ->
                    etEndDate.setText(date)
                }
            }


            binding.btReset.setOnClickListener {
                clearData()
            }

            btFilters.setOnClickListener {

                dates = etStartDate.text.toString() + "&" + etEndDate.text.toString()
                dates.replace("/","-")


                val chipGroupList = listOf<MyCustomChipGroup>(chipGroupOrder,chipGroupHighLow,chipGroupFilters)

                chipGroupList.forEach { chipGroup ->
                    if(chipGroup.hasGenreChips){
                        for (i in 0 until chipGroup.childCount){
                            val chip = chipGroup.getChildAt(i) as MyCustomChip
                            if (chip.isChecked) selectedFilters.add(chip.id)
                        }
                    }
                    else{
                        for (i in 0 until chipGroup.childCount){
                            val chip = chipGroup.getChildAt(i) as MyCustomChip
                            if(chip.isChecked)
                                when(chip.text){
                                    chipPop.text -> selectedOrder = getString(R.string.popularity)
                                    chipDate.text -> selectedOrder = getString(R.string.release_date)
                                    chipAsc.text -> selectedOrder += getString(R.string.ascender)
                                    chipDesc.text -> selectedOrder += getString(R.string.descender)
                                }
                        }
                    }
                }
                onFilterSelected(selectedFilters, dates, selectedOrder)
                dismiss()
            }
        }
    }

    private fun showDatePicker(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = "${selectedDay.toString().padStart(2, '0')}/" +
                        "${(selectedMonth + 1).toString().padStart(2, '0')}/" +
                        "$selectedYear"
                onDateSelected(formattedDate)
            }, year, month, day
        )

        datePickerDialog.show()
    }

    private fun clearData(){
        selectedFilters.clear()
        filtersApplied = emptyList()

        binding.apply {
            etStartDate.setText(getString(R.string.white))
            etEndDate.setText(getString(R.string.white))

            chipGroupOrder.setDefault()
            chipGroupHighLow.setDefault()
            chipGroupFilters.setDefault()
        }
    }

}