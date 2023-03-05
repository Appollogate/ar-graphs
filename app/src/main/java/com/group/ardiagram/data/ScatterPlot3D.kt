package com.group.ardiagram.data

import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.rendering.Material
import com.google.ar.sceneform.rendering.ShapeFactory

/**
 * Node that represents the origin of a 3-dimensional scatter plot.
 * Contains references to all dots on the scatter plot as children nodes.
 */
class ScatterPlot3D(
    coordinateList: List<Vector3>,
    material: Material
) : Node() {
    private val dotRadius = 0.01f

    init {
        for (dotCenter in coordinateList) {
            val dotNode = Node()
            dotNode.parent = this
            dotNode.renderable = ShapeFactory.makeSphere(dotRadius, Vector3.zero(), material)
            dotNode.localPosition = dotCenter
        }
    }
}