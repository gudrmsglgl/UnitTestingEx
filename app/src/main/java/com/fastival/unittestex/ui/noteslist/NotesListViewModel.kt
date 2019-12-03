package com.fastival.unittestex.ui.noteslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.fastival.unittestex.models.Note
import com.fastival.unittestex.repository.NoteRepository
import com.fastival.unittestex.ui.Resource
import javax.inject.Inject

class NotesListViewModel
@Inject constructor(val noteRepository: NoteRepository) : ViewModel() {

    private val _notes: MediatorLiveData<List<Note>> = MediatorLiveData()
    val notes: LiveData<List<Note>>
        get() = _notes

    fun deleteNote(note: Note): LiveData<Resource<Int>> {
        return noteRepository.deleteNote(note)
    }

    fun getNotes() {
        val source:LiveData<List<Note>> = noteRepository.getNotes()
        _notes.addSource(source, Observer {
            if (it != null) {
                _notes.value = it
            }
            _notes.removeSource(source)
        })
    }

}