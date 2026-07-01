package uz.hisobim.app.data.repo

import android.os.Build
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import uz.hisobim.app.Graph
import uz.hisobim.app.data.model.UpsertDevicePayload
import uz.hisobim.app.data.model.UserDevice
import uz.hisobim.app.data.remote.SupabaseProvider
import uz.hisobim.app.util.sessionIdFromJwt
import java.time.Instant

/**
 * Hisobga kirgan qurilmalar ro'yxati va masofadan chiqarish.
 * Ro'yxat/yozuv — RLS himoyasidagi `user_devices` jadvali orqali.
 * Chiqarish — `revoke_device` SECURITY DEFINER RPC (haqiqiy auth.sessions o'chirish).
 */
class DeviceRepository(
    private val client: SupabaseClient = SupabaseProvider.client,
) {
    /** "Samsung SM-A525F" ko'rinishidagi qurilma nomi. */
    fun currentModel(): String =
        listOf(Build.MANUFACTURER, Build.MODEL)
            .filter { it.isNotBlank() }
            .joinToString(" ")
            .trim()
            .replaceFirstChar { it.uppercase() }
            .ifBlank { "Android qurilma" }

    private fun currentSessionId(): String? =
        sessionIdFromJwt(client.auth.currentSessionOrNull()?.accessToken)

    /** Ushbu install uchun barqaror device_id. */
    suspend fun currentDeviceId(): String = Graph.deviceIdProvider.get()

    /** Joriy qurilmani ro'yxatga qo'shadi/yangilaydi (upsert). */
    suspend fun registerCurrentDevice(userId: String) {
        client.from("user_devices").upsert(
            UpsertDevicePayload(
                userId = userId,
                deviceId = currentDeviceId(),
                model = currentModel(),
                sessionId = currentSessionId(),
                lastActiveAt = Instant.now().toString(),
                revoked = false,
            )
        ) {
            onConflict = "user_id,device_id"
        }
    }

    suspend fun fetchDevices(userId: String): List<UserDevice> =
        client.from("user_devices").select {
            filter { eq("user_id", userId) }
            order("last_active_at", Order.DESCENDING)
        }.decodeList()

    /** Qurilmani hisobdan chiqaradi (sessiya server tomonda o'chiriladi). */
    suspend fun revokeDevice(deviceRowId: String) {
        client.postgrest.rpc(
            "revoke_device",
            buildJsonObject { put("p_device_id", deviceRowId) },
        )
    }
}
