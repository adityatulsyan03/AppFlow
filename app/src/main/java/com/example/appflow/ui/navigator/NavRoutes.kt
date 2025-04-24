package com.example.appflow.ui.navigator

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object AddNote : Screen("add")
    data object NoteDetail : Screen("detail/{noteId}") {
        fun createRoute(noteId: String) = "detail/$noteId"
    }
}