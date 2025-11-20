package com.sajjady.flattening.feature.parcelable

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sajjady.flattening.feature.parcelable.model.*

@Composable
fun ParcelableHomeScreen() {
    Column(Modifier.padding(16.dp)) {
        Text(
            "Parcelable Lab",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(16.dp))
        ManualParcelableDemo()
        Spacer(Modifier.height(16.dp))
        ParcelizeBasicsDemo()
        Spacer(Modifier.height(16.dp))
        ParcelizeNestedDemo()
        Spacer(Modifier.height(16.dp))
        ScreenStateDemo()
    }
}

@Composable
private fun ManualParcelableDemo() {
    var result by remember { mutableStateOf("") }

    Card(Modifier.padding(4.dp)) {
        Column(Modifier.padding(12.dp)) {
            Text("P1 – Manual Parcelable (LegacyUser)", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Button(onClick = {
                val user = LegacyUser(1L, "Manual Ali", "manual@example.com")
                val parcel = Parcel.obtain()
                user.writeToParcel(parcel, 0)
                parcel.setDataPosition(0)
                val restored = LegacyUser.CREATOR.createFromParcel(parcel)
                parcel.recycle()
                result = "Original: $user\nRestored: $restored"
            }) {
                Text("Run demo")
            }
            Spacer(Modifier.height(8.dp))
            Text(result)
        }
    }
}

@Composable
private fun ParcelizeBasicsDemo() {
    var result by remember { mutableStateOf("") }

    Card(Modifier.padding(4.dp)) {
        Column(Modifier.padding(12.dp)) {
            Text("P2 – @Parcelize Basics (PUser)", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Button(onClick = {
                val user = PUser(2L, "Parcelized Sara", "parcel@example.com")
                val parcel = Parcel.obtain()
                user.writeToParcel(parcel, 0)
                parcel.setDataPosition(0)
                val restored = parcelableCreator<PUser>().createFromParcel(parcel)
                parcel.recycle()
                result = "Original: $user\nRestored: $restored"
            }) {
                Text("Run demo")
            }
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
private fun ParcelizeNestedDemo() {
    var info by remember { mutableStateOf("") }

    Card(Modifier.padding(4.dp)) {
        Column(Modifier.padding(12.dp)) {
            Text("P3 – Nested & Collection Parcelable (POrder)", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Button(onClick = {
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
            }) {
                Text("Run demo")
            }
            Spacer(Modifier.height(8.dp))
            Text(info)
        }
    }
}

@Composable
private fun ScreenStateDemo() {
    var state: ScreenState by remember { mutableStateOf(ScreenState.Loading) }

    Card(Modifier.padding(4.dp)) {
        Column(Modifier.padding(12.dp)) {
            Text("P4 – @Parcelize on sealed ScreenState", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Button(onClick = {
                state = when (state) {
                    ScreenState.Loading -> ScreenState.Content(PUser(4L, "Content User", "c@example.com"))
                    is ScreenState.Content -> ScreenState.Error("Something went wrong")
                    is ScreenState.Error -> ScreenState.Loading
                }
            }) {
                Text("Toggle state")
            }
            Spacer(Modifier.height(8.dp))
            Text("Current state: $state")
        }
    }
}
