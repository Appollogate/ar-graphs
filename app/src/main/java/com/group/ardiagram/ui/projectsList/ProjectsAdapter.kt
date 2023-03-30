package com.group.ardiagram.ui.projectsList

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.group.ardiagram.data.Project
import com.group.ardiagram.databinding.ProjectItemBinding

class ProjectsAdapter(
    private val onItemClicked: (Project) -> Unit,
    private val onEditClicked: (Project) -> Unit,
    private val onDeleteClicked: (Project) -> Unit,
    private val onShareClicked: (Project) -> Unit
) : ListAdapter<Project, ProjectsAdapter.ProjectsViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Project>() {
            override fun areItemsTheSame(oldItem: Project, newItem: Project): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Project, newItem: Project): Boolean {
                return oldItem.name == newItem.name
            }
        }
    }

    class ProjectsViewHolder(
        private var binding: ProjectItemBinding,
        val context: Context,
        private val onEditClicked: (Project) -> Unit,
        private val onDeleteClicked: (Project) -> Unit,
        private val onShareClicked: (Project) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(project: Project) = with(binding) {
            projectName.text = project.name
            projectDescription.text = "Source: " + when {
                project.function != null -> project.function
                project.pathToTableFile.isNotEmpty() -> "Points from ${
                    project.pathToTableFile.split(
                        "/"
                    ).last()
                }"

                else -> "Manual points"
            }

            buttonChange.setOnClickListener {
              onEditClicked(project)
            }

            buttonDelete.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Are you sure you want to delete '${project.name}'?")
                builder.setPositiveButton("Yes") { _, _ -> onDeleteClicked(project) }
                builder.setNegativeButton("No", null)

                val dialog = builder.create()
                dialog.show()
            }

            buttonShare.setOnClickListener {
                onShareClicked(project)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectsViewHolder {
        val viewHolder = ProjectsViewHolder(
            ProjectItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            parent.context,
            onEditClicked,
            onDeleteClicked,
            onShareClicked
        )

        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.bindingAdapterPosition
            onItemClicked(getItem(position))
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: ProjectsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}