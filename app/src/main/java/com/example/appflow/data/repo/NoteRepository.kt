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
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    // This function listens to Firestore changes in real-time
    fun getNotesRealtime(email: String): Flow<UiState<List<Note>>> = callbackFlow {
        // Listen to real-time changes in the notes collection
        val listener = firestore.collection("users")
            .document(email)
            .collection("notes")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // If error occurs, send it to the flow as an Error state
                    trySend(UiState.Error(error.message ?: "Error loading notes"))
                    return@addSnapshotListener
                }

                // If snapshot is not null, map the documents to a list of Notes
                snapshot?.let {
                    val notes = it.map { doc ->
                        Note(
                            id = doc.id,
                            title = doc.getString("title") ?: "",
                            content = doc.getString("content") ?: ""
                        )
                    }
                    // Send the list of notes to the flow as a Success state
                    trySend(UiState.Success(notes))
                }
            }

        // Make sure to remove the listener when the flow is closed (e.g., when the screen is destroyed)
        awaitClose { listener.remove() }
    }

    // Function to add a note
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

    // Function to delete a note
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

    // Function to update a note
    suspend fun updateNote(note: Note, email: String): UiState<Note> {
        return try {
            firestore.collection("users")
                .document(email)
                .collection("notes")
                .document(note.id).set(note).await()
            // Re-fetch to ensure consistency after update
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