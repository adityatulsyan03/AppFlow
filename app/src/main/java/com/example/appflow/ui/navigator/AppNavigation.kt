package com.example.appflow.ui.navigator

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.appflow.ui.screens.*
import com.example.appflow.ui.viewmodel.NoteViewModel
import com.example.appflow.ui.screens.AddNoteScreen
import com.example.appflow.ui.screens.NoteDetailScreen

object Routes {
    const val HOME = "home"
    const val ADD_NOTE = "add_note"
    const val NOTE_DETAIL = "note_detail"
    const val EDIT_NOTE = "edit_note"
}

@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier = Modifier) {
    val viewModel: NoteViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
        modifier = modifier
    ) {
        composable(Routes.HOME) {
            HomeScreen(navController, viewModel)
        }
        composable(Routes.ADD_NOTE) {
            AddNoteScreen(navController, viewModel)
        }
        composable(Routes.NOTE_DETAIL) {
            NoteDetailScreen(navController,viewModel)
        }
        composable(Routes.EDIT_NOTE) {
            EditNoteScreen(navController,viewModel)
        }
    }
}