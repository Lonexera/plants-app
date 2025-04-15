package com.example.plantsapp.presentation.ui.plantcreation

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.plantsapp.R
import com.example.plantsapp.databinding.FragmentPlantCreationBinding
import com.example.plantsapp.presentation.ui.loading.LoadingDialog
import com.example.plantsapp.presentation.ui.loading.connectWith
import com.example.plantsapp.presentation.ui.plantdetail.PlantDetailScreen
import com.example.plantsapp.presentation.ui.utils.getCameraImageOutputUri
import com.example.plantsapp.presentation.ui.utils.loadPicture
import com.example.plantsapp.presentation.ui.utils.setUpWithAdapter
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
