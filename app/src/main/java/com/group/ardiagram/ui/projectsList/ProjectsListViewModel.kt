package com.group.ardiagram.ui.projectsList

import android.app.Application
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.aspose.threed.Vector2
import com.google.ar.sceneform.math.Vector3
import com.group.ardiagram.App
import com.group.ardiagram.data.Project
import kotlinx.coroutines.launch
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream

class ProjectsListViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = (application as App).repository
    private val _projectList: LiveData<List<Project>> = repository.allProjects.asLiveData()
    private val _points = MutableLiveData<ArrayList<Vector3>>(arrayListOf())
    private val pointsList: LiveData<ArrayList<Vector3>> get() = _points

    val projectList: LiveData<List<Project>> get() = _projectList

    fun applyManuallyImportProjectData(project: Project?) {
        project ?: return

        val isNewProject = project.isNew()
        project.resetAllInputs()

        val points = pointsList.value?.toList() ?: listOf()

        viewModelScope.launch {
            if (isNewProject) {
                project.points = points
                repository.insert(project)
            } else {
                project.resetAllInputs()
                project.points = points
                repository.change(project)
            }
        }
    }

    fun applyImportProjectData(project: Project?, fileUri: Uri?, path: String) {
        project ?: return

        val isNewProject = project.isNew()
        project.resetAllInputs()

        val (points, labels) = parseExcelFile(fileUri)
        project.points = points
        project.labels = labels
        project.pathToTableFile = path

        viewModelScope.launch {
            if (isNewProject) {
                repository.insert(project)
            } else {
                repository.change(project)
            }
        }
    }

    fun applyProjectFunction(
        project: Project?,
        function: String,
        xScope: List<Float>,
        yScope: List<Float>,
        zScope: List<Float>
    ) {
        project ?: return

        val isNewProject = project.isNew()
        project.resetAllInputs()

        project.function = function
//        project.xScope = xScope
//        project.yScope = yScope
//        project.zScope = zScope

        viewModelScope.launch {
            if (isNewProject) {
                repository.insert(project)
            } else {
                repository.change(project)
            }
        }
    }

    fun changeProjectName(project: Project?, newName: String) {
        project ?: return
        project.name = newName

        viewModelScope.launch {
            repository.change(project)
        }
    }

    fun deleteProject(project: Project) {
        viewModelScope.launch {
            repository.delete(project)
        }
    }

    fun addPoint(x: Float, y: Float, z: Float) {
        _points.value?.add(Vector3(x, y, z))
    }

    fun checkNameFieldIsCorrect(name: String): Boolean {
        return !(_projectList.value?.any { project ->
            project.name == name
        } ?: false) && name.isNotEmpty()
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
        return listOfPoints to listOfLabels
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