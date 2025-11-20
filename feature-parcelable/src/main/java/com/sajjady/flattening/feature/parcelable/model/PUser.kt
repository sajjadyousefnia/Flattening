package com.sajjady.flattening.feature.parcelable.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PUser(
    val id: Long,
    val name: String,
    val email: String
) : Parcelable
