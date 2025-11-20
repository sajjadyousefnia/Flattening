package com.sajjady.flattening.app.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sajjady.flattening.app.navigation.BottomDestinations

@Composable
fun IntroRoadmapScreen(navController: NavController) {
    Column(Modifier.padding(16.dp)) {
        Text(
            text = "Flattening – Parcelable & Serialization Roadmap",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(Modifier.height(16.dp))

        RoadmapCard(
            title = "Phase 2 – Serializable (Java)",
            description = "Reflection-based, legacy approach. See Serialization tab.",
            onClick = { navController.navigate(BottomDestinations.Serialization.route) }
        )

        RoadmapCard(
            title = "Phase 3 & 4 – Parcelable / @Parcelize",
            description = "Optimized for Android IPC, used in Bundles & Navigation.",
            onClick = { navController.navigate(BottomDestinations.Parcelable.route) }
        )

        RoadmapCard(
            title = "Phase 5 – Comparison & Limits",
            description = "Performance, TransactionTooLargeException, best practices.",
            onClick = { navController.navigate(BottomDestinations.Mixed.route) }
        )
    }
}

@Composable
private fun RoadmapCard(
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text(description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
