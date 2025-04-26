package com.example.appflow.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.appflow.model.Note
import com.example.appflow.ui.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NoteViewModel : ViewModel() {

    private val _notes = MutableStateFlow<UiState<List<Note>>>(UiState.Loading)
    val notes: StateFlow<UiState<List<Note>>> = _notes

    private val _selectedNote = MutableStateFlow<Note?>(null)
    val selectedNote: StateFlow<Note?> = _selectedNote

    init {
        loadDummyNotes()
    }

    private fun loadDummyNotes() {
        val dummyNotes = emptyList<Note>()
        _notes.value = UiState.Success(dummyNotes)
    }

    fun selectNote(note: Note) {
        _selectedNote.value = note
    }

    fun addNote(note: Note) {
        val current = (_notes.value as? UiState.Success)?.data ?: emptyList()
        _notes.value = UiState.Success(current + note)
    }

    fun deleteNote(id: String){
        val notes = (_notes.value as? UiState.Success)?.data ?: emptyList()
        val filterNotes = notes.filter {
            it.id != id
        }
        _notes.value = UiState.Success(filterNotes)
    }

    fun updateNote(updatedNote: Note) {
        val notes = (_notes.value as? UiState.Success)?.data ?: emptyList()
        val updatedNotes = notes.map { note ->
            if (note.id == updatedNote.id) updatedNote else note
        }
        _notes.value = UiState.Success(updatedNotes)
        _selectedNote.value = updatedNote
    }

}