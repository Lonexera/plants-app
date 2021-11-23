package com.example.plantsapp.presentation.ui.tasks.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plantsapp.databinding.ItemPlantWithTasksBinding
import com.example.plantsapp.domain.model.Plant
import com.example.plantsapp.domain.model.Task
import com.example.plantsapp.domain.model.TaskWithState
import com.example.plantsapp.presentation.ui.utils.loadPicture

// TODO Maybe add shared RecyclerViewPool
class PlantWithTasksViewHolder(
    private val binding: ItemPlantWithTasksBinding,
    onTaskClick: (Task) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private val tasksAdapter = TasksAdapter(onTaskClick)

    fun bind(plant: Plant, tasks: List<TaskWithState>) {
        with(binding) {
            tvTasksPlantName.text = plant.name.value

            ivTasksPlantPicture.loadPicture(plant.plantPicture)

            rvPlantsWithTasks.apply {
                layoutManager = LinearLayoutManager(binding.root.context)
                adapter = tasksAdapter
            }

            tasksAdapter.submitList(tasks)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onTaskClick: (Task) -> Unit
        ): PlantWithTasksViewHolder {

            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemPlantWithTasksBinding.inflate(inflater, parent, false)

            return PlantWithTasksViewHolder(binding, onTaskClick)
        }
    }
}
