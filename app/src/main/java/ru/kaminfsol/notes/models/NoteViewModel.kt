package ru.kaminfsol.notes.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.kaminfsol.notes.database.Note
import ru.kaminfsol.notes.database.NoteApplication

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = (application as NoteApplication).database.noteDao()

    val noteList = dao.getAllNotes()
        .stateIn(viewModelScope, SharingStarted.Companion.Lazily, emptyList())

    fun getNoteById(noteId: Int): Flow<Note?> {
        return dao.getNoteId(noteId)
    }

    fun saveNote(title: String, content: String) {
        viewModelScope.launch {
            dao.insertNote(Note(title = title, content = content))
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            dao.updateNote(note)
        }
    }

    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return NoteViewModel(app) as T
        }
    }
}