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
    var labels: List<String> = listOf()
) : Serializable {
    companion object {
        private const val serialVersionUID: Long = 123
    }
}