package uz.hisobim.app.data.repo

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import uz.hisobim.app.data.model.CreateShopPayload
import uz.hisobim.app.data.model.Shop
import uz.hisobim.app.data.model.UpdateShopNamePayload
import uz.hisobim.app.data.remote.SupabaseProvider

/** RN'dagi `src/services/shop.service.ts` ekvivalenti. */
class ShopRepository(
    private val client: SupabaseClient = SupabaseProvider.client,
) {
    suspend fun updateShopName(shopId: String, name: String): Shop =
        client.from("shops").update(UpdateShopNamePayload(name)) {
            select()
            filter { eq("id", shopId) }
        }.decodeSingle()

    suspend fun fetchOrCreateShop(ownerId: String, phone: String?): Shop {
        val existing = client.from("shops").select {
            filter { eq("owner_id", ownerId) }
            limit(1)
        }.decodeList<Shop>().firstOrNull()

        if (existing != null) return existing

        return client.from("shops").insert(
            CreateShopPayload(
                ownerId = ownerId,
                name = "Mening do'konim",
                phone = phone,
                isActive = true,
            )
        ) {
            select()
        }.decodeSingle()
    }
}
