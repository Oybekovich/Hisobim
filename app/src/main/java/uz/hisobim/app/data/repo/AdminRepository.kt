package uz.hisobim.app.data.repo

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import uz.hisobim.app.data.model.AdminAccount
import uz.hisobim.app.data.remote.SupabaseProvider

/**
 * Admin panel amallari — barchasi `admin_*` SECURITY DEFINER RPC orqali.
 * Server tomonda har bir chaqiruv is_admin() bilan tekshiriladi.
 */
class AdminRepository(
    private val client: SupabaseClient = SupabaseProvider.client,
) {
    suspend fun listAccounts(): List<AdminAccount> =
        client.postgrest.rpc("admin_list_accounts").decodeList()

    suspend fun deleteUser(userId: String) {
        client.postgrest.rpc("admin_delete_user", buildJsonObject { put("p_user", userId) })
    }

    suspend fun updateEmail(userId: String, email: String) {
        client.postgrest.rpc(
            "admin_update_email",
            buildJsonObject { put("p_user", userId); put("p_email", email) },
        )
    }

    suspend fun updatePassword(userId: String, password: String) {
        client.postgrest.rpc(
            "admin_update_password",
            buildJsonObject { put("p_user", userId); put("p_password", password) },
        )
    }
}
