package com.mindera.alfie.core.test

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

inline fun <reified T> T.toJson(): String = Json.encodeToString(this)

inline fun <reified M> String.getObjectFromJson(): M = Json.decodeFromString(this)
