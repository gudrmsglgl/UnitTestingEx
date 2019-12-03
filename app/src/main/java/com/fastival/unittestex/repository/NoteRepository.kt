package com.fastival.unittestex.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import com.fastival.unittestex.models.Note
import com.fastival.unittestex.persistence.NoteDao
import com.fastival.unittestex.ui.Resource
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository @Inject constructor(private val noteDao: NoteDao) {

    companion object{
        const val NOTE_TITLE_NULL = "Note title cannot be null"
        const val INVALID_NOTE_ID = "Invalid id. Can't delete note"
        const val DELETE_SUCCESS = "Delete success"
        const val DELETE_FAILURE = "Delete failure"
        const val UPDATE_SUCCESS = "Update success"
        const val UPDATE_FAILURE = "Update failure"
        const val INSERT_SUCCESS = "Insert success"
        const val INSERT_FAILURE = "Insert failure"
    }

    private val timeDelay = 0
    private val timeUnit:TimeUnit = TimeUnit.SECONDS

    fun insertNote(note: Note): Flowable<Resource<Int>> {
        return noteDao.insertNote(note)
            .delaySubscription(timeDelay.toLong(), timeUnit)
            .map { it.toInt() }
            .onErrorReturn { -1 }
            .map {
                if (it > 0) {
                    Resource.Success(it, INSERT_SUCCESS)
                } else {
                    Resource.Error(null, INSERT_FAILURE)
                } }
            .subscribeOn(Schedulers.io())
            .toFlowable()
    }

    fun updateNote(note: Note): Flowable<Resource<Int>> {
        return noteDao.updateNote(note)
                    .delaySubscription(timeDelay.toLong(), timeUnit)
                    .onErrorReturn { -1 }
                    .map { if(it>0) Resource.Success(it, UPDATE_SUCCESS) else Resource.Error(null, UPDATE_FAILURE) }
                    .subscribeOn(Schedulers.io())
                    .toFlowable()
    }

    fun deleteNote(note: Note): LiveData<Resource<Int>> {
        checkId(note)
        return LiveDataReactiveStreams.fromPublisher(
            noteDao.deleteNote(note)
                .onErrorReturn { -1 }
                .map { if (it > 0) Resource.Success(it, DELETE_SUCCESS) else Resource.Error(null, DELETE_FAILURE)}
                .subscribeOn(Schedulers.io())
                .toFlowable()
        )
    }

    fun getNotes(): LiveData<List<Note>> = noteDao.getNotes()

    private fun checkId(note: Note) {
        if (note.id != null) {
            if (note.id!! < 0) {
                throw Exception(INVALID_NOTE_ID)
            }
        }

    }

}