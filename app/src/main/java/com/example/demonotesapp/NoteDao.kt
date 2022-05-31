package com.example.demonotesapp

import androidx.room.*

@Dao
interface NoteDao {
    @Insert
    suspend fun insertTask(entity: Entity)

    @Update
    suspend fun updateTask(entity: Entity)

    @Delete
    suspend fun deleteTask(entity: Entity)

    @Query("Delete from note")
    suspend fun deleteAll()

    @Query("Select * from note")
    suspend fun getTasks():List<NoteInfo>

}
