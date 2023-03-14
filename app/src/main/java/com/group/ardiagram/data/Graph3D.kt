package com.group.ardiagram.data

import com.google.ar.sceneform.math.Vector3

class Graph3D(
    function: String,
    xMin: Float,
    xMax: Float,
    yMin: Float,
    yMax: Float,
    zMin: Float,
    zMax: Float,
    private val xCount: Int = 100,  // graph length
    private val yCount: Int = 100   // graph width
) {
    private val vertexMatrix: Array<Array<Vector3>>

    init {
        // todo: clamp z values between zMin and zMax
        val expression = ARExpression(function)
        val xDelta: Float = (xMax - xMin) / (xCount - 1)
        val yDelta: Float = (yMax - yMin) / (yCount - 1)
        vertexMatrix = getVertices(expression, xMin, xDelta, yMin, yDelta)
    }

    fun writeFile(objPath: String, glbPath: String) {
        // Constructs the graph 3d model and writes it to an OBJ file
        ObjBuilder(objPath, vertexMatrix).build()
        // Converts OBJ file to a GLB file which can be read by SceneForm
        ObjToGlbConverter(objPath, glbPath).convert()
    }

    private fun getVertices(
        expression: ARExpression,
        xMin: Float, xDelta: Float,
        yMin: Float, yDelta: Float,
    ): Array<Array<Vector3>> {
        val matrix = Array(xCount) { Array(yCount) { Vector3.zero() } }
        for (i in 0 until xCount) {
            for (j in 0 until yCount) {
                val x = xMin + xDelta * i
                val y = yMin + yDelta * j
                val z = expression.setX(x).setY(y).evaluate()
                // coordinates are swapped because SceneForm's axis orientation is different
                matrix[i][j] = Vector3(x, z, y)
            }
        }
        return matrix
    }
}