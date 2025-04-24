package com.example.appflow.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.appflow.ui.viewmodel.NoteViewModel
import com.example.appflow.utils.safePopBackStack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(navController: NavHostController, viewModel: NoteViewModel) {
    val note = viewModel.selectedNote.collectAsState().value

    BackHandler {
        navController.safePopBackStack()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Note Details") })
        }
    ) { padding ->
        note?.let {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Text(text = it.title, style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it.content, style = MaterialTheme.typography.bodyLarge)
            }
        } ?: run {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
            ) {
                Text("No note selected")
            }
        }
    }
}
