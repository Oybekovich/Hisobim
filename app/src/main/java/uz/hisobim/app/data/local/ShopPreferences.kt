package uz.hisobim.app.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import uz.hisobim.app.data.model.Shop

private val Context.shopDataStore by preferencesDataStore(name = "hisobim_shop")

/**
 * Faol do'konni saqlovchi store — RN'dagi `shop.store.ts` (zustand persist, "hisobim-shop").
 */
class ShopPreferences(private val context: Context) {

    private val shopKey = stringPreferencesKey("active_shop")
    private val json = Json { ignoreUnknownKeys = true }

    val activeShop: Flow<Shop?> = context.shopDataStore.data.map { prefs ->
        prefs[shopKey]?.let { raw ->
            runCatching { json.decodeFromString(Shop.serializer(), raw) }.getOrNull()
        }
    }

    suspend fun setShop(shop: Shop?) {
        context.shopDataStore.edit { prefs ->
            if (shop == null) {
                prefs.remove(shopKey)
            } else {
                prefs[shopKey] = json.encodeToString(Shop.serializer(), shop)
            }
        }
    }
}
