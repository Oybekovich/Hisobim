package uz.hisobim.app.data.repo

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.flow.StateFlow
import uz.hisobim.app.data.remote.SupabaseProvider

/** RN'dagi `src/services/auth.service.ts` ekvivalenti. */
class AuthRepository(
    private val client: SupabaseClient = SupabaseProvider.client,
) {
    companion object {
        /** Admin login: foydalanuvchi "admin" yozadi, shu emailга bog'lanadi. */
        const val ADMIN_LOGIN = "admin"
        const val ADMIN_EMAIL = "admin@hisobim.uz"
    }

    val sessionStatus: StateFlow<SessionStatus> get() = client.auth.sessionStatus

    fun currentSession(): UserSession? = client.auth.currentSessionOrNull()

    fun currentUser(): UserInfo? = client.auth.currentUserOrNull()

    suspend fun signUp(email: String, password: String) {
        client.auth.signUpWith(Email) {
            this.email = email
            this.password = password
        }
    }

    suspend fun signIn(email: String, password: String) {
        client.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
    }

    suspend fun signOut() {
        client.auth.signOut()
    }
}
