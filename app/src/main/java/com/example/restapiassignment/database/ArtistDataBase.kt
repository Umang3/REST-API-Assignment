package com.example.restapiassignment.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ArtistEntity::class],version = 1)
abstract class ArtistDataBase : RoomDatabase()  {
    abstract fun artistDao() : ArtistDao

}