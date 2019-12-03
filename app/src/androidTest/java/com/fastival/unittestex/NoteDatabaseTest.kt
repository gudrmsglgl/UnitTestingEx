package com.fastival.unittestex

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.fastival.unittestex.persistence.NoteDao
import com.fastival.unittestex.persistence.NoteDatabase
import org.junit.After
import org.junit.Before

abstract class NoteDatabaseTest {

    private lateinit var noteDatabase: NoteDatabase

    fun getNoteDao(): NoteDao {
        return noteDatabase.getNoteDao()
    }

    @Before
    fun init() {
        noteDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NoteDatabase::class.java
        ).build()
    }

    @After
    fun finish() {
        noteDatabase.close()
    }
}