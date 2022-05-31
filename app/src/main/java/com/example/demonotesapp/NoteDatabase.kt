package com.example.demonotesapp

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Entity::class], version=7)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun dao(): NoteDao
}