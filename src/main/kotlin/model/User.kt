package org.example.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString

@Serializable
data class User (
    val id: Int,
    var firstName: String,
    var lastName: String,
    var passport: Int,
)