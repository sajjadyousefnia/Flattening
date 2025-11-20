package com.sajjady.flattening.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sajjady.flattening.feature.mixed.navigation.MixedTopics
import com.sajjady.flattening.feature.parcelable.navigation.ParcelableTopics
import com.sajjady.flattening.feature.serialization.navigation.SerializationTopics

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoDetailScreen(topic: String, navController: NavController) {
    val content = remember(topic) { infoContentFor(topic) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(content?.title ?: "جزئیات") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "بازگشت")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (content == null) {
                Text("صفحه پیدا نشد")
                return@Column
            }

            content.paragraphs.forEach { paragraph ->
                Text(paragraph, style = MaterialTheme.typography.bodyLarge)
            }

            Text("منابع:", style = MaterialTheme.typography.titleMedium)
            content.sources.forEach { source ->
                Text("• $source", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodeSamplesScreen(topic: String, navController: NavController) {
    val samples = remember(topic) { codeSamplesFor(topic) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("نمونه کد ها") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "بازگشت")
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Outlined.Code, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (samples.isNullOrEmpty()) {
                Text("نمونه ای موجود نیست")
                return@Column
            }

            samples.forEach { sample ->
                SampleCard(sample)
            }
        }
    }
}

@Composable
private fun SampleCard(sample: CodeSample) {
    Surface(
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(sample.title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(6.dp))
            Text(
                text = sample.code.trimIndent(),
                fontFamily = FontFamily.Monospace,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

data class DetailContent(
    val title: String,
    val paragraphs: List<String>,
    val sources: List<String>
)

data class CodeSample(
    val title: String,
    val code: String
)

private fun infoContentFor(topic: String): DetailContent? = when (topic) {
    ParcelableTopics.ManualInfo -> DetailContent(
        title = "Parcelable دستی در جاوا/کاتلین",
        paragraphs = listOf(
            "در این حالت همه‌چیز به دست خودمان نوشته می‌شود؛ ترتیب نوشتن و خواندن فیلدها در Parcel اهمیت دارد و نباید فراموش شود.",
            "برای کلاس‌های قدیمی یا زمانی که کنترل کامل روی بایت‌ها می‌خواهیم، این الگو همچنان جواب می‌دهد اما کد طولانی و مستعد خطاست."
        ),
        sources = listOf(
            "Android Docs – Parcelable", "Guides by developer.android.com"
        )
    )

    ParcelableTopics.ParcelizeInfo -> DetailContent(
        title = "@Parcelize در کاتلین",
        paragraphs = listOf(
            "با فعال بودن پلاگین kotlin-parcelize، کد تکراری حذف می‌شود و سازندهٔ CREATOR به‌صورت خودکار تولید می‌گردد.",
            "به خاطر داشته باشید که همهٔ پراپرتی‌ها باید parcelable یا قابل نوشتن در Parcel باشند، و از نسخهٔ Kotlin 2.0 نیز همچنان باید CREATOR تولید شده را درست لود کنید."
        ),
        sources = listOf(
            "JetBrains Parcelize reference", "Android KTX samples"
        )
    )

    ParcelableTopics.NestedInfo -> DetailContent(
        title = "لیست و Map داخل Parcelable",
        paragraphs = listOf(
            "Parcel می‌تواند لیست، Map و حتی کلاس‌های تودرتو را نگه دارد؛ کافی است نوع‌ها Parcelable یا Primitive باشند.",
            "در مثال سفارش، کاربر، اقلام و meta همگی در یک Parcel نوشته و دوباره خوانده می‌شوند و تعداد آیتم‌ها ثابت می‌ماند."
        ),
        sources = listOf(
            "Official sample: Writing collections to Parcel", "Android performance tips"
        )
    )

    ParcelableTopics.ScreenStateInfo -> DetailContent(
        title = "Parcelize روی sealed class",
        paragraphs = listOf(
            "برای ارسال State بین Activity/Navigation می‌توان sealed class را نیز @Parcelize کرد، کافی است هر زیرکلاس Parcelable باشد.",
            "این تکنیک برای rememberSaveable یا SavedStateHandle ایده‌آل است و چرخهٔ حیات را ساده می‌کند."
        ),
        sources = listOf(
            "Compose saved state docs", "Sealed classes + Parcelize codelab"
        )
    )

    ParcelableTopics.JavaInfo -> DetailContent(
        title = "Parcelable در جاوا",
        paragraphs = listOf(
            "در جاوا باید CREATOR و writeToParcel را دستی بنویسیم. با وجود سرریز کمتر، کد جاوا verbose است اما در پروژه‌های قدیمی هنوز پرکاربرد است.",
            "اگر تیم شما ترکیبی از Java/Kotlin است، این الگو راهی برای اشتراک مدل‌ها بین دو زبان است."
        ),
        sources = listOf("Android Parcelable guide", "Legacy interop checklists")
    )

    SerializationTopics.BasicsInfo -> DetailContent(
        title = "Serializable پایه",
        paragraphs = listOf(
            "java.io.Serializable از بازتاب (Reflection) استفاده می‌کند و بدون کدنویسی اضافه کار می‌کند، اما در اندروید کندتر از Parcelable است.",
            "برای ذخیرهٔ ساده یا انتقال بین JVM ها مفید است ولی به خاطر GC و سربار، برای IPC در اندروید توصیه نمی‌شود."
        ),
        sources = listOf("Java Serialization spec", "Android performance FAQ")
    )

    SerializationTopics.TransientInfo -> DetailContent(
        title = "transient و serialVersionUID",
        paragraphs = listOf(
            "فیلدهای transient در جریان Serialization ذخیره نمی‌شوند و در بازگردانی مقدار null می‌گیرند.",
            "serialVersionUID تضمین می‌کند نسخهٔ کلاس در زمان خواندن با نسخهٔ زمان نوشتن سازگار باشد و خطای InvalidClassException رخ ندهد."
        ),
        sources = listOf("Effective Java – Item 87", "Oracle docs on Serialization")
    )

    SerializationTopics.KotlinxInfo -> DetailContent(
        title = "kotlinx.serialization (JSON)",
        paragraphs = listOf(
            "کتابخانهٔ رسمی کاتلین برای سریال‌سازی چندفرمت است؛ با annotation @Serializable می‌توانید JSON تمیز و سریع بسازید.",
            "پیکربندی‌هایی مثل prettyPrint یا ignoreUnknownKeys کمک می‌کنند قرارداد API را مطابق نیاز اپ تنظیم کنید."
        ),
        sources = listOf("kotlinx.serialization guide", "Kotlinlang.org JSON documentation")
    )

    SerializationTopics.JavaInfo -> DetailContent(
        title = "Serializable در جاوا",
        paragraphs = listOf(
            "کلاس‌های جاوا نیز می‌توانند با پیاده‌سازی Serializable ذخیره شوند. افزودن transient به داده‌های حساس (مثل رمز) ضروری است.",
            "برای کار با سیستم‌های قدیمی یا فریمورک‌هایی که Serializable می‌خواهند، این گزینه همچنان معتبر است."
        ),
        sources = listOf("Java Object Serialization", "OWASP guidance for transient fields")
    )

    MixedTopics.BenchmarkInfo -> DetailContent(
        title = "مقایسهٔ سرعت بین سه رویکرد",
        paragraphs = listOf(
            "بنچمارک کوچک نشان می‌دهد Parcelable معمولاً در IPC سریع‌ترین است و کمترین تخصیص را دارد.",
            "kotlinx.serialization برای JSON مناسب است و Serializable کندترین گزینه در اندروید به حساب می‌آید مگر اینکه به سازگاری نیاز باشد."
        ),
        sources = listOf("Android performance whitepaper", "Benchmark tools on developer.android.com")
    )

    MixedTopics.GuideInfo -> DetailContent(
        title = "راهنمای انتخاب",
        paragraphs = listOf(
            "اگر داده قرار است بین Component های اندروید جابه‌جا شود، Parcelable/@Parcelize انتخاب اول است.",
            "برای تبادل با سرور یا ذخیرهٔ فایل قابل‌خواندن، JSON و کتابخانه‌هایی مثل kotlinx.serialization بهتر هستند؛ Serializable فقط برای میراث."
        ),
        sources = listOf("Navigation & Parcelable docs", "JSON vs Binary trade-offs article")
    )

    MixedTopics.JavaInfo -> DetailContent(
        title = "Hybrid جاوا",
        paragraphs = listOf(
            "گاهی مدل جاوا باید هم Serializable باشد هم Parcelable تا هم با سیستم‌های قدیمی کار کند و هم با Navigation/Bundle جدید.",
            "نوشتن CREATOR و مراقبت از serialVersionUID کمک می‌کند داده در هر دو مسیر بدون خطا round-trip شود."
        ),
        sources = listOf("Android IPC guide", "Interoperability tips for Java/Kotlin")
    )

    else -> null
}

private fun codeSamplesFor(topic: String): List<CodeSample>? = when (topic) {
    ParcelableTopics.ManualCode -> listOf(
        CodeSample(
            "LegacyUser manual write/read",
            """
            class LegacyUser(...) : Parcelable {
                override fun writeToParcel(parcel: Parcel, flags: Int) {
                    parcel.writeLong(id)
                    parcel.writeString(name)
                }
                companion object CREATOR : Parcelable.Creator<LegacyUser> {
                    override fun createFromParcel(parcel: Parcel) = LegacyUser(parcel)
                }
            }
            """
        )
    )

    ParcelableTopics.ParcelizeCode -> listOf(
        CodeSample(
            "@Parcelize data class",
            """
            @Parcelize
data class PUser(val id: Long, val name: String, val email: String) : Parcelable
            """
        )
    )

    ParcelableTopics.NestedCode -> listOf(
        CodeSample(
            "Collection inside Parcel",
            """
            @Parcelize
data class POrder(
    val id: Long,
    val user: PUser,
    val items: List<POrderItem>,
    val meta: Map<String, String>
) : Parcelable
            """
        )
    )

    ParcelableTopics.ScreenStateCode -> listOf(
        CodeSample(
            "Sealed ScreenState",
            """
            @Parcelize
            sealed class ScreenState : Parcelable {
                @Parcelize
                data class Content(val user: PUser) : ScreenState()
                @Parcelize
                data object Loading : ScreenState()
                @Parcelize
                data class Error(val message: String) : ScreenState()
            }
            """
        )
    )

    ParcelableTopics.JavaCode -> listOf(
        CodeSample(
            "Java Parcelable",
            """
            public class JParcelUser implements Parcelable {
                protected JParcelUser(Parcel in) {
                    id = in.readLong();
                    name = in.readString();
                    email = in.readString();
                }
                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeLong(id);
                    dest.writeString(name);
                    dest.writeString(email);
                }
            }
            """
        )
    )

    SerializationTopics.BasicsCode -> listOf(
        CodeSample(
            "Serialize to bytes",
            """
            val bos = ByteArrayOutputStream()
            ObjectOutputStream(bos).use { it.writeObject(SimpleUser(...)) }
            val bytes = bos.toByteArray()
            val restored = ObjectInputStream(ByteArrayInputStream(bytes)).use {
                it.readObject() as SimpleUser
            }
            """
        )
    )

    SerializationTopics.TransientCode -> listOf(
        CodeSample(
            "Mark field transient",
            """
            data class SecureUser(
                val username: String,
                @Transient val password: String
            ) : Serializable {
                companion object { private const val serialVersionUID = 2L }
            }
            """
        )
    )

    SerializationTopics.KotlinxCode -> listOf(
        CodeSample(
            "kotlinx JSON",
            """
            val json = Json { prettyPrint = true }
            val payload = json.encodeToString(KxUser(1, "Nima", "n@ex.com"))
            val back = json.decodeFromString<KxUser>(payload)
            """
        )
    )

    SerializationTopics.JavaCode -> listOf(
        CodeSample(
            "Java Serializable",
            """
            public class JavaSerializableUser implements Serializable {
                @Serial private static final long serialVersionUID = 42L;
                private final transient String password;
            }
            """
        )
    )

    MixedTopics.BenchmarkCode -> listOf(
        CodeSample(
            "Mini benchmark",
            """
            repeat(iterations) {
                val parcel = Parcel.obtain()
                user.writeToParcel(parcel, 0)
                parcel.setDataPosition(0)
                parcelableCreator<HybridUser>().createFromParcel(parcel)
                parcel.recycle()
            }
            """
        )
    )

    MixedTopics.GuideCode -> listOf(
        CodeSample(
            "Decision bullets",
            """
            // Parcelable => IPC و Navigation
            // kotlinx.serialization => JSON و API
            // Serializable => فقط برای سازگاری
            """
        )
    )

    MixedTopics.JavaCode -> listOf(
        CodeSample(
            "Java hybrid class",
            """
            public class JavaHybridUser implements Parcelable, Serializable {
                @Serial private static final long serialVersionUID = 7L;
                @Override
                public void writeToParcel(Parcel dest, int flags) { /*...*/ }
            }
            """
        )
    )

    else -> null
}
