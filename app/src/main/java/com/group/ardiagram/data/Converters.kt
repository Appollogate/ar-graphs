package com.group.ardiagram.data

import android.util.Log
import androidx.room.TypeConverter
import com.google.ar.sceneform.math.Vector3
import java.lang.Exception

class Converters {

    @TypeConverter
    fun fromVectorList(value: List<Vector3>): String {
        return value.joinToString(",")
    }

    @TypeConverter
    fun fromVectorString(value: String): List<Vector3> {
        val list = value.split("],[")

        if (list.isEmpty()) return listOf()

        return list.map {
            val xyz = it.split(",")
            try {
                val x: Float = xyz[0].removePrefix("[x=").toFloatOrNull() ?: 0f
                val y: Float = xyz[1].removePrefix(" y=").toFloatOrNull() ?: 0f
                val z: Float = xyz[2].removePrefix(" z=").removePrefix("]").toFloatOrNull() ?: 0f
                Vector3(x, y, z)
            } catch (e: Exception) {
                Log.e("Converter Error", "$e")
                return listOf()
            }
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