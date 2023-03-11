package com.group.ardiagram.data

import androidx.room.TypeConverter
import com.google.ar.sceneform.math.Vector3

class Converters {

    @TypeConverter
    fun fromVectorList(value: List<Vector3>): String {
        return value.joinToString(",")
    }

    @TypeConverter
    fun fromVectorString(value: String): List<Vector3> {
        return value.split("],[").map {
            val xyz = it.split(",")
            val x: Float = xyz[0].removePrefix("[x=").toFloatOrNull() ?: 0f
            val y: Float = xyz[1].removePrefix("y=").toFloatOrNull() ?: 0f
            val z: Float = xyz[2].removePrefix("z=").removePrefix("]").toFloatOrNull() ?: 0f
            Vector3(x, y, z)
        }
    }

    @TypeConverter
    fun fromString(value: String): List<String> {
        return value.split(",")
    }

    @TypeConverter
    fun fromList(value: List<String>): String {
        return value.joinToString(",")
    }
}