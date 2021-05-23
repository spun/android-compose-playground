package com.spundev.composebasics.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.spundev.composebasics.model.Plant
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantDao {
    @Insert
    suspend fun insert(plants: List<Plant>)

    @Query("SELECT * FROM plant WHERE id = :plantId")
    fun getPlant(plantId: Long): Flow<Plant>

    @Query("SELECT * FROM plant")
    fun getAllPlants(): PagingSource<Int, Plant>

    @Query("UPDATE plant SET isFavorite = :isFavorite WHERE id = :plantId")
    suspend fun setFavorite(plantId: Long, isFavorite: Boolean)
}