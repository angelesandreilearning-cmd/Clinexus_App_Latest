package com.example.clinexusapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.clinexusapp.ui.navigation.SetupNavGraph
import com.example.clinexusapp.ui.theme.ClinexusAppTheme
import com.example.clinexusapp.util.NotificationHelper
import com.example.clinexusapp.viewmodel.SettingsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize notifications
        NotificationHelper.createNotificationChannel(this)
        
        enableEdgeToEdge()
        setContent {
            val settingsViewModel: SettingsViewModel = viewModel()
            val isDarkMode by settingsViewModel.isDarkMode.collectAsState()

            ClinexusAppTheme(darkTheme = isDarkMode) {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    SetupNavGraph(navController = navController, settingsViewModel = settingsViewModel)
                }
            }
        }
    }
}
