package com.example.appflow.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.appflow.ui.navigator.Routes
import com.example.appflow.ui.viewmodel.NoteViewModel
import com.example.appflow.utils.safeNavigateOnce
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
            TopAppBar(
                title = { Text("Note Details") },
                actions = {
                    IconButton(
                        onClick = {
                            navController.safeNavigateOnce(Routes.EDIT_NOTE)
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "")
                    }
                }
            )
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
