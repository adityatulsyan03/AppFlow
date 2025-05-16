package com.example.appflow

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.appflow.data.model.NoteDao
import com.example.appflow.data.model.NoteEntity

@Database(entities = [NoteEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}