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
import com.example.appflow.ui.Screen
import com.example.appflow.utils.safeNavigateOnce

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "SmartNotes - Home", style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.safeNavigateOnce(Screen.AddNote.route)
//                {
//                    launchSingleTop = true
//                }
                Log.d("Navigation","Home to Add Note")
            }
        ) {
            Text("Add Note")
        }

        Button(onClick = {
            navController.safeNavigateOnce(Screen.NoteDetail.createRoute("1"))
//            {
//                launchSingleTop = true
//            }
            Log.d("Navigation","Home to Note Details")
        }) {
            Text("View Note")
        }
    }
}