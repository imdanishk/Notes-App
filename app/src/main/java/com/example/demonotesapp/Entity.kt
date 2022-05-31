package com.example.demonotesapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note")
data class Entity(
    @PrimaryKey
    var id:String,
    var title:String,
    var description: String,
    var spinner: String
)
