package com.example.appflow.ui.viewmodel

import android.util.Log
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appflow.data.model.Note
import com.example.appflow.data.repo.NoteRepository
import com.example.appflow.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    private val _notes = MutableStateFlow<UiState<List<Note>>>(UiState.Loading)
    val notes: StateFlow<UiState<List<Note>>> = _notes

    private val _selectedNote = MutableStateFlow<Note?>(null)
    val selectedNote: StateFlow<Note?> = _selectedNote

    fun loadNotes(email: String) {
        viewModelScope.launch {
            _notes.value = noteRepository.getNotes(email)
        }
    }

    fun addNote(note: Note, email: String) {
        viewModelScope.launch {
            val result = noteRepository.addNote(note, email)
            if (result is UiState.Success) loadNotes(email)
        }
    }

    fun deleteNote(id: String, email: String) {
        viewModelScope.launch {
            val result = noteRepository.deleteNote(id, email)
            if (result is UiState.Success) loadNotes(email)
        }
    }

    fun updateNote(note: Note, email: String) {
        viewModelScope.launch {
            when (val result = noteRepository.updateNote(note, email)) {
                is UiState.Success -> {
                    _selectedNote.value = result.data
                    loadNotes(email)
                }
                is UiState.Error -> {
                    Log.d("Update Note", "Error: ${result.message}")
                }
                else -> Unit
            }
        }
    }

    fun selectNote(note: Note) {
        _selectedNote.value = note
    }
}