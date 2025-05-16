package com.example.appflow.data.repo

import com.example.appflow.data.clearAndInsert
import com.example.appflow.data.model.Note
import com.example.appflow.data.model.NoteDao
import com.example.appflow.data.model.NoteEntity
import com.example.appflow.data.toEntity
import com.example.appflow.data.toNote
import com.example.appflow.ui.state.UiState
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val dao: NoteDao
) {

    fun getNotes(email: String): Flow<UiState<List<Note>>> = flow {
        emit(UiState.Loading)

        // Emit local notes first
        val localNotes = dao.getAllNotes(email).map { it.map { it.toNote() } }
        emitAll(localNotes.map { UiState.Success(it) })

        // Start listening to Firestore
        firestore.collection("users").document(email).collection("notes")
            .addSnapshotListener { snapshot, error ->
                if (error == null && snapshot != null) {
                    val notes = snapshot.map {
                        Note(
                            id = it.id,
                            title = it.getString("title") ?: "",
                            content = it.getString("content") ?: ""
                        )
                    }
                    // Sync to local Room DB
                    CoroutineScope(Dispatchers.IO).launch {
                        dao.clearAndInsert(notes.map { it.toEntity() })
                    }
                }
            }
    }

    suspend fun addNote(note: Note, email: String): UiState<Unit> {
        return try {
            val noteEntity = NoteEntity(
                id = note.id,
                title = note.title,
                content = note.content,
                userEmail = email
            )
            dao.insertNote(noteEntity) // save locally
            firestore.collection("users")
                .document(email)
                .collection("notes")
                .document(note.id)
                .set(note)
                .await()
            UiState.Success(Unit)
        } catch (e: Exception) {
            UiState.Error(e.localizedMessage ?: "Failed to add note")
        }
    }

    suspend fun deleteNote(id: String, email: String): UiState<Unit> {
        return try {
            dao.deleteNoteById(id)
            firestore.collection("users")
                .document(email)
                .collection("notes")
                .document(id)
                .delete()
                .await()
            UiState.Success(Unit)
        } catch (e: Exception) {
            UiState.Error(e.localizedMessage ?: "Failed to delete note")
        }
    }

    suspend fun updateNote(note: Note, email: String): UiState<Note> {
        return try {
            val noteEntity = NoteEntity(
                id = note.id,
                title = note.title,
                content = note.content,
                userEmail = email
            )
            dao.insertNote(noteEntity)
            firestore.collection("users")
                .document(email)
                .collection("notes")
                .document(note.id)
                .set(note)
                .await()
            UiState.Success(note)
        } catch (e: Exception) {
            UiState.Error(e.localizedMessage ?: "Failed to update note")
        }
    }
}