package com.sajjady.flattening.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.sajjady.flattening.app.navigation.AppNavHost
import com.sajjady.flattening.app.navigation.BottomDestinations

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlatteningAppRoot()
        }
    }
}

@Composable
fun FlatteningAppRoot() {
    val navController = rememberNavController()
    var selectedDestination by remember { mutableStateOf(BottomDestinations.Parcelable) }

    MaterialTheme {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    BottomDestinations.values().forEach { dest ->
                        NavigationBarItem(
                            selected = selectedDestination == dest,
                            onClick = {
                                selectedDestination = dest
                                navController.navigate(dest.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            label = { Text(dest.label) },
                            icon = {}
                        )
                    }
                }
            }
        ) { padding ->
            AppNavHost(
                navController = navController,
                contentPadding = padding
            )
        }
    }
}
