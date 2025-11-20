package com.sajjady.flattening.feature.serialization.model

import kotlinx.serialization.Serializable

@Serializable
data class KxUser(
    val id: Long,
    val name: String,
    val email: String
)
