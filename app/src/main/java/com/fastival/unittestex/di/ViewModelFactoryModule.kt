package com.fastival.unittestex.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fastival.unittestex.ui.note.NoteViewModel
import com.fastival.unittestex.ui.noteslist.NotesListViewModel
import com.fastival.unittestex.viewmodels.ViewModelProviderFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(provideFactory: ViewModelProviderFactory) : ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(NoteViewModel::class)
    abstract fun bindNoteViewModel(noteViewModel: NoteViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NotesListViewModel::class)
    abstract fun bindNotesListViewModel(noteViewModel: NotesListViewModel): ViewModel
}