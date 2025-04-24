package com.example.appflow.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.appflow.utils.safePopBackStack

@Composable
fun NoteDetailScreen(
    noteId: String, navController: NavController
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Note Detail for ID: $noteId", style = MaterialTheme.typography.headlineMedium
        )
        Spacer(
            modifier = Modifier.height(16.dp)
        )
        Button(onClick = {
            navController.safePopBackStack()
            Log.d("Navigation","Note Details to Home")
        }) {
            Text(
                text = "Go Back"
            )
        }
    }
}
