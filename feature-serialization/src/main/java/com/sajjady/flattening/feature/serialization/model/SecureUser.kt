package com.sajjady.flattening.feature.serialization.model

import java.io.Serializable

data class SecureUser(
    val username: String,
    @Transient
    val password: String
) : Serializable {
    companion object {
        private const val serialVersionUID: Long = 1L
    }
}
