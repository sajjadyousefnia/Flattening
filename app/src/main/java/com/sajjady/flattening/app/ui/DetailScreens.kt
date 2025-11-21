package com.sajjady.flattening.app.ui

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sajjady.flattening.app.ui.components.DirectionAwareText
import com.sajjady.flattening.feature.mixed.navigation.MixedTopics
import com.sajjady.flattening.feature.parcelable.navigation.ParcelableTopics
import com.sajjady.flattening.feature.serialization.navigation.SerializationTopics

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoDetailScreen(topic: String, navController: NavController) {
    val content = remember(topic) { infoContentFor(topic) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { DirectionAwareText(content?.title ?: "جزئیات") },
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
                DirectionAwareText("صفحه پیدا نشد")
                return@Column
            }

            content.paragraphs.forEach { paragraph ->
                DirectionAwareText(paragraph, style = MaterialTheme.typography.bodyLarge)
            }

            DirectionAwareText("برای توضیحات بیشتر:", style = MaterialTheme.typography.titleMedium)
            content.sources.forEach { source ->
                TextButton(onClick = { openInChrome(context, source.url) }) {
                    DirectionAwareText("• ${source.label}", style = MaterialTheme.typography.bodyMedium)
                }
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
                title = { DirectionAwareText("نمونه کد ها") },
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
                DirectionAwareText("نمونه ای موجود نیست")
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
            DirectionAwareText(sample.title, style = MaterialTheme.typography.titleMedium)
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
    val sources: List<DetailSource>
)

data class CodeSample(
    val title: String,
    val code: String
)

data class DetailSource(
    val label: String,
    val url: String
)

private fun infoContentFor(topic: String): DetailContent? = when (topic) {
    ParcelableTopics.ManualInfo -> DetailContent(
        title = "Parcelable دستی در جاوا/کاتلین",
        paragraphs = listOf(
            "در این حالت همه‌چیز به دست خودمان نوشته می‌شود؛ ترتیب نوشتن و خواندن فیلدها در Parcel اهمیت دارد و نباید فراموش شود.",
            "برای کلاس‌های قدیمی یا زمانی که کنترل کامل روی بایت‌ها می‌خواهیم، این الگو همچنان جواب می‌دهد اما کد طولانی و مستعد خطاست."
        ),
        sources = listOf(
            DetailSource("Android Docs – Parcelable", "https://developer.android.com/reference/android/os/Parcelable"),
            DetailSource("Parcel reference", "https://developer.android.com/reference/android/os/Parcel")
        )
    )

    ParcelableTopics.ParcelizeInfo -> DetailContent(
        title = "@Parcelize در کاتلین",
        paragraphs = listOf(
            "با فعال بودن پلاگین kotlin-parcelize، کد تکراری حذف می‌شود و سازندهٔ CREATOR به‌صورت خودکار تولید می‌گردد.",
            "به خاطر داشته باشید که همهٔ پراپرتی‌ها باید parcelable یا قابل نوشتن در Parcel باشند و در Kotlin 2.0 نیز CREATOR تولید شده باید درست لود شود."
        ),
        sources = listOf(
            DetailSource("JetBrains Parcelize reference", "https://kotlinlang.org/docs/parcelize.html"),
            DetailSource("Android KTX samples", "https://developer.android.com/kotlin/parcelize")
        )
    )

    ParcelableTopics.NestedInfo -> DetailContent(
        title = "لیست و Map داخل Parcelable",
        paragraphs = listOf(
            "Parcel می‌تواند لیست، Map و حتی کلاس‌های تودرتو را نگه دارد؛ کافی است نوع‌ها Parcelable یا Primitive باشند.",
            "در مثال سفارش، کاربر، اقلام و meta همگی در یک Parcel نوشته و دوباره خوانده می‌شوند و تعداد آیتم‌ها ثابت می‌ماند."
        ),
        sources = listOf(
            DetailSource("Write collections to Parcel", "https://developer.android.com/reference/android/os/Parcel#writeList(java.util.List%3C?%3E)"),
            DetailSource("Android performance tips", "https://developer.android.com/topic/performance/memory")
        )
    )

    ParcelableTopics.ScreenStateInfo -> DetailContent(
        title = "Parcelize روی sealed class",
        paragraphs = listOf(
            "برای ارسال State بین Activity/Navigation می‌توان sealed class را نیز @Parcelize کرد، کافی است هر زیرکلاس Parcelable باشد.",
            "این تکنیک برای rememberSaveable یا SavedStateHandle ایده‌آل است و چرخهٔ حیات را ساده می‌کند."
        ),
        sources = listOf(
            DetailSource("Compose saved state", "https://developer.android.com/jetpack/compose/state#save-ui-state"),
            DetailSource("SavedStateHandle", "https://developer.android.com/topic/libraries/architecture/viewmodel-savedstate")
        )
    )

    ParcelableTopics.JavaInfo -> DetailContent(
        title = "Parcelable در جاوا",
        paragraphs = listOf(
            "در جاوا باید CREATOR و writeToParcel را دستی بنویسیم. با وجود سرریز کمتر، کد جاوا verbose است اما در پروژه‌های قدیمی هنوز پرکاربرد است.",
            "اگر تیم شما ترکیبی از Java/Kotlin است، این الگو راهی برای اشتراک مدل‌ها بین دو زبان است."
        ),
        sources = listOf(
            DetailSource("Parcelable.Creator", "https://developer.android.com/reference/android/os/Parcelable.Creator"),
            DetailSource("Legacy interop checklists", "https://developer.android.com/guide/components/activities/parcelables-and-bundles")
        )
    )

    SerializationTopics.BasicsInfo -> DetailContent(
        title = "Serializable پایه",
        paragraphs = listOf(
            "java.io.Serializable از بازتاب (Reflection) استفاده می‌کند و بدون کدنویسی اضافه کار می‌کند، اما در اندروید کندتر از Parcelable است.",
            "برای ذخیرهٔ ساده یا انتقال بین JVM ها مفید است ولی به خاطر GC و سربار، برای IPC در اندروید توصیه نمی‌شود."
        ),
        sources = listOf(
            DetailSource("Java Serialization spec", "https://docs.oracle.com/javase/8/docs/api/java/io/Serializable.html"),
            DetailSource("Android performance FAQ", "https://developer.android.com/topic/performance")
        )
    )

    SerializationTopics.TransientInfo -> DetailContent(
        title = "transient و serialVersionUID",
        paragraphs = listOf(
            "فیلدهای transient در جریان Serialization ذخیره نمی‌شوند و در بازگردانی مقدار null می‌گیرند.",
            "serialVersionUID تضمین می‌کند نسخهٔ کلاس در زمان خواندن با نسخهٔ زمان نوشتن سازگار باشد و خطای InvalidClassException رخ ندهد."
        ),
        sources = listOf(
            DetailSource("Effective Java – Item 87", "https://learning.oreilly.com/library/view/effective-java-3rd/9780134686097/ch12.xhtml"),
            DetailSource("Oracle docs on Serialization", "https://docs.oracle.com/javase/8/docs/platform/serialization/spec/serial-arch.html")
        )
    )

    SerializationTopics.KotlinxInfo -> DetailContent(
        title = "kotlinx.serialization (JSON)",
        paragraphs = listOf(
            "کتابخانهٔ رسمی کاتلین برای سریال‌سازی چندفرمت است؛ با annotation @Serializable می‌توانید JSON تمیز و سریع بسازید.",
            "پیکربندی‌هایی مثل prettyPrint یا ignoreUnknownKeys کمک می‌کنند قرارداد API را مطابق نیاز اپ تنظیم کنید."
        ),
        sources = listOf(
            DetailSource("kotlinx.serialization guide", "https://kotlinlang.org/docs/serialization.html"),
            DetailSource("Kotlinlang.org JSON documentation", "https://kotlinlang.org/docs/serialization.json.html")
        )
    )

    SerializationTopics.JavaInfo -> DetailContent(
        title = "Serializable در جاوا",
        paragraphs = listOf(
            "کلاس‌های جاوا نیز می‌توانند با پیاده‌سازی Serializable ذخیره شوند. افزودن transient به داده‌های حساس (مثل رمز) ضروری است.",
            "برای کار با سیستم‌های قدیمی یا فریمورک‌هایی که Serializable می‌خواهند، این گزینه همچنان معتبر است."
        ),
        sources = listOf(
            DetailSource("Java Object Serialization", "https://docs.oracle.com/javase/8/docs/platform/serialization/spec/serial-arch.html"),
            DetailSource("OWASP deserialization guidance", "https://owasp.org/www-community/vulnerabilities/Deserialization_of_untrusted_data")
        )
    )

    MixedTopics.BenchmarkInfo -> DetailContent(
        title = "مقایسهٔ سرعت بین سه رویکرد",
        paragraphs = listOf(
            "بنچمارک کوچک نشان می‌دهد Parcelable معمولاً در IPC سریع‌ترین است و کمترین تخصیص را دارد.",
            "kotlinx.serialization برای JSON مناسب است و Serializable کندترین گزینه در اندروید به حساب می‌آید مگر اینکه به سازگاری نیاز باشد."
        ),
        sources = listOf(
            DetailSource("Android performance whitepaper", "https://developer.android.com/topic/performance/memory"),
            DetailSource("Benchmark tools", "https://developer.android.com/studio/profile/memory-profiler")
        )
    )

    MixedTopics.GuideInfo -> DetailContent(
        title = "راهنمای انتخاب",
        paragraphs = listOf(
            "اگر داده قرار است بین Component های اندروید جابه‌جا شود، Parcelable/@Parcelize انتخاب اول است.",
            "برای تبادل با سرور یا ذخیرهٔ فایل قابل‌خواندن، JSON و کتابخانه‌هایی مثل kotlinx.serialization بهتر هستند؛ Serializable فقط برای میراث."
        ),
        sources = listOf(
            DetailSource("Navigation & Parcelable docs", "https://developer.android.com/guide/navigation/navigation-pass-data"),
            DetailSource("JSON vs Binary trade-offs", "https://developer.android.com/topic/performance/graphics/choose-text-binary")
        )
    )

    MixedTopics.JavaInfo -> DetailContent(
        title = "Hybrid جاوا",
        paragraphs = listOf(
            "گاهی مدل جاوا باید هم Serializable باشد هم Parcelable تا هم با سیستم‌های قدیمی کار کند و هم با Navigation/Bundle جدید.",
            "نوشتن CREATOR و مراقبت از serialVersionUID کمک می‌کند داده در هر دو مسیر بدون خطا round-trip شود."
        ),
        sources = listOf(
            DetailSource("Android IPC guide", "https://developer.android.com/guide/components/activities/parcelables-and-bundles"),
            DetailSource("Interoperability tips", "https://developer.android.com/kotlin/interop")
        )
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
        ),
        CodeSample(
            "Manual read constructor",
            """
            class LegacyUser(parcel: Parcel) : Parcelable {
                val id: Long = parcel.readLong()
                val name: String? = parcel.readString()

                override fun writeToParcel(dest: Parcel, flags: Int) {
                    dest.writeLong(id)
                    dest.writeString(name)
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
        ),
        CodeSample(
            "Custom Parceler",
            """
            object EmailSanitizer : Parceler<String> {
                override fun create(parcel: Parcel) = parcel.readString().orEmpty().lowercase()
                override fun String.write(parcel: Parcel, flags: Int) = parcel.writeString(this.trim())
            }

            @TypeParceler<String, EmailSanitizer>
            @Parcelize
            data class SanitizedUser(val email: String) : Parcelable
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
        ),
        CodeSample(
            "Parcelize + Map",
            """
            @Parcelize
            data class CartMeta(
                val coupons: Map<String, Int> = emptyMap(),
                val flags: MutableList<String> = mutableListOf()
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
        ),
        CodeSample(
            "rememberSaveable state",
            """
            val state by rememberSaveable(stateSaver) {
                mutableStateOf<ScreenState>(ScreenState.Loading)
            }
            LaunchedEffect(Unit) { state = ScreenState.Content(user) }
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
        ),
        CodeSample(
            "describeContents + CREATOR",
            """
            @Override
            public int describeContents() { return 0; }

            public static final Creator<JParcelUser> CREATOR = new Creator<>() {
                @Override public JParcelUser createFromParcel(Parcel in) { return new JParcelUser(in); }
                @Override public JParcelUser[] newArray(int size) { return new JParcelUser[size]; }
            };
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
        ),
        CodeSample(
            "Persist to file",
            """
            FileOutputStream(cacheFile).use { fos ->
                ObjectOutputStream(fos).writeObject(settings)
            }
            ObjectInputStream(FileInputStream(cacheFile)).use { ois ->
                val loaded = ois.readObject() as Settings
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
        ),
        CodeSample(
            "readObject fallback",
            """
            @Throws(IOException::class, ClassNotFoundException::class)
            private fun readObject(input: ObjectInputStream) {
                input.defaultReadObject()
                auditToken = null // transient reset
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
        ),
        CodeSample(
            "SerialName + ignoreUnknownKeys",
            """
            @Serializable
            data class ApiUser(@SerialName("full_name") val name: String)

            val relaxed = Json { ignoreUnknownKeys = true }
            val model = relaxed.decodeFromString<ApiUser>(jsonString)
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
        ),
        CodeSample(
            "Custom writeObject",
            """
            private void writeObject(ObjectOutputStream out) throws IOException {
                out.defaultWriteObject();
                out.writeInt(version);
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
        ),
        CodeSample(
            "measureTimeMillis compare",
            """
            val parcelableTime = measureTimeMillis { writeParcelableMany(times, user) }
            val jsonTime = measureTimeMillis { json.encodeToString(user) }
            log("P: $parcelableTime ms vs JSON: $jsonTime ms")
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
        ),
        CodeSample(
            "Bundle hand-off",
            """
            val args = bundleOf("item" to parcelableModel)
            navController.navigate(R.id.details, args)
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
        ),
        CodeSample(
            "Interop helper",
            """
            fun Intent.putHybrid(name: String, value: JavaHybridUser) {
                putExtra(name, value as Parcelable)
            }
            """
        )
    )

    else -> null
}

private fun openInChrome(context: Context, url: String) {
    val chromeIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
        setPackage("com.android.chrome")
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    try {
        context.startActivity(chromeIntent)
    } catch (_: ActivityNotFoundException) {
        val fallback = Intent(Intent.ACTION_VIEW, Uri.parse(url)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(fallback)
    }
}
