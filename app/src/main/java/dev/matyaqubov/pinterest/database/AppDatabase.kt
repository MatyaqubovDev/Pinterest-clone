package dev.matyaqubov.pinterest.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.matyaqubov.pinterest.database.dao.Dao
import dev.matyaqubov.pinterest.database.entitiy.Photos



@Database(entities = [Photos::class], version = 5)
abstract class AppDatabase : RoomDatabase() {
    abstract fun photoDao(): Dao

    companion object{
        private var instance:AppDatabase?=null

        fun getInstance(context: Context):AppDatabase{
            if (instance==null){
                instance= Room.databaseBuilder(context,AppDatabase::class.java,"photos.db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }

}