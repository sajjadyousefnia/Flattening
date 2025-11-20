package com.sajjady.flattening.feature.serialization

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
import com.sajjady.flattening.feature.serialization.model.KxUser
import com.sajjady.flattening.feature.serialization.model.SecureUser
import com.sajjady.flattening.feature.serialization.model.SimpleUser
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.*

@Composable
fun SerializationHomeScreen() {
    Column(Modifier.padding(16.dp)) {
        Text("Serialization Lab", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        SerializableBasicsCard()
        Spacer(Modifier.height(16.dp))
        SerializableTransientCard()
        Spacer(Modifier.height(16.dp))
        KotlinxJsonCard()
    }
}

@Composable
private fun SerializableBasicsCard() {
    var result by remember { mutableStateOf("") }

    Card(Modifier.padding(4.dp)) {
        Column(Modifier.padding(12.dp)) {
            Text("S1 – java.io.Serializable Basics", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Button(onClick = {
                val user = SimpleUser(1L, "Ali", "ali@example.com")
                val bytes = serializeToBytes(user)
                val restored = deserializeFromBytes(bytes)
                result = "Bytes: ${bytes.size} bytes\nOriginal: $user\nRestored: $restored"
            }) {
                Text("Run Serializable demo")
            }
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
private fun SerializableTransientCard() {
    var result by remember { mutableStateOf("") }

    Card(Modifier.padding(4.dp)) {
        Column(Modifier.padding(12.dp)) {
            Text("S2 – transient & serialVersionUID", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Button(onClick = {
                val user = SecureUser("user123", "secretPassword")
                val bos = ByteArrayOutputStream()
                ObjectOutputStream(bos).use { it.writeObject(user) }
                val bytes = bos.toByteArray()
                val restored = ObjectInputStream(ByteArrayInputStream(bytes)).use {
                    it.readObject() as SecureUser
                }
                result = "Original password: ${user.password}\nRestored password: ${restored.password}\n(Password is null/empty because of transient)"
            }) {
                Text("Run transient demo")
            }
            Spacer(Modifier.height(8.dp))
            Text(result)
        }
    }
}

@Composable
private fun KotlinxJsonCard() {
    var jsonText by remember { mutableStateOf("") }
    var roundTrip by remember { mutableStateOf("") }

    Card(Modifier.padding(4.dp)) {
        Column(Modifier.padding(12.dp)) {
            Text("S4 – Kotlinx Serialization JSON", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Button(onClick = {
                val json = Json {
                    prettyPrint = true
                }
                val user = KxUser(5L, "Kotlinx Mina", "mina@example.com")
                val encoded = json.encodeToString(user)
                val decoded = json.decodeFromString<KxUser>(encoded)
                jsonText = encoded
                roundTrip = "Decoded object: $decoded"
            }) {
                Text("Run JSON demo")
            }
            Spacer(Modifier.height(8.dp))
            if (jsonText.isNotEmpty()) {
                Text("JSON:\n$jsonText")
                Spacer(Modifier.height(8.dp))
                Text(roundTrip)
            }
        }
    }
}
