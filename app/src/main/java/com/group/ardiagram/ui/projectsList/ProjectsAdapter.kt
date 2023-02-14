package com.group.ardiagram.ui.projectsList

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.group.ardiagram.R
import com.group.ardiagram.data.Project
import com.group.ardiagram.databinding.ProjectItemBinding

class ProjectsAdapter(projectList: ArrayList<Project> = ArrayList()) :
    RecyclerView.Adapter<ProjectsAdapter.MyViewHolder>() {

    private var _projectList = projectList

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ProjectItemBinding.bind(view)
        fun bind(project: Project) = with(binding) {
            projectName.text = project.name
            buttonChange.setOnClickListener {
                val activity = itemView.context as FragmentActivity
                val nameChangeDialogFragment = NameChangeDialogFragment(project)
                val manager = activity.supportFragmentManager
                nameChangeDialogFragment.show(manager, "nameChange")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.project_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(_projectList[position])
    }

    override fun getItemCount(): Int = _projectList.size

    // notifyDataSetChanged() doesn't work properly
    // that is why updateUI(...) function was added
    @SuppressLint("NotifyDataSetChanged")
    fun updateUI(projects: ArrayList<Project>) {
        _projectList = projects
        notifyDataSetChanged()
    }
}