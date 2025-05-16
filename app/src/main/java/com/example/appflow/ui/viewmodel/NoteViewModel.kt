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

    fun observeNotes(email: String) {
        viewModelScope.launch {
            noteRepository.getNotesRealtime(email).collect {
                _notes.value = it
            }
        }
    }

    fun addNote(note: Note, email: String) {
        viewModelScope.launch {
            val result = noteRepository.addNote(note, email)
            if (result is UiState.Success) observeNotes(email)
        }
    }

    fun deleteNote(id: String, email: String) {
        viewModelScope.launch {
            val result = noteRepository.deleteNote(id, email)
            if (result is UiState.Success) observeNotes(email)
        }
    }

    fun updateNote(note: Note, email: String) {
        viewModelScope.launch {
            when (val result = noteRepository.updateNote(note, email)) {
                is UiState.Success -> {
                    _selectedNote.value = result.data
                    observeNotes(email)
                    //If u want to fetch data from the observeNots only
                    // First fetch updated notes list
                    // Wait briefly to let Flow emit (dirty fix)
//                    kotlinx.coroutines.delay(300)
                    // Get latest version of the updated note
//                    val updated = _notes.value.let {
//                        if (it is UiState.Success) it.data.find { it.id == note.id } else null
//                    }
//                    _selectedNote.value = updated
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