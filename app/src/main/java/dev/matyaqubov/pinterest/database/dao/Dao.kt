package dev.matyaqubov.pinterest.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.matyaqubov.pinterest.database.entitiy.Photos

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePhoto(photo:Photos)

    @Query("SELECT * FROM photos")
    fun getAllPhotos():List<Photos>

}