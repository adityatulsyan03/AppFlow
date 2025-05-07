package com.example.appflow.data.repo

import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.appflow.data.model.Note
import com.example.appflow.ui.state.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun getNotes(email: String): UiState<List<Note>> {
        return try {
            val snapshot = firestore.collection("users")
                .document(email)
                .collection("notes")
                .get().await()
            val notes = snapshot.map { doc ->
                Note(
                    id = doc.id,
                    title = doc.getString("title") ?: "",
                    content = doc.getString("content") ?: ""
                )
            }
            UiState.Success(notes)
        } catch (e: Exception) {
            UiState.Error(e.localizedMessage ?: "Unknown error")
        }
    }

    suspend fun addNote(note: Note, email: String): UiState<Unit> {
        return try {
            firestore.collection("users")
                .document(email)
                .collection("notes")
                .add(note).await()
            UiState.Success(Unit)
        } catch (e: Exception) {
            UiState.Error(e.localizedMessage ?: "Failed to add note")
        }
    }

    suspend fun deleteNote(id: String, email: String): UiState<Unit> {
        return try {
            firestore.collection("users")
                .document(email)
                .collection("notes")
                .document(id).delete().await()
            UiState.Success(Unit)
        } catch (e: Exception) {
            UiState.Error(e.localizedMessage ?: "Failed to delete note")
        }
    }

    suspend fun updateNote(note: Note, email: String): UiState<Note> {
        return try {
            firestore.collection("users")
                .document(email)
                .collection("notes")
                .document(note.id).set(note).await()
            // Re-fetch to ensure consistency
            val updatedDoc = firestore.collection("users")
                .document(email)
                .collection("notes")
                .document(note.id).get().await()
            val updatedNote = Note(
                id = updatedDoc.id,
                title = updatedDoc.getString("title") ?: "",
                content = updatedDoc.getString("content") ?: ""
            )
            UiState.Success(updatedNote)
        } catch (e: Exception) {
            UiState.Error(e.localizedMessage ?: "Failed to update note")
        }
    }
}