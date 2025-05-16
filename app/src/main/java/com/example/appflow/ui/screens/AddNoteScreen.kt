package com.example.appflow.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.appflow.data.model.Note
import com.example.appflow.ui.viewmodel.LoginViewModel
import com.example.appflow.ui.viewmodel.NoteViewModel
import com.example.appflow.utils.safePopBackStack
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(navController: NavController, viewModel: NoteViewModel, loginViewModel: LoginViewModel) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val email by loginViewModel.currentUser.collectAsState()
    var isClicked by remember { mutableStateOf(false) }

    BackHandler {
        navController.safePopBackStack()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Add Note") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Content") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (!isClicked && title.isNotEmpty() && content.isNotEmpty()) {
                        isClicked = true
                        viewModel.addNote(
                            email = email ?: "",
                            note = Note(
                                id = UUID.randomUUID().toString(),
                                title = title,
                                content = content
                            )
                        )
                        navController.safePopBackStack()
                    }
                },
                enabled = !isClicked,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.Black,
                    disabledContainerColor = MaterialTheme.colorScheme.primary,
                    disabledContentColor = Color.Black
                )
            ) {
                Text("Save Note")
            }
        }
    }
}
