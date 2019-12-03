package com.fastival.unittestex.ui.note

import androidx.lifecycle.*
import com.fastival.unittestex.extension.removeWhiteSpace
import com.fastival.unittestex.models.Note
import com.fastival.unittestex.repository.NoteRepository
import com.fastival.unittestex.repository.NoteRepository.Companion.NOTE_TITLE_NULL
import com.fastival.unittestex.ui.Resource
import com.fastival.unittestex.util.DateUtil
import org.reactivestreams.Subscription
import java.lang.NullPointerException
import javax.inject.Inject
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class NoteViewModel
@Inject constructor(private val noteRepository: NoteRepository): ViewModel() {

    enum class ViewState{
        VIEW, EDIT
    }

    private var insertSubscription: Subscription? = null
    private var updateSubscription: Subscription? = null

    private var isNewNote = true

    private val _note = MutableLiveData<Note>()
    private val _viewState = MutableLiveData<ViewState>()

    val note: LiveData<Note>
        get() = _note

    val viewState: LiveData<ViewState>
        get() = _viewState

    val insertNote: LiveData<Resource<Int>> = Transformations
        .switchMap(_note){note ->
            LiveDataReactiveStreams.fromPublisher(
                noteRepository.insertNote(note)
                    .doOnSubscribe { insertSubscription = it }
            )
        }

    val updateNote: LiveData<Resource<Int>> = Transformations
        .switchMap(_note){note ->
            LiveDataReactiveStreams.fromPublisher(
                noteRepository.updateNote(note)
                    .doOnSubscribe { updateSubscription = it }
            )
        }

    fun setIsNewNote(isNew: Boolean) {
        this.isNewNote = isNew
    }



    /*fun insertNote(): LiveData<Resource<Int>> {
        return LiveDataReactiveStreams.fromPublisher(
            noteRepository.insertNote(_note.value!!)
        )
    }*/

    fun setNote(note: Note) {
        if (note.title == "") {
            throw Exception(NOTE_TITLE_NULL)
        }
        this._note.value = note
    }

    fun setViewState(viewState: ViewState) {
        this._viewState.value = viewState
    }

    fun updateNote(title: String, content: String) {

        if (title == "") throw NullPointerException("Title can't be null")
        val temp: String = content.removeWhiteSpace()

        if (temp.isNotEmpty()) {

            val updateNote: Note = Note(_note.value!!).apply {
                this.title = title
                this.content = content
                this.timestamp = DateUtil.getCurrentTimeStamp()
            }

            _note.value = updateNote

        }

    }

    fun saveNote(): LiveData<Resource<Int>> {

        if (!shouldAllowSave()) throw Exception(NO_CONTENT_ERROR)

        cancelPendingTransactions()

        return object: NoteInsertUpdateHelper<Int>(){
            override fun getAction(): LiveData<Resource<Int>> {
                return if(isNewNote) insertNote else updateNote
            }

            override fun setNoteId(noteId: Int) {
                isNewNote = false
                val currentNote = _note.value
                currentNote?.id = noteId
                _note.value = currentNote            }

            override fun defineAction(): String {
                return if (isNewNote) ACTION_INSERT
                else ACTION_UPDATE
            }

            override fun onTransactionComplete() {
                insertSubscription = null
                updateSubscription = null
            }
        }.getAsLiveData()
    }

    private fun cancelPendingTransactions(){

        if (insertSubscription != null) cancelInsertTransaction()
        if (updateSubscription != null) cancelUpdateTransaction()

    }

    private fun cancelInsertTransaction(){
        insertSubscription?.cancel()
        insertSubscription = null
    }

    private fun cancelUpdateTransaction(){
        updateSubscription?.cancel()
        updateSubscription = null
    }

    fun shouldNavigateBack() = _viewState.value == ViewState.VIEW

    private fun shouldAllowSave() = note.value?.content!!.removeWhiteSpace().isNotEmpty()

    companion object{
        const val NO_CONTENT_ERROR = "Can't save note with no content"
    }
}