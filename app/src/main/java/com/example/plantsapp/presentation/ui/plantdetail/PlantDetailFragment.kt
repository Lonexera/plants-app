package com.example.plantsapp.presentation.ui.plantdetail

import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.plantsapp.R
import com.example.plantsapp.databinding.FragmentPlantDetailBinding
import com.example.plantsapp.domain.model.Plant
import com.example.plantsapp.presentation.ui.loading.LoadingDialog
import com.example.plantsapp.presentation.ui.loading.hideOnLifecycle
import com.example.plantsapp.presentation.ui.plantdetail.adapter.DetailTasksAdapter
import com.example.plantsapp.presentation.ui.plantdetail.adapter.PlantPhotosAdapter
import com.example.plantsapp.presentation.ui.utils.loadPicture
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlantDetailFragment : Fragment(R.layout.fragment_plant_detail) {

    private val binding: FragmentPlantDetailBinding by viewBinding(FragmentPlantDetailBinding::bind)

    @Inject
    lateinit var assistedFactory: DetailViewModelAssistedFactory
    private val detailViewModel: PlantDetailViewModel by viewModels {
        DetailViewModelFactory(
            assistedFactory = assistedFactory,
            plantName = requireArguments().plantName
        )
    }
    private val tasksAdapter = DetailTasksAdapter()
    private val photosAdapter = PlantPhotosAdapter()
    private val loadingDialog by lazy { LoadingDialog(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingDialog.hideOnLifecycle(viewLifecycleOwner)

        with(detailViewModel) {
            appBarTitle.observe(viewLifecycleOwner) {
                activity?.title = it
            }

            plantDetailUiState.observe(viewLifecycleOwner) { state ->
                when (state) {
                    is PlantDetailViewModel.PlantDetailUiState.InitialState -> {
                        changeLoadingStates(
                            isLoadingIndicatorVisible = true,
                            isLoadingDialogVisible = false
                        )
                    }
                    is PlantDetailViewModel.PlantDetailUiState.DataIsLoaded -> {
                        changeLoadingStates(
                            isLoadingIndicatorVisible = false,
                            isLoadingDialogVisible = false
                        )
                        showPlantDetail(state.plant)
                        tasksAdapter.submitList(state.tasks)
                    }
                    is PlantDetailViewModel.PlantDetailUiState.LoadingState -> {
                        changeLoadingStates(
                            isLoadingIndicatorVisible = false,
                            isLoadingDialogVisible = true
                        )
                    }
                    is PlantDetailViewModel.PlantDetailUiState.FinalState -> {
                        changeLoadingStates(
                            isLoadingIndicatorVisible = false,
                            isLoadingDialogVisible = false
                        )
                        requireActivity().supportFragmentManager.popBackStack()
                    }
                }
            }

            plantPhotosUiState.observe(viewLifecycleOwner) { state ->
                when (state) {
                    is PlantDetailViewModel.PlantPhotosUiState.InitialState -> {
                        binding.rvPlantPhotos.isVisible = false
                        binding.layoutPlantGalleryShimmering.root.isVisible = true
                    }
                    is PlantDetailViewModel.PlantPhotosUiState.PhotosAreLoaded -> {
                        photosAdapter.submitList(state.photos)
                        binding.rvPlantPhotos.isVisible = true
                        binding.layoutPlantGalleryShimmering.root.isVisible = false
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_plant_detail_appbar, menu)

        detailViewModel.plantDetailUiState.observe(viewLifecycleOwner) { state ->
            menu.findItem(R.id.action_delete_plant).isVisible =
                state is PlantDetailViewModel.PlantDetailUiState.DataIsLoaded
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete_plant -> {
                showDialogDeleteConfirm()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showPlantDetail(plant: Plant) {
        with(binding) {
            ivDetailPlant.loadPicture(plant.plantPicture)

            etDetailPlantName.setText(plant.name.value)
            etDetailSpeciesName.setText(plant.speciesName)

            rvDetailTasks.adapter = tasksAdapter
            rvPlantPhotos.adapter = photosAdapter
        }
    }

    private fun changeLoadingStates(
        isLoadingIndicatorVisible: Boolean,
        isLoadingDialogVisible: Boolean
    ) {
        binding.pbLoading.isVisible = isLoadingIndicatorVisible

        if (isLoadingDialogVisible) loadingDialog.show()
        else loadingDialog.dismiss()
    }

    private fun showDialogDeleteConfirm() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.title_delete_dialog_confirmation)
            .setMessage(R.string.msg_delete_dialog_confirmation)
            .setPositiveButton(R.string.title_dialog_confirmation_btn_positive) { _, _ ->
                detailViewModel.onDelete()
            }
            .setNegativeButton(R.string.title_dialog_confirmation_btn_negative) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    companion object {
        private const val ARGUMENT_PLANT_NAME = "PLANT NAME"
        private var Bundle.plantName: Plant.Name
            get() = Plant.Name(
                getString(ARGUMENT_PLANT_NAME, null)
                    ?: error("You forgot to pass ARGUMENT_PLANT_NAME")
            )
            set(plantName) = putString(ARGUMENT_PLANT_NAME, plantName.value)

        fun newInstance(plantName: Plant.Name): PlantDetailFragment {
            val detailFragment = PlantDetailFragment()
            val args = bundleOf()
            args.plantName = plantName
            detailFragment.arguments = args
            return detailFragment
        }
    }
}
