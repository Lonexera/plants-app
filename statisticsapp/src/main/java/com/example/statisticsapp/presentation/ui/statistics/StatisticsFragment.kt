package com.example.statisticsapp.presentation.ui.statistics

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.statisticsapp.R
import com.example.statisticsapp.databinding.FragmentStatisticsBinding
import com.example.statisticsapp.presentation.ui.statistics.adapter.PlantAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticsFragment : Fragment(R.layout.fragment_statistics) {

    private val binding: FragmentStatisticsBinding by viewBinding(FragmentStatisticsBinding::bind)
    private val viewModel: StatisticsViewModel by viewModels()
    private val plantAdapter = PlantAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewModel) {
            isLoading.observe(viewLifecycleOwner) { isLoading ->
                binding.pbLoading.isVisible = isLoading
            }

            plantsWithTasksStatistics.observe(viewLifecycleOwner) {
                plantAdapter.submitList(it)
            }

            error.observe(viewLifecycleOwner) {
                it.getContentIfNotHandled()?.let { errorId ->
                    Snackbar
                        .make(binding.rvPlants, errorId, Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        }

        with(binding) {
            rvPlants.adapter = plantAdapter
        }
    }
}
