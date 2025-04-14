package com.example.plantsapp.presentation.ui.plantdetail

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.plantsapp.R
import com.example.plantsapp.domain.model.Plant
import com.example.plantsapp.presentation.ui.loading.LoadingDialog
import com.example.plantsapp.presentation.ui.loading.hideOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlantDetailFragment : Fragment(R.layout.fragment_plant_detail) {

    @Inject
    lateinit var assistedFactory: DetailViewModelAssistedFactory
    private val detailViewModel: PlantDetailViewModel by viewModels {
        DetailViewModelFactory(
            assistedFactory = assistedFactory,
            plantName = requireArguments().plantName
        )
    }
    private val loadingDialog by lazy { LoadingDialog(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                PlantDetailScreen(
                    viewModel = detailViewModel,
                    onNavigateBack = {
                        requireActivity().supportFragmentManager.popBackStack()
                    }
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingDialog.hideOnLifecycle(viewLifecycleOwner)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_plant_detail_appbar, menu)
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
