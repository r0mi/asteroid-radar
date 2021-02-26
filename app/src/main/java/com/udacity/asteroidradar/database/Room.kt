package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidRadarDao {
    @Query("select * from databasepictureofday limit 1")
    fun getFirstPictureOfDay(): LiveData<DatabasePictureOfDay>

    @Query("select exists(select * from databasepictureofday where url = :url)")
    suspend fun pictureOfDayExists(url: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPictureOfDay(picture: DatabasePictureOfDay)

    @Query("DELETE FROM databasepictureofday")
    suspend fun deletePicturesOfDay()
}

@Database(entities = [DatabasePictureOfDay::class], version = 1)
abstract class AsteroidRadarDatabase : RoomDatabase() {
    abstract val asteroidRadarDao: AsteroidRadarDao
}

private lateinit var INSTANCE: AsteroidRadarDatabase

fun getDatabase(context: Context): AsteroidRadarDatabase {
    synchronized(AsteroidRadarDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AsteroidRadarDatabase::class.java,
                "asteroid_radar"
            ).build()
        }
    }
    return INSTANCE
}