package uz.hisobim.app.data.repo

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import uz.hisobim.app.data.model.CreateDebtPayload
import uz.hisobim.app.data.model.Debt
import uz.hisobim.app.data.remote.SupabaseProvider

/** RN'dagi `src/services/debt.service.ts` ekvivalenti. */
class DebtRepository(
    private val client: SupabaseClient = SupabaseProvider.client,
) {
    suspend fun fetchDebtsByCustomer(customerId: String): List<Debt> =
        client.from("debts").select {
            filter { eq("customer_id", customerId) }
            order("created_at", Order.DESCENDING)
        }.decodeList()

    suspend fun createDebt(payload: CreateDebtPayload): Debt =
        client.from("debts").insert(payload) {
            select()
        }.decodeSingle()

    suspend fun deleteDebt(id: String) {
        client.from("debts").delete {
            filter { eq("id", id) }
        }
    }
}
