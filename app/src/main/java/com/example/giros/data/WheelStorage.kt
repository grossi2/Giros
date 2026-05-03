package com.example.giros.data

import android.content.Context
import com.example.giros.model.Wheel
import java.io.FileNotFoundException
import kotlinx.serialization.json.Json

class WheelStorage(
    private val context: Context
) {
    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    fun loadWheels(): List<Wheel> {
        return try {
            val jsonContent = context.openFileInput(FILE_NAME).bufferedReader().use { reader ->
                reader.readText()
            }
            json.decodeFromString<List<Wheel>>(jsonContent)
        } catch (_: FileNotFoundException) {
            emptyList()
        }
    }

    fun saveWheels(wheels: List<Wheel>) {
        val jsonContent = json.encodeToString(wheels)
        context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).bufferedWriter().use { writer ->
            writer.write(jsonContent)
        }
    }

    private companion object {
        const val FILE_NAME = "wheels.json"
    }
}
