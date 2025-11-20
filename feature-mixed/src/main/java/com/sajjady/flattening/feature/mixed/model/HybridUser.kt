package com.sajjady.flattening.feature.mixed.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import java.io.Serializable as JavaSerializable

@Parcelize
@Serializable
data class HybridUser(
    val id: Long,
    val name: String,
    val email: String
) : Parcelable, JavaSerializable
