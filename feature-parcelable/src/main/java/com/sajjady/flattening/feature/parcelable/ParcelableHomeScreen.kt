package com.sajjady.flattening.feature.parcelable

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sajjady.flattening.feature.parcelable.model.*
import com.sajjady.flattening.feature.parcelable.navigation.ParcelableTopics

@Composable
fun ParcelableHomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            "Parcelable Lab",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(16.dp))
        ManualParcelableDemo(navController)
        Spacer(Modifier.height(16.dp))
        ParcelizeBasicsDemo(navController)
        Spacer(Modifier.height(16.dp))
        ParcelizeNestedDemo(navController)
        Spacer(Modifier.height(16.dp))
        ScreenStateDemo(navController)
        Spacer(Modifier.height(16.dp))
        JavaParcelableDemo(navController)
    }
}

@Composable
private fun ManualParcelableDemo(navController: NavController) {
    var result by remember { mutableStateOf("") }

    Card(Modifier.padding(4.dp)) {
        Column(Modifier.padding(12.dp)) {
            Text("P1 – Manual Parcelable (LegacyUser)", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            ActionRow(
                onRun = {
                    val user = LegacyUser(1L, "Manual Ali", "manual@example.com")
                    val parcel = Parcel.obtain()
                    user.writeToParcel(parcel, 0)
                    parcel.setDataPosition(0)
                    val restored = LegacyUser.CREATOR.createFromParcel(parcel)
                    parcel.recycle()
                    result = "Original: $user\nRestored: $restored"
                },
                onInfo = { navController.navigate(ParcelableTopics.ManualInfo) },
                onCode = { navController.navigate(ParcelableTopics.ManualCode) }
            )
            Spacer(Modifier.height(8.dp))
            Text(result)
        }
    }
}

@Composable
private fun ParcelizeBasicsDemo(navController: NavController) {
    var result by remember { mutableStateOf("") }

    Card(Modifier.padding(4.dp)) {
        Column(Modifier.padding(12.dp)) {
            Text("P2 – @Parcelize Basics (PUser)", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            ActionRow(
                onRun = {
                    val user = PUser(2L, "Parcelized Sara", "parcel@example.com")
                    val parcel = Parcel.obtain()
                    user.writeToParcel(parcel, 0)
                    parcel.setDataPosition(0)
                    val restored = parcelableCreator<PUser>().createFromParcel(parcel)
                    parcel.recycle()
                    result = "Original: $user\nRestored: $restored"
                },
                onInfo = { navController.navigate(ParcelableTopics.ParcelizeInfo) },
                onCode = { navController.navigate(ParcelableTopics.ParcelizeCode) }
            )
            Spacer(Modifier.height(8.dp))
            Text(result)
        }
    }
}

@Suppress("UNCHECKED_CAST")
private inline fun <reified T : Parcelable> parcelableCreator(): Parcelable.Creator<T> {
    // Reflectively access the generated CREATOR to avoid direct references in Kotlin 2.0+
    val field = T::class.java.getDeclaredField("CREATOR")
    return field.get(null) as Parcelable.Creator<T>
}

@Composable
private fun ParcelizeNestedDemo(navController: NavController) {
    var info by remember { mutableStateOf("") }

    Card(Modifier.padding(4.dp)) {
        Column(Modifier.padding(12.dp)) {
            Text("P3 – Nested & Collection Parcelable (POrder)", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            ActionRow(
                onRun = {
                    val user = PUser(3L, "Nested Nima", "nested@example.com")
                    val items = listOf(
                        POrderItem(1, "Book", 2),
                        POrderItem(2, "Pen", 5)
                    )
                    val order = POrder(
                        id = 10L,
                        user = user,
                        items = items,
                        meta = mapOf("source" to "demo")
                    )
                    val parcel = Parcel.obtain()
                    order.writeToParcel(parcel, 0)
                    parcel.setDataPosition(0)
                    val restored = parcelableCreator<POrder>().createFromParcel(parcel)
                    parcel.recycle()
                    info = "Items count: ${restored.items.size}, meta[source]=${restored.meta["source"]}"
                },
                onInfo = { navController.navigate(ParcelableTopics.NestedInfo) },
                onCode = { navController.navigate(ParcelableTopics.NestedCode) }
            )
            Spacer(Modifier.height(8.dp))
            Text(info)
        }
    }
}

@Composable
private fun ScreenStateDemo(navController: NavController) {
    var state: ScreenState by remember { mutableStateOf(ScreenState.Loading) }

    Card(Modifier.padding(4.dp)) {
        Column(Modifier.padding(12.dp)) {
            Text("P4 – @Parcelize on sealed ScreenState", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            ActionRow(
                onRun = {
                    state = when (state) {
                        ScreenState.Loading -> ScreenState.Content(PUser(4L, "Content User", "c@example.com"))
                        is ScreenState.Content -> ScreenState.Error("Something went wrong")
                        is ScreenState.Error -> ScreenState.Loading
                    }
                },
                onInfo = { navController.navigate(ParcelableTopics.ScreenStateInfo) },
                onCode = { navController.navigate(ParcelableTopics.ScreenStateCode) }
            )
            Spacer(Modifier.height(8.dp))
            Text("Current state: $state")
        }
    }
}

@Composable
private fun JavaParcelableDemo(navController: NavController) {
    var status by remember { mutableStateOf("") }

    Card(Modifier.padding(4.dp)) {
        Column(Modifier.padding(12.dp)) {
            Text("P5 – Java Parcelable (JParcelUser)", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            ActionRow(
                onRun = {
                    val javaUser = JParcelUser(5L, "Java Meghdad", "java@example.com")
                    val parcel = Parcel.obtain()
                    javaUser.writeToParcel(parcel, 0)
                    parcel.setDataPosition(0)
                    val restored = JParcelUser.CREATOR.createFromParcel(parcel)
                    parcel.recycle()
                    status = "Round-trip => ${restored.name} / ${restored.email}"
                },
                onInfo = { navController.navigate(ParcelableTopics.JavaInfo) },
                onCode = { navController.navigate(ParcelableTopics.JavaCode) }
            )
            Spacer(Modifier.height(8.dp))
            Text(status)
        }
    }
}

@Composable
private fun ActionRow(
    onRun: () -> Unit,
    onInfo: () -> Unit,
    onCode: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Button(onClick = onRun) { Text("Run demo") }
        Spacer(Modifier.width(4.dp))
        IconButton(onClick = onInfo) {
            Icon(imageVector = Icons.Outlined.Info, contentDescription = "جزئیات بیشتر")
        }
        IconButton(onClick = onCode) {
            Icon(imageVector = Icons.Outlined.Code, contentDescription = "نمونه کد")
        }
    }
}
