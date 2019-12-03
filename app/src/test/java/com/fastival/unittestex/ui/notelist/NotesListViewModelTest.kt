package com.fastival.unittestex.ui.notelist

import androidx.lifecycle.MutableLiveData
import com.fastival.unittestex.InstantExecutorExtension
import com.fastival.unittestex.LiveDataTestUtil
import com.fastival.unittestex.TestUtil
import com.fastival.unittestex.models.Note
import com.fastival.unittestex.repository.NoteRepository
import com.fastival.unittestex.repository.NoteRepository.Companion.DELETE_FAILURE
import com.fastival.unittestex.repository.NoteRepository.Companion.DELETE_SUCCESS
import com.fastival.unittestex.ui.Resource
import com.fastival.unittestex.ui.noteslist.NotesListViewModel
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@ExtendWith(InstantExecutorExtension::class)
class NotesListViewModelTest {

    private fun <T> any(): T {
        Mockito.any<T>()
        return null as T
    }

    companion object{
        val notes = TestUtil.TEST_NOTES_LIST
    }

    @Mock
    lateinit var noteRepository: NoteRepository
    private val notesListVM by lazy { NotesListViewModel(noteRepository) }

    @BeforeEach
    fun init(){
        MockitoAnnotations.initMocks(this)
        notesListVM
    }

    @Test
    fun retrieveNotes_returnNotesList(){

        val returnedValue = MutableLiveData<List<Note>>()
        returnedValue.value = notes
        `when`(noteRepository.getNotes()).thenReturn(returnedValue)

        notesListVM.getNotes()
        val observedData = LiveDataTestUtil.getValue(notesListVM.notes)

        assertEquals(notes, observedData)
    }

    @Test
    fun retrieveNotes_returnEmptyNoteList() {

        val emptyNotes = listOf<Note>()
        val emtLiveData = MutableLiveData<List<Note>>()
        emtLiveData.value = emptyNotes
        `when`(noteRepository.getNotes()).thenReturn(emtLiveData)

        notesListVM.getNotes()
        val observeData = LiveDataTestUtil.getValue(notesListVM.notes)

        assertEquals(emptyNotes, observeData)
    }

    @Test
    fun deleteNote_observeResourceSuccess() {

        val note = TestUtil.TEST_NOTE_1
        val deleteRow = 1
        val deleteValue = MutableLiveData<Resource<Int>>()
        deleteValue.value = Resource.Success(deleteRow, DELETE_SUCCESS)
        `when`(noteRepository.deleteNote(any())).thenReturn(deleteValue)

        val observeData = LiveDataTestUtil.getValue(notesListVM.deleteNote(note))

        verify(noteRepository).deleteNote(any())
        assertEquals(Resource.Success(deleteRow, DELETE_SUCCESS), observeData)

    }

    @Test
    fun deleteNote_observeResourceError(){

        val note = Note(TestUtil.TEST_NOTE_1)
        val errResource = Resource.Error(null, DELETE_FAILURE)
        val errLiveData = MutableLiveData<Resource<Int>>()
        errLiveData.value = errResource

        `when`(noteRepository.deleteNote(any())).thenReturn(errLiveData)

        val observeData = LiveDataTestUtil.getValue(notesListVM.deleteNote(note))

        assertEquals(errResource, observeData)
    }
}