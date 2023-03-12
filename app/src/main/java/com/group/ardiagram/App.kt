package com.group.ardiagram

import android.app.Application
import com.group.ardiagram.data.AppDatabase
import com.group.ardiagram.data.AppRepository

class App : Application() {
    private val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { AppRepository(database.projectDao()) }
}