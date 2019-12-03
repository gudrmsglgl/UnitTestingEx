package com.fastival.unittestex.ui.note

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.fastival.unittestex.ui.Resource

abstract class NoteInsertUpdateHelper<T> {

    companion object{
        const val ACTION_INSERT = "ACTION_INSERT"
        const val ACTION_UPDATE = "ACTION_UPDATE"
        const val GENERIC_ERROR = "Something went wrong"
    }

    private val result: MediatorLiveData<Resource<T>> = MediatorLiveData()

    init {
        result.value = Resource.Loading(null)
        try{
            val source = this.getAction()
            result.addSource(source, Observer {
                result.removeSource(source)
                result.value = it
                setNewNoteIdIfIsNewNote(it)
                onTransactionComplete()
            })
        } catch (e: Exception){
            e.printStackTrace()
            result.value = Resource.Error<T>(null, GENERIC_ERROR)
        }
    }

    private fun setNewNoteIdIfIsNewNote(resource: Resource<T>){
        if (resource.data != null) {
            if (resource.data is Int) {
                val i = resource.data as Int
                if (defineAction() == ACTION_INSERT) {
                    if (i>=0) setNoteId(i)
                }
            } else {
                Log.d("Main", "resource.data is not Int")
            }
        } else {
            Log.d("Main", "resource.data null")
        }
    }


    abstract fun getAction(): LiveData<Resource<T>>

    abstract fun setNoteId(noteId: Int)

    abstract fun defineAction(): String

    abstract fun onTransactionComplete()

    fun getAsLiveData(): LiveData<Resource<T>> = result
}