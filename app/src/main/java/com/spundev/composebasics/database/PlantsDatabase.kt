package com.spundev.composebasics.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.spundev.composebasics.model.Plant
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(entities = [Plant::class], version = 1, exportSchema = false)
abstract class PlantsDatabase : RoomDatabase() {
    abstract fun plantDao(): PlantDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: PlantsDatabase? = null

        fun getDatabase(context: Context): PlantsDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlantsDatabase::class.java,
                    "plants_database"
                ).addCallback(object : Callback() {
                    // prepopulate the database after onCreate was called
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // FIXME: we should use a db file if we want to prepopulate the database
                        //  instead of doing...this
                        GlobalScope.launch {
                            getDatabase(context).plantDao().insert(initialPlants)
                        }
                    }
                }).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}

val initialPlants = listOf(
    Plant(
        "Monstera",
        "This is a description",
        true,
        "https://images.unsplash.com/photo-1598764557991-b9f211b73b81?w=300"
    ),
    Plant(
        "Aglaonema",
        "This is a description",
        false,
        "https://images.unsplash.com/photo-1620803366004-119b57f54cd6?w=300"
    ),
    Plant(
        "Peace lily",
        "This is a description",
        false,
        "https://images.unsplash.com/photo-1593691509543-c55fb32d8de5?w=300"
    ),
    Plant(
        "Fiddle leaf tree",
        "This is a description",
        true,
        "https://images.unsplash.com/photo-1616173981402-3a526ec60ba2?w=300"
    ),
    Plant(
        "Snake plant",
        "This is a description",
        false,
        "https://images.unsplash.com/photo-1599009944474-5bc0ff20db85?w=300"
    ),
    Plant(
        "Peace lily",
        "This is a description",
        false,
        "https://images.unsplash.com/photo-1620310252507-c65943dbd411?w=300"
    ),
)