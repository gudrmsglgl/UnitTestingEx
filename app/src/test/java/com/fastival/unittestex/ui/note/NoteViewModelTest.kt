package com.fastival.unittestex.ui.note

import com.fastival.unittestex.InstantExecutorExtension
import com.fastival.unittestex.LiveDataTestUtil
import com.fastival.unittestex.TestUtil
import com.fastival.unittestex.models.Note
import com.fastival.unittestex.repository.NoteRepository
import com.fastival.unittestex.repository.NoteRepository.Companion.INSERT_SUCCESS
import com.fastival.unittestex.repository.NoteRepository.Companion.UPDATE_SUCCESS
import com.fastival.unittestex.ui.Resource
import com.fastival.unittestex.ui.note.NoteViewModel.Companion.NO_CONTENT_ERROR
import io.reactivex.Flowable
import io.reactivex.internal.operators.single.SingleToFlowable
import org.junit.Rule
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.function.Executable
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit

@ExtendWith(InstantExecutorExtension::class)
class NoteViewModelTest {

    private fun <T> any(): T {
        Mockito.any<T>()
        return null as T
    }

    private lateinit var noteViewModel: NoteViewModel

    @Mock
    private lateinit var noteRespository: NoteRepository



    @BeforeEach
    fun init(){
        MockitoAnnotations.initMocks(this)
        noteViewModel = NoteViewModel(noteRespository)
    }

    @Test
    fun observeEmptyNoteWhenNoteSet() {

        val note = LiveDataTestUtil.getValue(noteViewModel.note)

        assertNull(note)
    }

    @Test
    fun observeNote_whenSet() {

        val note = Note(TestUtil.TEST_NOTE_1)

        noteViewModel.setNote(note)

        val observeNote = LiveDataTestUtil.getValue(noteViewModel.note)
        assertEquals(note, observeNote)

    }

    @Test
    fun insertNote_returnRow() {

        val note  = Note(TestUtil.TEST_NOTE_1)
        val insertedRow = 1
        val returnedData : Flowable<Resource<Int>> = SingleToFlowable.just(Resource.Success(insertedRow, INSERT_SUCCESS))
        `when`(noteRespository.insertNote(any())).thenReturn(returnedData)

        noteViewModel.setNote(note)
        noteViewModel.setIsNewNote(true)
        val insertedValue = LiveDataTestUtil.getValue(noteViewModel.saveNote())
        //val insertedValue = LiveDataTestUtil.getValue(noteViewModel.insertNote)

        assertEquals(Resource.Success(insertedRow, INSERT_SUCCESS), insertedValue )
    }

    @Test
    fun dontRetrunInsertRowWithOutObserver(){

        val note  = Note(TestUtil.TEST_NOTE_1)

        noteViewModel.setNote(note)

        verify(noteRespository, never()).insertNote(any())
    }

    @Test
    fun updateNote_returnRow(){

        //Arrange
        val note = Note(TestUtil.TEST_NOTE_1)
        val updateRow = 1
        val returnValue: Flowable<Resource<Int>> = SingleToFlowable.just(Resource.Success(updateRow, UPDATE_SUCCESS))
        `when`(noteRespository.updateNote(any())).thenReturn(returnValue)

        // Act
        noteViewModel.setNote(note)
        noteViewModel.setIsNewNote(false)
        val updateResultData = LiveDataTestUtil.getValue(noteViewModel.saveNote())
        //val updateResultData = LiveDataTestUtil.getValue(noteViewModel.updateNote)

        // Assert
        assertEquals(Resource.Success(1, UPDATE_SUCCESS), updateResultData)
    }

    @Test
    fun dontReturnUpdateRowNumWithoutObserver() {
        val note = Note(TestUtil.TEST_NOTE_1)

        noteViewModel.setNote(note)

        verify(noteRespository, never()).updateNote(any())
    }

    @Test
    fun saveNote_shouldAllowSave_returnFalse() {

        val note = Note(TestUtil.TEST_NOTE_1)
        note.content = ""

        noteViewModel.setNote(note)
        noteViewModel.setIsNewNote(true)

        val exception = assertThrows(Exception::class.java, Executable {
            noteViewModel.saveNote()
        })

        assertEquals(NO_CONTENT_ERROR, exception.message)
    }
}