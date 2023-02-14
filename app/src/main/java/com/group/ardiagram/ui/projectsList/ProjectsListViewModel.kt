package com.group.ardiagram.ui.projectsList

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.group.ardiagram.data.Project

class ProjectsListViewModel : ViewModel() {

    private val _projectList = MutableLiveData<ArrayList<Project>>().apply {
        value = ArrayList<Project>()
    }

    private val _text = MutableLiveData<String>().apply {
        if (_projectList.value!!.isEmpty()) {
            value = "You don't have any projects yet"
        }
    }

    val text: LiveData<String> get() = _text
    val projectList: ArrayList<Project> get() = _projectList.value!!

    val adapter: ProjectsAdapter = ProjectsAdapter(_projectList.value!!)

    fun createNewProject() {

    }

    fun addNewProject(projectName: String, filePath: String) {
        val project = Project(projectName, filePath)
        _projectList.value?.add(project)
        updateProjectListUI()

        Log.d("MyLog", "Project was added. Name: ${project.name}\tPath: ${project.pathToTableFile}")
//        Log.d("MyLog", "Adapter add Project: list size ${adapter.projectList.size}")
    }

    fun updateProjectListUI(){
        adapter.updateUI(_projectList.value!!)
    }
}