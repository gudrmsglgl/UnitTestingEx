package com.fastival.unittestex

import android.database.sqlite.SQLiteConstraintException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fastival.unittestex.models.Note
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NoteDaoTest: NoteDatabaseTest() {

    companion object{
        const val TEST_TITLE = "This is a test title"
        const val TEST_CONTENT = "This is some test content"
        const val TEST_TIMESTAMP = "08-2018"
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()
    /*
    *   Insert, Read, Delete
    * */
    @Test
    fun insertReadDelete(){

        val note = Note(TestUtil.TEST_NOTE_1)

        // insert
        getNoteDao().insertNote(note).blockingGet() // wait until inserted

        // read
        var loaded: List<Note> = LiveDataTestUtil.getValue(getNoteDao().getNotes())

        assertNotNull(loaded)

        assertEquals(note.content, loaded[0].content)
        assertEquals(note.timestamp, loaded[0].timestamp)
        assertEquals(note.title, loaded[0].title)

        note.id = loaded[0].id
        assertEquals(note, loaded[0])

        // delete
        getNoteDao().deleteNote(note).blockingGet()

        // confirm the database is empty
        loaded = LiveDataTestUtil.getValue(getNoteDao().getNotes())
        assertEquals(0, loaded.size)
    }

    /*
    *   Insert, Read, Update, Read, Delete
    * */
    @Test
    fun insertReadUpdateReadDelete() {

        val note = Note(TestUtil.TEST_NOTE_1)

        // insert
        getNoteDao().insertNote(note).blockingGet()

        // read
        var loaded = LiveDataTestUtil.getValue(getNoteDao().getNotes())

        assertNotNull(loaded)

        assertEquals(note.title, loaded[0].title)
        assertEquals(note.timestamp, loaded[0].timestamp)
        assertEquals(note.content, loaded[0].content)

        note.id = loaded[0].id
        assertEquals(note, loaded[0])

        // update
        note.title = TEST_TITLE
        note.content = TEST_CONTENT
        note.timestamp = TEST_TIMESTAMP
        getNoteDao().updateNote(note).blockingGet()

        //read
        loaded = LiveDataTestUtil.getValue(getNoteDao().getNotes())

        assertEquals(TEST_TITLE, loaded[0].title)
        assertEquals(TEST_CONTENT, loaded[0].content)
        assertEquals(TEST_TIMESTAMP, loaded[0].timestamp)

        note.id = loaded[0].id
        assertEquals(note, loaded[0])

        // delete
        getNoteDao().deleteNote(note).blockingGet()

        // confirm the database is empty
        loaded = LiveDataTestUtil.getValue(getNoteDao().getNotes())
        assertEquals(0, loaded.size)

    }
    
}