package uz.hisobim.app.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Domen modellari — `legacy-react-native/src/types/index.ts` ning aniq ko'chirmasi.
 * Supabase jadval ustunlari snake_case, shuning uchun @SerialName ishlatilgan.
 */

@Serializable
data class Shop(
    val id: String,
    @SerialName("owner_id") val ownerId: String,
    val name: String,
    val phone: String? = null,
    val region: String? = null,
    val address: String? = null,
    @SerialName("is_active") val isActive: Boolean = true,
    @SerialName("created_at") val createdAt: String,
)

@Serializable
data class Customer(
    val id: String,
    @SerialName("shop_id") val shopId: String,
    val name: String,
    val phone: String? = null,
    val note: String? = null,
    @SerialName("total_debt") val totalDebt: Double = 0.0,
    @SerialName("created_at") val createdAt: String,
)

@Serializable
data class Debt(
    val id: String,
    @SerialName("shop_id") val shopId: String,
    @SerialName("customer_id") val customerId: String,
    val amount: Double, // musbat = qarz, manfiy = to'lov
    val description: String? = null,
    @SerialName("created_by") val createdBy: String,
    @SerialName("created_at") val createdAt: String,
)

enum class UserRole { owner, cashier, viewer }

// ── Insert / update payloadlari ─────────────────────────────────
@Serializable
data class CreateCustomerPayload(
    @SerialName("shop_id") val shopId: String,
    val name: String,
    val phone: String? = null,
    val note: String? = null,
)

@Serializable
data class UpdateCustomerPayload(
    val name: String,
    val phone: String? = null,
    val note: String? = null,
)

@Serializable
data class CreateDebtPayload(
    @SerialName("shop_id") val shopId: String,
    @SerialName("customer_id") val customerId: String,
    val amount: Double,
    val description: String? = null,
    @SerialName("created_by") val createdBy: String,
)

@Serializable
data class CreateShopPayload(
    @SerialName("owner_id") val ownerId: String,
    val name: String,
    val phone: String? = null,
    @SerialName("is_active") val isActive: Boolean = true,
)

@Serializable
data class UpdateShopNamePayload(val name: String)

// ── Kirgan qurilmalar (sessiyalar) ──────────────────────────────
@Serializable
data class UserDevice(
    val id: String,
    @SerialName("user_id") val userId: String,
    @SerialName("device_id") val deviceId: String,
    val model: String? = null,
    @SerialName("session_id") val sessionId: String? = null,
    @SerialName("created_at") val createdAt: String,
    @SerialName("last_active_at") val lastActiveAt: String,
    val revoked: Boolean = false,
)

@Serializable
data class UpsertDevicePayload(
    @SerialName("user_id") val userId: String,
    @SerialName("device_id") val deviceId: String,
    val model: String? = null,
    @SerialName("session_id") val sessionId: String? = null,
    @SerialName("last_active_at") val lastActiveAt: String,
    val revoked: Boolean = false,
)

// ── Admin panel ─────────────────────────────────────────────────
@Serializable
data class AdminAccount(
    @SerialName("user_id") val userId: String,
    val email: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("shop_id") val shopId: String? = null,
    @SerialName("shop_name") val shopName: String? = null,
    @SerialName("customer_count") val customerCount: Long = 0,
    @SerialName("total_debt") val totalDebt: Double = 0.0,
)

// ── Hisobot ─────────────────────────────────────────────────────
data class TopDebtor(
    val id: String,
    val name: String,
    val phone: String?,
    val totalDebt: Double,
)

data class ReportData(
    val totalDebt: Double,
    val customerCount: Int,
    val debtorCount: Int,
    val clearedCount: Int,
    val topDebtors: List<TopDebtor>,
)
