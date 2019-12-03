package com.fastival.unittestex.di

import android.app.Application
import androidx.room.Room
import com.fastival.unittestex.persistence.NoteDatabase
import com.fastival.unittestex.persistence.NoteDatabase.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideNoteDatabase(application: Application): NoteDatabase {
        return Room.databaseBuilder(
            application,
            NoteDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideNoteDao(noteDatabase: NoteDatabase) = noteDatabase.getNoteDao()
}