package com.sajjady.flattening.feature.mixed

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
import com.sajjady.flattening.feature.mixed.model.HybridUser
import com.sajjady.flattening.feature.mixed.model.JavaHybridUser
import com.sajjady.flattening.feature.mixed.navigation.MixedTopics
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.*

@Composable
fun MixedHomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Mixed / Comparison Lab", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))
        HybridBenchmarkCard(navController)
        Spacer(Modifier.height(16.dp))
        DecisionGuideCard(navController)
        Spacer(Modifier.height(16.dp))
        JavaHybridCard(navController)
    }
}

@Composable
private fun HybridBenchmarkCard(navController: NavController) {
    var result by remember { mutableStateOf("") }

    Card(Modifier.padding(4.dp)) {
        Column(Modifier.padding(12.dp)) {
            Text("M2 – Hybrid Benchmark", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            ActionRow(
                buttonLabel = "Run benchmark",
                onRun = { result = runHybridBenchmark() },
                onInfo = { navController.navigate(MixedTopics.BenchmarkInfo) },
                onCode = { navController.navigate(MixedTopics.BenchmarkCode) }
            )
            Spacer(Modifier.height(8.dp))
            Text(result)
        }
    }
}

private fun runHybridBenchmark(): String {
    val user = HybridUser(1L, "Hybrid Ali", "ali@example.com")
    val json = Json
    val iterations = 5_000

    // 1) Kotlinx Serialization
    val start1 = System.nanoTime()
    repeat(iterations) {
        val s = json.encodeToString(user)
        json.decodeFromString<HybridUser>(s)
    }
    val t1 = (System.nanoTime() - start1) / 1_000_000

    // 2) java.io.Serializable
    val start2 = System.nanoTime()
    repeat(iterations) {
        val bos = ByteArrayOutputStream()
        ObjectOutputStream(bos).use { it.writeObject(user) }
        val bytes = bos.toByteArray()
        ObjectInputStream(ByteArrayInputStream(bytes)).use {
            it.readObject() as HybridUser
        }
    }
    val t2 = (System.nanoTime() - start2) / 1_000_000

    // 3) Parcelable via Parcel
    val start3 = System.nanoTime()
    repeat(iterations) {
        val parcel = Parcel.obtain()
        user.writeToParcel(parcel, 0)
        parcel.setDataPosition(0)
        parcelableCreator<HybridUser>().createFromParcel(parcel)
        parcel.recycle()
    }
    val t3 = (System.nanoTime() - start3) / 1_000_000

    return buildString {
        appendLine("kotlinx.serialization (JSON): ${t1} ms")
        appendLine("java.io.Serializable: ${t2} ms")
        appendLine("Parcelable (Parcel): ${t3} ms")
    }
}

@Suppress("UNCHECKED_CAST")
private inline fun <reified T : Parcelable> parcelableCreator(): Parcelable.Creator<T> {
    val field = T::class.java.getDeclaredField("CREATOR")
    return field.get(null) as Parcelable.Creator<T>
}

@Composable
private fun DecisionGuideCard(navController: NavController) {
    Card(Modifier.padding(4.dp)) {
        Column(Modifier.padding(12.dp)) {
            Text("M3 – Decision Guide", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Text(
                """
                Use Parcelable / @Parcelize:
                 • Passing data between Activities/Fragments
                 • SavedStateHandle / rememberSaveable
                 • IPC / Service / AIDL

                Use kotlinx.serialization (JSON):
                 • Talking to backend APIs
                 • Human-readable config files
                 • Caching network responses

                Avoid java.io.Serializable in Android:
                 • Reflection-based, slower, GC pressure
                 • Only for legacy interop or when required by APIs
                """.trimIndent()
            )
            Spacer(Modifier.height(8.dp))
            ActionRow(
                buttonLabel = "More details",
                onRun = {},
                onInfo = { navController.navigate(MixedTopics.GuideInfo) },
                onCode = { navController.navigate(MixedTopics.GuideCode) }
            )
        }
    }
}

@Composable
private fun JavaHybridCard(navController: NavController) {
    var message by remember { mutableStateOf("") }

    Card(Modifier.padding(4.dp)) {
        Column(Modifier.padding(12.dp)) {
            Text("M4 – Java Hybrid (Parcelable + Serializable)", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            ActionRow(
                buttonLabel = "Run Java flow",
                onRun = {
                    val user = JavaHybridUser(11L, "Hybrid Java", "hybrid@java.com")
                    val parcel = Parcel.obtain()
                    user.writeToParcel(parcel, 0)
                    parcel.setDataPosition(0)
                    val restored = JavaHybridUser.CREATOR.createFromParcel(parcel)
                    parcel.recycle()

                    val bos = ByteArrayOutputStream()
                    ObjectOutputStream(bos).use { it.writeObject(restored) }
                    val restoredAgain = ObjectInputStream(ByteArrayInputStream(bos.toByteArray())).use {
                        it.readObject() as JavaHybridUser
                    }
                    message = "${restoredAgain.name} -> parcel + serial round-trip"
                },
                onInfo = { navController.navigate(MixedTopics.JavaInfo) },
                onCode = { navController.navigate(MixedTopics.JavaCode) }
            )
            Spacer(Modifier.height(8.dp))
            Text(message)
        }
    }
}

@Composable
private fun ActionRow(
    buttonLabel: String,
    onRun: () -> Unit,
    onInfo: () -> Unit,
    onCode: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Button(onClick = onRun) { Text(buttonLabel) }
        Spacer(Modifier.width(4.dp))
        IconButton(onClick = onInfo) {
            Icon(Icons.Outlined.Info, contentDescription = "اطلاعات بیشتر")
        }
        IconButton(onClick = onCode) {
            Icon(Icons.Outlined.Code, contentDescription = "نمونه کد")
        }
    }
}
