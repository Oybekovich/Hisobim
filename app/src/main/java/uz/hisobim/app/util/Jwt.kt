package uz.hisobim.app.util

import android.util.Base64
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive

private val jwtJson = Json { ignoreUnknownKeys = true }

/**
 * JWT access token'ining payload qismidan `session_id` claim'ini ajratadi.
 * Imzo tekshirilmaydi — bu faqat joriy sessiyani aniqlash uchun ishlatiladi.
 */
fun sessionIdFromJwt(accessToken: String?): String? {
    if (accessToken.isNullOrBlank()) return null
    val parts = accessToken.split(".")
    if (parts.size < 2) return null
    return runCatching {
        val bytes = Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
        val obj = jwtJson.parseToJsonElement(String(bytes)) as JsonObject
        obj["session_id"]?.jsonPrimitive?.contentOrNull
    }.getOrNull()
}
