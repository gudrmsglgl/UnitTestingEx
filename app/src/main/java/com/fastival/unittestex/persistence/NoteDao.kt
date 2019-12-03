package com.fastival.unittestex.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import com.fastival.unittestex.models.Note
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface NoteDao {

    @Insert
    fun insertNote(note: Note): Single<Long>
    //fun insertNote(note: Note): Completable

    @Query("SELECT * FROM notes")
    fun getNotes() : LiveData<List<Note>>

    @Delete
    fun deleteNote(note: Note) : Single<Int>

    @Update
    fun updateNote(note: Note) : Single<Int>
}