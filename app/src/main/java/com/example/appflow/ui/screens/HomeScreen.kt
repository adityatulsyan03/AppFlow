package com.example.appflow.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.appflow.model.Note
import com.example.appflow.ui.navigator.Routes
import com.example.appflow.ui.state.UiState
import com.example.appflow.ui.viewmodel.NoteViewModel
import com.example.appflow.utils.safeNavigateOnce
import com.example.appflow.utils.safePopBackStack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: NoteViewModel) {
    val state = viewModel.notes.collectAsState()

    BackHandler {
        navController.safePopBackStack()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("SmartNotes") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.safeNavigateOnce(Routes.ADD_NOTE)
            }) {
                Text("+")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (val uiState = state.value) {
                is UiState.Loading -> CircularProgressIndicator()
                is UiState.Success -> NoteList(
                    notes = uiState.data,
                    onNoteClick = {
                        viewModel.selectNote(it)
                        navController.safeNavigateOnce(Routes.NOTE_DETAIL)
                    }
                )
                is UiState.Error -> Text("Error: ${uiState.message}")
            }
        }
    }
}

@Composable
fun NoteList(notes: List<Note>, onNoteClick: (Note) -> Unit) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(notes) { note ->
            NoteItem(note = note, onClick = { onNoteClick(note) })
            Divider()
        }
    }
}

@Composable
fun NoteItem(note: Note, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Text(text = note.title, style = MaterialTheme.typography.titleMedium)
        Text(text = note.content, style = MaterialTheme.typography.bodyMedium)
    }
}