package uz.hisobim.app.data.remote

import android.content.Context
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import uz.hisobim.app.BuildConfig

/**
 * Yagona Supabase mijozi — `legacy-react-native/src/lib/supabase.ts` ekvivalenti.
 * Sessiya DataStore orqali saqlanadi (RN'dagi AsyncStorage o'rnida).
 */
object SupabaseProvider {

    @Volatile
    private var instance: SupabaseClient? = null

    val client: SupabaseClient
        get() = instance ?: error("SupabaseProvider.init() chaqirilmagan")

    fun init(context: Context) {
        if (instance != null) return
        instance = createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_ANON_KEY,
        ) {
            install(Auth) {
                sessionManager = DataStoreSessionManager(context.applicationContext)
                alwaysAutoRefresh = true
                autoLoadFromStorage = true
            }
            install(Postgrest)
        }
    }
}
