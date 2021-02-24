package com.example.restapiassignment.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "artists")
data class ArtistEntity (
    @PrimaryKey val collectionId: Int,
    @ColumnInfo(name = "artistname") val artistname: String,
    @ColumnInfo(name = "primaryGenreName") val primaryGenreName: String,
    @ColumnInfo(name = "collectionPrice") val collectionPrice: String,
    @ColumnInfo(name = "releaseDate") val releaseDate: String,
    @ColumnInfo(name = "collectionurl") val collectionurl: String
)