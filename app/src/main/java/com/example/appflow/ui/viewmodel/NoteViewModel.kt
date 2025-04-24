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
        val dummyNotes = listOf(
            Note("1", "Meeting Notes", "Project sync at 5 PM"),
            Note("2", "Grocery List", "Milk, Eggs, Coffee")
        )
        _notes.value = UiState.Success(dummyNotes)
    }

    fun selectNote(note: Note) {
        _selectedNote.value = note
    }

    fun addNote(note: Note) {
        val current = (_notes.value as? UiState.Success)?.data ?: emptyList()
        _notes.value = UiState.Success(current + note)
    }
}