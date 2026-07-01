package uz.hisobim.app.data.repo

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import uz.hisobim.app.data.model.Customer
import uz.hisobim.app.data.model.ReportData
import uz.hisobim.app.data.model.TopDebtor
import uz.hisobim.app.data.remote.SupabaseProvider

/** RN'dagi `src/services/reports.service.ts` ekvivalenti. */
class ReportsRepository(
    private val client: SupabaseClient = SupabaseProvider.client,
) {
    suspend fun fetchReports(shopId: String): ReportData {
        val customers = client.from("customers").select {
            filter { eq("shop_id", shopId) }
        }.decodeList<Customer>()

        val totalDebt = customers.sumOf { it.totalDebt }
        val topDebtors = customers
            .filter { it.totalDebt > 0 }
            .sortedByDescending { it.totalDebt }
            .take(5)
            .map { TopDebtor(it.id, it.name, it.phone, it.totalDebt) }

        return ReportData(
            totalDebt = totalDebt,
            customerCount = customers.size,
            debtorCount = customers.count { it.totalDebt > 0 },
            clearedCount = customers.count { it.totalDebt <= 0 },
            topDebtors = topDebtors,
        )
    }
}
