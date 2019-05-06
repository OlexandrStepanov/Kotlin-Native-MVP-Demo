package com.akqa.kn.lib

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Post(val title: String = "", val text: String = "") {
    companion object {
        fun parse(json: String): Post {
            return Json.parse(serializer(), json)
        }
    }
}