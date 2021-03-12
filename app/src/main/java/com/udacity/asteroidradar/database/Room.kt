package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidRadarDao {
    @Query("select * from picture_of_day limit 1")
    fun getFirstPictureOfDay(): LiveData<DatabasePictureOfDay>

    @Query("select exists(select * from picture_of_day where url = :url)")
    suspend fun pictureOfDayExists(url: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPictureOfDay(picture: DatabasePictureOfDay)

    @Query("DELETE FROM picture_of_day")
    suspend fun deletePicturesOfDay()

    @Query("select * from asteroid WHERE close_approach_date >= :timestampFrom AND close_approach_date <= :timestampTo ORDER BY close_approach_date ASC")
    fun getAsteroids(timestampFrom: Long, timestampTo: Long): LiveData<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAsteroids(vararg asteroids: DatabaseAsteroid)

    @Query("DELETE FROM asteroid WHERE close_approach_date < :timestamp")
    suspend fun deleteOlderAsteroidsThan(timestamp: Long)
}

@Database(
    entities = [DatabasePictureOfDay::class, DatabaseAsteroid::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(CalendarConverter::class)
abstract class AsteroidRadarDatabase : RoomDatabase() {
    abstract val asteroidRadarDao: AsteroidRadarDao
}

@Volatile
private lateinit var INSTANCE: AsteroidRadarDatabase

fun getDatabase(context: Context): AsteroidRadarDatabase {
    synchronized(AsteroidRadarDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AsteroidRadarDatabase::class.java,
                "asteroid_radar_database"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}