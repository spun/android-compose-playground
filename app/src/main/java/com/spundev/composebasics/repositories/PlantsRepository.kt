package com.spundev.composebasics.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import com.spundev.composebasics.database.PlantDao
import com.spundev.composebasics.model.Plant
import com.spundev.composebasics.model.PlantCollection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class PlantsRepository @Inject constructor(
    private val plantDao: PlantDao
) {
    val plants = Pager(PagingConfig(pageSize = 20)) { getAllPlants() }

    private fun getAllPlants(): PagingSource<Int, Plant> = plantDao.getAllPlants()

    fun getPlantDetails(plantId: Int?): Flow<Plant> {
        return if (plantId != null) {
            plantDao.getPlant(plantId.toLong())
        } else {
            emptyFlow()
        }
    }

    suspend fun setFavorite(plantId: Long, isFavorite: Boolean) {
        plantDao.setFavorite(plantId, isFavorite)
    }

    fun getCollections(): List<PlantCollection> {

        // Mock data
        val relatedPlants = listOf(
            Plant(
                "Monstera",
                "",
                true,
                "https://images.unsplash.com/photo-1598764557991-b9f211b73b81?w=300"
            ),
            Plant(
                "Aglaonema",
                "",
                true,
                "https://images.unsplash.com/photo-1620803366004-119b57f54cd6?w=300"
            ),
            Plant(
                "Peace lily",
                "",
                true,
                "https://images.unsplash.com/photo-1593691509543-c55fb32d8de5?w=300"
            ),
        )


        return listOf(
            PlantCollection(
                "Desert chic",
                "https://images.unsplash.com/photo-1547909080-06ea920f49ad?w=300",
                relatedPlants
            ),
            PlantCollection(
                "Tiny garden",
                "https://images.unsplash.com/photo-1460533893735-45cea2212645?w=300",
                relatedPlants
            ),
            PlantCollection(
                "Jungle",
                "https://images.unsplash.com/photo-1552301726-73515b22c2ec?w=300",
                relatedPlants
            ),
        )
    }
}