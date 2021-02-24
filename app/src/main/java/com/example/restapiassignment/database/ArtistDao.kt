package com.example.restapiassignment.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ArtistDao {
    @Insert
    fun insertArtist(artistEntity: ArtistEntity)

    @Delete
    fun deleteArtist(artistEntity: ArtistEntity)

    @Query("SELECT * FROM artists")
    fun getAllArtists() :List<ArtistEntity>

    @Query("SELECT * FROM artists WHERE collectionId=:artistId")
    fun getArtistById(artistId : String) :ArtistEntity
}