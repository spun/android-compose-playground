package com.spundev.composebasics.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Plant(
    val name: String = "Plant name",
    val description: String = "Plant description",
    val isFavorite: Boolean = false,
    val imageUrl: String = ""
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
}