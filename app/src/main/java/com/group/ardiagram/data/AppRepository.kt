package com.group.ardiagram.data

import android.util.Log
import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class AppRepository(private val projectDAO: ProjectDAO) {

    val allProjects: Flow<List<Project>> = projectDAO.getAll()

    @WorkerThread
    suspend fun insert(project: Project) {
        projectDAO.insert(project)
    }

    @WorkerThread
    suspend fun change(project: Project) {
        projectDAO.update(project)
    }

    @WorkerThread
    suspend fun delete(project: Project) {
        projectDAO.delete(project)
    }
}