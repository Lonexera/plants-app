package com.example.plantsapp.presentation.ui.plantcreation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.plantsapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlantCreationFragment : Fragment(R.layout.fragment_plant_creation) {

    private val creationViewModel: PlantCreationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                PlantCreationScreen(
                    viewModel = creationViewModel,
                    onNavigateBack = {
                        requireActivity().supportFragmentManager.popBackStack()
                    }
                )
            }
        }
    }
}
