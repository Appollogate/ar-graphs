package com.group.ardiagram.ui.projectsList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.concurrent.timer
import kotlin.coroutines.CoroutineContext

class ProjectsListViewModel : ViewModel() {

    val list: List<String> = listOf()
    private val _text = MutableLiveData<String>().apply {
        if (list.isEmpty()) {
            value = "You don't have any projects yet"
        }
    }
    val text: LiveData<String> = _text

    fun createNewProject() {

    }

}