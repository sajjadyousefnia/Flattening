package com.sajjady.flattening.feature.serialization

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
import com.sajjady.flattening.feature.serialization.model.KxUser
import com.sajjady.flattening.feature.serialization.model.SecureUser
import com.sajjady.flattening.feature.serialization.model.SimpleUser
import com.sajjady.flattening.feature.serialization.model.JavaSerializableUser
import com.sajjady.flattening.feature.serialization.navigation.SerializationTopics
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.*

@Composable
fun SerializationHomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Serialization Lab", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        SerializableBasicsCard(navController)
        Spacer(Modifier.height(16.dp))
        SerializableTransientCard(navController)
        Spacer(Modifier.height(16.dp))
        KotlinxJsonCard(navController)
        Spacer(Modifier.height(16.dp))
        JavaSerializableCard(navController)
    }
}

@Composable
private fun SerializableBasicsCard(navController: NavController) {
    var result by remember { mutableStateOf("") }

    Card(Modifier.padding(4.dp)) {
        Column(Modifier.padding(12.dp)) {
            Text("S1 – java.io.Serializable Basics", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            ActionRow(
                buttonLabel = "Run Serializable demo",
                onRun = {
                    val user = SimpleUser(1L, "Ali", "ali@example.com")
                    val bytes = serializeToBytes(user)
                    val restored = deserializeFromBytes(bytes)
                    result = "Bytes: ${bytes.size} bytes\nOriginal: $user\nRestored: $restored"
                },
                onInfo = { navController.navigate(SerializationTopics.BasicsInfo) },
                onCode = { navController.navigate(SerializationTopics.BasicsCode) }
            )
            Spacer(Modifier.height(8.dp))
            Text(result)
        }
    }
}

private fun serializeToBytes(user: SimpleUser): ByteArray {
    val bos = ByteArrayOutputStream()
    ObjectOutputStream(bos).use { out ->
        out.writeObject(user)
    }
    return bos.toByteArray()
}

private fun deserializeFromBytes(bytes: ByteArray): SimpleUser {
    return ObjectInputStream(ByteArrayInputStream(bytes)).use { inp ->
        inp.readObject() as SimpleUser
    }
}

@Composable
private fun SerializableTransientCard(navController: NavController) {
    var result by remember { mutableStateOf("") }

    Card(Modifier.padding(4.dp)) {
        Column(Modifier.padding(12.dp)) {
            Text("S2 – transient & serialVersionUID", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            ActionRow(
                buttonLabel = "Run transient demo",
                onRun = {
                    val user = SecureUser("user123", "secretPassword")
                    val bos = ByteArrayOutputStream()
                    ObjectOutputStream(bos).use { it.writeObject(user) }
                    val bytes = bos.toByteArray()
                    val restored = ObjectInputStream(ByteArrayInputStream(bytes)).use {
                        it.readObject() as SecureUser
                    }
                    result = "Original password: ${user.password}\nRestored password: ${restored.password}\n(Password is null/empty because of transient)"
                },
                onInfo = { navController.navigate(SerializationTopics.TransientInfo) },
                onCode = { navController.navigate(SerializationTopics.TransientCode) }
            )
            Spacer(Modifier.height(8.dp))
            Text(result)
        }
    }
}

@Composable
private fun KotlinxJsonCard(navController: NavController) {
    var jsonText by remember { mutableStateOf("") }
    var roundTrip by remember { mutableStateOf("") }

    Card(Modifier.padding(4.dp)) {
        Column(Modifier.padding(12.dp)) {
            Text("S4 – Kotlinx Serialization JSON", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            ActionRow(
                buttonLabel = "Run JSON demo",
                onRun = {
                    val json = Json {
                        prettyPrint = true
                    }
                    val user = KxUser(5L, "Kotlinx Mina", "mina@example.com")
                    val encoded = json.encodeToString(user)
                    val decoded = json.decodeFromString<KxUser>(encoded)
                    jsonText = encoded
                    roundTrip = "Decoded object: $decoded"
                },
                onInfo = { navController.navigate(SerializationTopics.KotlinxInfo) },
                onCode = { navController.navigate(SerializationTopics.KotlinxCode) }
            )
            Spacer(Modifier.height(8.dp))
            if (jsonText.isNotEmpty()) {
                Text("JSON:\n$jsonText")
                Spacer(Modifier.height(8.dp))
                Text(roundTrip)
            }
        }
    }
}

@Composable
private fun JavaSerializableCard(navController: NavController) {
    var javaRoundTrip by remember { mutableStateOf("") }

    Card(Modifier.padding(4.dp)) {
        Column(Modifier.padding(12.dp)) {
            Text("S5 – Java Serializable (transient)", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            ActionRow(
                buttonLabel = "Run Java serialization",
                onRun = {
                    val user = JavaSerializableUser(8L, "legacyUser", "password123")
                    val bos = ByteArrayOutputStream()
                    ObjectOutputStream(bos).use { it.writeObject(user) }
                    val bytes = bos.toByteArray()
                    val restored = ObjectInputStream(ByteArrayInputStream(bytes)).use {
                        it.readObject() as JavaSerializableUser
                    }
                    javaRoundTrip = "${bytes.size} bytes | restored password=${restored.password}"
                },
                onInfo = { navController.navigate(SerializationTopics.JavaInfo) },
                onCode = { navController.navigate(SerializationTopics.JavaCode) }
            )
            Spacer(Modifier.height(8.dp))
            Text(javaRoundTrip)
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
