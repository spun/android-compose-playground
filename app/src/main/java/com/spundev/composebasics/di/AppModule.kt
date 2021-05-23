package com.spundev.composebasics.di

import android.content.Context
import com.spundev.composebasics.database.PlantDao
import com.spundev.composebasics.database.PlantsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AppModule {
    @Provides
    @Singleton
    fun providePlantsDatabase(@ApplicationContext appContext: Context): PlantsDatabase {
        return PlantsDatabase.getDatabase(appContext)
    }

    @Provides
    @Singleton
    fun providePlantDao(db: PlantsDatabase): PlantDao {
        return db.plantDao()
    }
}