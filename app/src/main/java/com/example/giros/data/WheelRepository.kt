package com.example.giros.data

import com.example.giros.model.Wheel

class WheelRepository(
    private val storage: WheelStorage
) {
    fun getWheels(): List<Wheel> = storage.loadWheels()

    fun saveWheels(wheels: List<Wheel>) {
        storage.saveWheels(wheels)
    }
}
