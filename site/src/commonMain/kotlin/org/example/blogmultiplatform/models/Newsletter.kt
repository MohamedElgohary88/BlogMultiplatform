package org.example.blogmultiplatform.models

import kotlinx.serialization.Serializable

@Serializable
data class Newsletter(
    val email: String
)
