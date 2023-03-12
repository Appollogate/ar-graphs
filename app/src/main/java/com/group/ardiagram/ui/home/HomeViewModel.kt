package com.group.ardiagram.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.group.ardiagram.App
import com.group.ardiagram.data.Project

class HomeViewModel(application: Application) :  AndroidViewModel(application)  {
    private val repository = (application as App).repository
    private val _projectList: LiveData<List<Project>> = repository.allProjects.asLiveData()
    val projectList: LiveData<List<Project>> get() = _projectList
}