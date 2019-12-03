package com.fastival.unittestex.repository

import androidx.lifecycle.MutableLiveData
import com.fastival.unittestex.InstantExecutorExtension
import com.fastival.unittestex.LiveDataTestUtil
import com.fastival.unittestex.TestUtil
import com.fastival.unittestex.models.Note
import com.fastival.unittestex.persistence.NoteDao
import com.fastival.unittestex.repository.NoteRepository.Companion.DELETE_FAILURE
import com.fastival.unittestex.repository.NoteRepository.Companion.DELETE_SUCCESS
import com.fastival.unittestex.repository.NoteRepository.Companion.INSERT_FAILURE
import com.fastival.unittestex.repository.NoteRepository.Companion.INSERT_SUCCESS
import com.fastival.unittestex.repository.NoteRepository.Companion.INVALID_NOTE_ID
import com.fastival.unittestex.repository.NoteRepository.Companion.UPDATE_FAILURE
import com.fastival.unittestex.repository.NoteRepository.Companion.UPDATE_SUCCESS
import com.fastival.unittestex.ui.Resource
import io.reactivex.Single
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.function.Executable
import org.mockito.Mockito
import org.mockito.Mockito.*
@ExtendWith(InstantExecutorExtension::class)
class NoteRepositoryTest {

    companion object {
        val note: Note = Note(TestUtil.TEST_NOTE_1)
    }

    private fun <T> any(): T {
        Mockito.any<T>()
        return null as T
    }

    // system under test
    lateinit var noteRepository: NoteRepository
    lateinit var noteDao: NoteDao

    @BeforeEach
    fun initEach(){
        println("@BeforeEach_fun_initEach()")
        noteDao = mock(NoteDao::class.java)
        noteRepository = NoteRepository(noteDao)
    }

    @Test
    fun insertNote_returnRow(){

        val insertedRow = 2L
        var returnedData = Single.just(insertedRow)
        `when`((noteDao).insertNote(any())).thenReturn(returnedData)

        val returnValue = noteRepository.insertNote(note).blockingSingle()

        verify(noteDao).insertNote(any())
        verifyNoMoreInteractions(noteDao)

        println("Returned value: ${returnValue.data}")
        println("Returned msg: ${returnValue.msg}")
        assertEquals(Resource.Success(2, INSERT_SUCCESS), returnValue)
    }

    @Test
    fun insertNote_returnFailure() {

        val failedInsert = -1L
        val returedData = Single.just(failedInsert)
        `when`(noteDao.insertNote(any())).thenReturn(returedData)

        val returnedValue = noteRepository.insertNote(note).blockingFirst()

        verify(noteDao).insertNote(any())
        verifyNoMoreInteractions(noteDao)

        println("Returned value: ${returnedValue.data}")
        println("Returned msg: ${returnedValue.msg}")
        assertEquals(Resource.Error(null, INSERT_FAILURE), returnedValue)
    }

    @Test
    fun updateNote_returnNumRowUpdated() {

        `when`(noteDao.updateNote(any())).thenReturn(Single.just(1))

        val updatedValue = noteRepository.updateNote(note).blockingSingle()

        verify(noteDao).updateNote(any())
        verifyNoMoreInteractions(noteDao)

        assertEquals(Resource.Success(1, UPDATE_SUCCESS), updatedValue)
    }

    @Test
    fun updateNote_returnFailure() {

        `when`(noteDao.updateNote(any())).thenReturn(Single.just(-1))

        val returnValue = noteRepository.updateNote(note).blockingFirst()

        verify(noteDao).updateNote(any())
        verifyNoMoreInteractions(noteDao)
        assertEquals(returnValue, Resource.Error(null, UPDATE_FAILURE))
    }

    @Test
    fun deleteNote_nullId_throwException(){
        val exception = assertThrows(Exception::class.java, Executable {
            note.id = -1
            noteRepository.deleteNote(note)
        })

        assertEquals(INVALID_NOTE_ID, exception.message)
    }

    @Test
    fun deleteNote_returnRow () {

        val deleteRow = 1
        `when`(noteDao.deleteNote(any())).thenReturn(Single.just(deleteRow))

        val retDeleteRow = LiveDataTestUtil.getValue(noteRepository.deleteNote(note))

        verify(noteDao).deleteNote(any())
        assertEquals(Resource.Success(deleteRow, DELETE_SUCCESS), retDeleteRow)

    }

    @Test
    fun deleteNote_returnFailure() {

        val deletedRow = -1
        `when`(noteDao.deleteNote(any())).thenReturn(Single.just(deletedRow))

        val returnedValue = LiveDataTestUtil.getValue(noteRepository.deleteNote(note))

        assertEquals(Resource.Error(null, DELETE_FAILURE), returnedValue)

    }

    @Test
    fun getNotes_returnListWithNotes(){

        val notes = TestUtil.TEST_NOTES_LIST
        val returnedData = MutableLiveData<List<Note>>()
        returnedData.value = notes
        `when`(noteDao.getNotes()).thenReturn(returnedData)

        val getNotes = LiveDataTestUtil.getValue(noteRepository.getNotes())

        assertEquals(notes, getNotes)
    }

    @Test
    fun getNotes_returnEmptyList(){
        val notes = listOf<Note>()
        val returnedData = MutableLiveData<List<Note>>()
        returnedData.value = notes
        `when`(noteDao.getNotes()).thenReturn(returnedData)

        val observedData = LiveDataTestUtil.getValue(noteRepository.getNotes())

        assertEquals(notes, observedData)
    }

}