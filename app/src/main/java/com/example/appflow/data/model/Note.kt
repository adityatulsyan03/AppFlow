package com.example.appflow.data.model

data class Note(
    val id: String = System.currentTimeMillis().toString(),
    val title: String,
    val content: String
)