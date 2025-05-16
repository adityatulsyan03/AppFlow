package com.example.appflow.data

import com.example.appflow.data.model.Note
import com.example.appflow.data.model.NoteDao
import com.example.appflow.data.model.NoteEntity

fun Note.toEntity() = NoteEntity(id, title, content)
fun NoteEntity.toNote() = Note(id, title, content)

suspend fun NoteDao.clearAndInsert(notes: List<NoteEntity>) {
    notes.forEach { insertNote(it) }
}
