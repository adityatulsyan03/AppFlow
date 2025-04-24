package com.example.appflow.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appflow.ui.screens.AddNoteScreen
import com.example.appflow.ui.screens.HomeScreen
import com.example.appflow.ui.screens.NoteDetailScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.AddNote.route) {
            AddNoteScreen(navController)
        }
        composable(Screen.NoteDetail.route) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId") ?: "Unknown"
            NoteDetailScreen(noteId, navController)
        }
    }
}