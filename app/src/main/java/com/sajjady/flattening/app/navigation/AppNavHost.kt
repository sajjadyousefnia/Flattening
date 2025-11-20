package com.sajjady.flattening.app.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sajjady.flattening.app.ui.CodeSamplesScreen
import com.sajjady.flattening.app.ui.InfoDetailScreen
import com.sajjady.flattening.app.ui.IntroRoadmapScreen
import com.sajjady.flattening.feature.parcelable.ParcelableHomeScreen
import com.sajjady.flattening.feature.parcelable.navigation.ParcelableTopics
import com.sajjady.flattening.feature.serialization.SerializationHomeScreen
import com.sajjady.flattening.feature.serialization.navigation.SerializationTopics
import com.sajjady.flattening.feature.mixed.MixedHomeScreen
import com.sajjady.flattening.feature.mixed.navigation.MixedTopics

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
            ParcelableHomeScreen(navController)
        }

        composable(BottomDestinations.Serialization.route) {
            SerializationHomeScreen(navController)
        }

        composable(BottomDestinations.Mixed.route) {
            MixedHomeScreen(navController)
        }

        listOf(
            ParcelableTopics.ManualInfo,
            ParcelableTopics.ParcelizeInfo,
            ParcelableTopics.NestedInfo,
            ParcelableTopics.ScreenStateInfo,
            ParcelableTopics.JavaInfo,
            SerializationTopics.BasicsInfo,
            SerializationTopics.TransientInfo,
            SerializationTopics.KotlinxInfo,
            SerializationTopics.JavaInfo,
            MixedTopics.BenchmarkInfo,
            MixedTopics.GuideInfo,
            MixedTopics.JavaInfo
        ).forEach { route ->
            composable(route) { InfoDetailScreen(route, navController) }
        }

        listOf(
            ParcelableTopics.ManualCode,
            ParcelableTopics.ParcelizeCode,
            ParcelableTopics.NestedCode,
            ParcelableTopics.ScreenStateCode,
            ParcelableTopics.JavaCode,
            SerializationTopics.BasicsCode,
            SerializationTopics.TransientCode,
            SerializationTopics.KotlinxCode,
            SerializationTopics.JavaCode,
            MixedTopics.BenchmarkCode,
            MixedTopics.GuideCode,
            MixedTopics.JavaCode
        ).forEach { route ->
            composable(route) { CodeSamplesScreen(route, navController) }
        }
    }
}
