package com.group.ardiagram.data

import com.aspose.threed.Scene

class ObjToGlbConverter (private val inputPathObj : String, private val outputPathGlb : String) {
    fun convert() {
        val scene: Scene = Scene.fromFile(inputPathObj)
        scene.save(outputPathGlb)
    }
}