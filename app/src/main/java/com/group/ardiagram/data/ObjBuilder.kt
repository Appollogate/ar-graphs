package com.group.ardiagram.data

import com.google.ar.sceneform.math.Vector3
import de.javagl.obj.ObjWriter
import de.javagl.obj.Objs
import java.io.FileOutputStream
import java.io.OutputStream

class ObjBuilder(private val path: String, private val vertexMatrix: Array<Array<Vector3>>) {
    private val obj = Objs.create()

    fun build() {
        addVertexes()
        addFaces()
        writeFile()
    }

    private fun addVertexes() {
        for (row in vertexMatrix) {
            for (vertex in row) {
                obj.addVertex(vertex.x, vertex.y, vertex.z)
            }
        }
    }

    private fun addFaces() {
        val rowCount = vertexMatrix.size
        // It is expected that colCount is consistent between rows.
        val colCount = if (rowCount > 0) vertexMatrix[0].size else 0
        for (i in 0 until rowCount - 1) {
            for (j in 0 until colCount - 1) {
                obj.addFace(
                    intArrayOf(i * colCount + j, i * colCount + j + 1, (i + 1) * colCount + j),
                    null, null)
                obj.addFace(
                    intArrayOf((i + 1) * colCount + j, (i + 1) * colCount + j + 1, i * colCount + j + 1),
                    null, null
                )
            }
        }
    }

    private fun writeFile() {
        val objOutputStream: OutputStream = FileOutputStream(path)
        ObjWriter.write(obj, objOutputStream)
    }
}