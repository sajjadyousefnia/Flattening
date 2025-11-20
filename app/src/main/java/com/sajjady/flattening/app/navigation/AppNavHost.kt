package com.sajjady.flattening.app.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sajjady.flattening.app.ui.IntroRoadmapScreen
import com.sajjady.flattening.feature.parcelable.ParcelableHomeScreen
import com.sajjady.flattening.feature.serialization.SerializationHomeScreen
import com.sajjady.flattening.feature.mixed.MixedHomeScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    contentPadding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = BottomDestinations.Parcelable.route,
        modifier = Modifier.padding(contentPadding)
    ) {
        // Optional intro/roadmap:
        composable("intro") {
            IntroRoadmapScreen(navController)
        }

        composable(BottomDestinations.Parcelable.route) {
            ParcelableHomeScreen()
        }

        composable(BottomDestinations.Serialization.route) {
            SerializationHomeScreen()
        }

        composable(BottomDestinations.Mixed.route) {
            MixedHomeScreen()
        }
    }
}
