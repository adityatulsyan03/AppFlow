package com.example.appflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.appflow.sync.WorkManagerUtil
import com.example.appflow.ui.navigator.AppNavigation
import com.example.appflow.ui.theme.AppFlowTheme
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppFlowTheme {
                val navController = rememberNavController()
                AppNavigation(navController)
            }
        }
    }
}