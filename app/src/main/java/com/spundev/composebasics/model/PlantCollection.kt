package com.spundev.composebasics.model

data class PlantCollection(
    val name: String,
    // @IdRes val asset: Int,
    val imageUrl: String,
    val plants: List<Plant>
)