package com.example.plantsapp.presentation.ui.plants

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.plantsapp.R
import com.example.plantsapp.presentation.ui.plantcreation.PlantCreationFragment
import com.example.plantsapp.presentation.ui.plantdetail.PlantDetailFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlantsFragment : Fragment(R.layout.fragment_plants) {

    private val plantsViewModel: PlantsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                PlantsScreen(
                    viewModel = plantsViewModel,
                    onNavigateToPlantDetail = { plant ->
                        openFragment(
                            PlantDetailFragment.newInstance(plant.name)
                        )
                    },
                    onNavigateToPlantCreation = {
                        openFragment(PlantCreationFragment())
                    },
                )
            }
        }
    }

    private fun openFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(fragment.tag)
            .commit()
    }
}
