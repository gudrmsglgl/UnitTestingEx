package com.fastival.unittestex.di

import com.fastival.unittestex.ui.note.NoteActivity
import com.fastival.unittestex.ui.noteslist.NotesListActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeNotesListActivity(): NotesListActivity

    @ContributesAndroidInjector
    abstract fun contributeNoteActivity(): NoteActivity
}