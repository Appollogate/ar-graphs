package com.group.ardiagram.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.ar.sceneform.math.Vector3
import java.io.Serializable

@Entity
data class Project(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var name: String,
    var pathToTableFile: String = "",
    var points: List<Vector3> = listOf(),
    var labels: List<String> = listOf(),
    var function: String? = null,
    var xMin: Float = -10f,
    var xMax: Float = 10f,
    var yMin: Float = -10f,
    var yMax: Float = 10f,
    var zMin: Float = -10f,
    var zMax: Float = 10f
) : Serializable {
    companion object {
        private const val serialVersionUID: Long = 123
    }

    fun resetAllInputs() {
        pathToTableFile = ""
        points = listOf()
        function = null
    }

    fun isNew(): Boolean {
        return pathToTableFile.isEmpty() && points.isEmpty() && function == null
    }
}