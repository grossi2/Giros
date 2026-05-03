package com.example.giros.model

import kotlinx.serialization.Serializable

@Serializable
data class Wheel(
    val id: String,
    val name: String,
    val options: List<WheelOption>
)
