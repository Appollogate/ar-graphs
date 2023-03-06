package com.group.ardiagram.ui.projectsList

import android.app.Application
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.ar.sceneform.math.Vector3
import com.group.ardiagram.data.Project
import com.group.ardiagram.notifyObserver
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.lang.Exception

class ProjectsListViewModel(application: Application) : AndroidViewModel(application) {
    private val _projectList = MutableLiveData<ArrayList<Project>>(arrayListOf())
    private val _points = MutableLiveData<ArrayList<Vector3>>(arrayListOf())


    val projectList: LiveData<ArrayList<Project>> get() = _projectList
    private val pointsList: LiveData<ArrayList<Vector3>> get() = _points

    fun addNewProject(project: Project, fileUri: Uri? = null) {
        if (fileUri == null) {
            project.points = pointsList.value?.toList() ?: listOf()
        } else {
            val data = parseExcelFile(fileUri)
            project.points = data.first
            project.labels = data.second
        }

        _projectList.value?.add(project)
        _projectList.notifyObserver()

        Log.d("MyLog", "Project was added. Name: ${project.name}\tPath: ${project.pathToTableFile} Points: ${project.points}")
    }

    fun addPoint(x: Float, y: Float, z: Float) {
        _points.value?.add(Vector3(x, y, z))
        _points.notifyObserver()
    }

    fun changeProjectName(project: Project?, newName: String) {
        project ?: return
        val index: Int = _projectList.value?.indexOf(project) ?: return

        _projectList.value?.get(index)?.name = newName
        _projectList.notifyObserver()
    }

    private fun parseExcelFile(uri: Uri?): Pair<List<Vector3>, List<String>> {
        if (uri == null) return Pair(listOf(), listOf())

        val inputStream = getApplication<Application>().contentResolver.openInputStream(uri)
        val workbook: Workbook? = getWorkbook(inputStream)

        val firstSheet: Sheet =
            if ((workbook?.numberOfSheets ?: 0) > 0)
                workbook?.getSheetAt(0) ?: return Pair(listOf(), listOf())
            else
                return Pair(listOf(), listOf())

        val listOfPoints = mutableListOf<Vector3>()
        val listOfLabels = mutableListOf<String>()

        var shouldTakeLabels = true

        try {
            for (row in firstSheet) {
                if (row.rowNum >= 0) {
                    val cellIterator = row.cellIterator()

                    if (shouldTakeLabels) {
                        val xLabel = cellIterator.next().stringCellValue
                        val yLabel = cellIterator.next().stringCellValue
                        val zLabel = cellIterator.next().stringCellValue

                        listOfLabels.add(xLabel)
                        listOfLabels.add(yLabel)
                        listOfLabels.add(zLabel)
                        shouldTakeLabels = false
                    } else {
                        val x = cellIterator.next().numericCellValue
                        val y = cellIterator.next().numericCellValue
                        val z = cellIterator.next().numericCellValue

                        listOfPoints.add(Vector3(x.toFloat(), y.toFloat(), z.toFloat()))
                    }
                }
            }
        } catch (e: Exception) {
            Toast.makeText(
                getApplication<Application>().baseContext,
                "Error: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
        return Pair(listOfPoints, listOfLabels)
    }

    private fun getWorkbook(inputStream: InputStream?): Workbook? {
        return inputStream.let {
            try {
                WorkbookFactory.create(it)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                Toast.makeText(
                    getApplication<Application>().baseContext,
                    "Error: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
                null
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(
                    getApplication<Application>().baseContext,
                    "Error: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
                null
            }
        }
    }
}