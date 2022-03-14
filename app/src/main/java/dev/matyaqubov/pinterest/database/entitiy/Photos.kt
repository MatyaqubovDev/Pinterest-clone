package dev.matyaqubov.pinterest.database.entitiy

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.matyaqubov.pinterest.service.model.Links
import dev.matyaqubov.pinterest.service.model.Urls
import dev.matyaqubov.pinterest.service.model.User

@Entity(tableName = "photos")
data class Photos(
    @PrimaryKey
    val id: String,
    val color: String,
    val description: String,
    val altDescription: String,
    val url:String,
    val word:String,
)
