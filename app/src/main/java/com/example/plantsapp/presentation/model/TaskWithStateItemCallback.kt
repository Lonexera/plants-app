package com.example.plantsapp.presentation.model

import androidx.recyclerview.widget.DiffUtil

class TaskWithStateItemCallback : DiffUtil.ItemCallback<TaskWithState>() {

    override fun areItemsTheSame(oldItem: TaskWithState, newItem: TaskWithState): Boolean {
        return oldItem.task == newItem.task
    }

    override fun areContentsTheSame(oldItem: TaskWithState, newItem: TaskWithState): Boolean {
        return oldItem == newItem
    }
}
