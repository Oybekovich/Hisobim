package uz.hisobim.app.data.remote

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import io.github.jan.supabase.auth.SessionManager
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json

private val Context.authDataStore by preferencesDataStore(name = "hisobim_auth")

/**
 * Supabase sessiyasini DataStore'da saqlovchi SessionManager.
 * RN'dagi `persist` + AsyncStorage ("hisobim-auth") ning ekvivalenti.
 */
class DataStoreSessionManager(private val context: Context) : SessionManager {

    private val sessionKey = stringPreferencesKey("session")
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    override suspend fun saveSession(session: UserSession) {
        context.authDataStore.edit { prefs ->
            prefs[sessionKey] = json.encodeToString(UserSession.serializer(), session)
        }
    }

    override suspend fun loadSession(): UserSession? {
        val raw = context.authDataStore.data.first()[sessionKey] ?: return null
        return runCatching { json.decodeFromString(UserSession.serializer(), raw) }.getOrNull()
    }

    override suspend fun deleteSession() {
        context.authDataStore.edit { prefs -> prefs.remove(sessionKey) }
    }
}
