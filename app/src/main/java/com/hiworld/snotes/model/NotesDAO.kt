package com.hiworld.snotes.model

import androidx.room.*

@Dao
interface NotesDAO {

    @Insert
    suspend fun newNotes(notes: Notes)

    @Query("SELECT * FROM notes ORDER BY id DESC")
    suspend fun getAllNotes(): List<Notes>

    @Update
    suspend fun updateNotes(notes: Notes)

    @Delete
    suspend fun deleteNotes(notes: Notes)
}