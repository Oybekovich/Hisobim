package uz.hisobim.app.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import java.util.UUID

private val Context.deviceDataStore by preferencesDataStore(name = "hisobim_device")

/**
 * Barqaror, per-install qurilma identifikatori.
 * Bir marta generatsiya qilinib DataStore'da saqlanadi (ANDROID_ID'ga qaramaydi).
 */
class DeviceIdProvider(private val context: Context) {

    private val key = stringPreferencesKey("device_id")

    suspend fun get(): String {
        context.deviceDataStore.data.first()[key]?.let { return it }
        val generated = UUID.randomUUID().toString()
        context.deviceDataStore.edit { it[key] = generated }
        return generated
    }
}
