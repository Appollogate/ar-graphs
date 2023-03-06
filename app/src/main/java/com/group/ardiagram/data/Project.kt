package com.group.ardiagram.data

import com.google.ar.sceneform.math.Vector3
import java.io.Serializable

data class Project(
    var name: String,
    var pathToTableFile: String = "",
    var points: List<Vector3> = listOf(),
    var labels: List<String> = listOf()
) : Serializable