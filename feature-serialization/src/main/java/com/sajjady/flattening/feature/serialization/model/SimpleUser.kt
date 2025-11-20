package com.sajjady.flattening.feature.serialization.model

import java.io.Serializable

data class SimpleUser(
    val id: Long,
    val name: String,
    val email: String
) : Serializable
