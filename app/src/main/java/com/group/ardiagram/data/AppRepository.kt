package com.group.ardiagram.data

import android.util.Log
import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class AppRepository(private val projectDAO: ProjectDAO) {

    val allProjects: Flow<List<Project>> = projectDAO.getAll()

    @WorkerThread
    suspend fun insert(project: Project) {
        Log.d("MyLog", "Project was added. $project")
        projectDAO.insert(project)
    }

    @WorkerThread
    suspend fun change(project: Project) {
        Log.d("MyLog", "Project was updated. $project")
        projectDAO.update(project)
    }

    @WorkerThread
    suspend fun delete(project: Project) {
        projectDAO.delete(project)
    }
}