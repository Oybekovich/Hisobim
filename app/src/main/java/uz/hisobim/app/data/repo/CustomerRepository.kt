package uz.hisobim.app.data.repo

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import uz.hisobim.app.data.model.CreateCustomerPayload
import uz.hisobim.app.data.model.Customer
import uz.hisobim.app.data.model.UpdateCustomerPayload
import uz.hisobim.app.data.remote.SupabaseProvider

/** RN'dagi `src/services/customer.service.ts` ekvivalenti. */
class CustomerRepository(
    private val client: SupabaseClient = SupabaseProvider.client,
) {
    suspend fun fetchCustomers(shopId: String): List<Customer> =
        client.from("customers").select {
            filter { eq("shop_id", shopId) }
            order("name", Order.ASCENDING)
        }.decodeList()

    suspend fun fetchCustomerById(id: String): Customer =
        client.from("customers").select {
            filter { eq("id", id) }
        }.decodeSingle()

    suspend fun createCustomer(payload: CreateCustomerPayload): Customer =
        client.from("customers").insert(payload) {
            select()
        }.decodeSingle()

    suspend fun updateCustomer(id: String, payload: UpdateCustomerPayload): Customer =
        client.from("customers").update(payload) {
            select()
            filter { eq("id", id) }
        }.decodeSingle()

    suspend fun deleteCustomer(id: String) {
        client.from("customers").delete {
            filter { eq("id", id) }
        }
    }

    suspend fun searchCustomers(shopId: String, query: String): List<Customer> =
        client.from("customers").select {
            filter {
                eq("shop_id", shopId)
                or {
                    ilike("name", "%$query%")
                    ilike("phone", "%$query%")
                }
            }
            order("name", Order.ASCENDING)
            limit(50)
        }.decodeList()
}
