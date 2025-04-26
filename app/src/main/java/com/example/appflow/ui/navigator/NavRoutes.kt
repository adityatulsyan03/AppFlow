package com.example.appflow.ui.navigator

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object AddNote : Screen("add")
    data object NoteDetail : Screen("detail/{noteId}") {
        fun createRoute(noteId: String) = "detail/$noteId"
    }
    data object EditNote : Screen("edit/{noteId}") {
        fun createRoute(noteId: String) = "edit/$noteId"
    }
    data object Register : Screen("register")
    data object Login : Screen("login")
}