package com.group.ardiagram.ui.projectsList

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.group.ardiagram.data.Project
import com.group.ardiagram.notifyObserver

class ProjectsListViewModel : ViewModel() {
    private val _projectList = MutableLiveData<ArrayList<Project>>(arrayListOf())
    val projectList: LiveData<ArrayList<Project>> get() = _projectList

    fun addNewProject(projectName: String, filePath: String) {
        val project = Project(projectName, filePath)
        _projectList.value?.add(project)
        _projectList.notifyObserver()


        Log.d("MyLog", "Project was added. Name: ${project.name}\tPath: ${project.pathToTableFile}")
//        Log.d("MyLog", "Adapter add Project: list size ${adapter.projectList.size}")
    }

    fun changeProjectName(project: Project?, newName: String) {
        project ?: return
        val index: Int = _projectList.value?.indexOf(project) ?: return

        _projectList.value?.get(index)?.name = newName
        _projectList.notifyObserver()
    }
}