package uz.hisobim.app.util

/**
 * `legacy-react-native/src/utils/validation.ts` (zod) ning ekvivalenti.
 * Har bir funksiya xato xabarini (String) yoki null (to'g'ri) qaytaradi.
 */
object Validation {

    private val EMAIL = Regex("^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,}$")
    private val PHONE = Regex("^\\+?[0-9\\s\\-()]*$")

    // ── login / register ──
    fun email(value: String): String? =
        if (EMAIL.matches(value)) null else "Email noto'g'ri formatda"

    fun password(value: String): String? =
        if (value.length >= 6) null else "Parol kamida 6 ta belgidan iborat bo'lishi kerak"

    fun confirmPassword(password: String, confirm: String): String? =
        if (password == confirm) null else "Parollar mos kelmadi"

    // ── add customer ──
    fun customerName(value: String): String? = when {
        value.trim().length < 2 -> "Ism kamida 2 ta belgidan iborat bo'lishi kerak"
        value.length > 100 -> "Ism 100 ta belgidan oshmasligi kerak"
        else -> null
    }

    fun customerAmountOptional(value: String): String? {
        if (value.isBlank()) return null
        val n = value.toDoubleOrNull()
        return if (n != null && n > 0) null else "Summa 0 dan katta bo'lishi kerak"
    }

    fun phoneOptional(value: String): String? {
        if (value.isEmpty()) return null
        return if (PHONE.matches(value)) null else "Telefon raqam noto'g'ri formatda"
    }

    fun noteOptional(value: String, max: Int = 500): String? =
        if (value.length > max) "Izoh $max ta belgidan oshmasligi kerak" else null

    // ── add debt ──
    fun debtAmount(value: String): String? = when {
        value.isEmpty() -> "Summani kiriting"
        (value.toDoubleOrNull()?.let { it > 0 }) != true -> "Summa 0 dan katta bo'lishi kerak"
        else -> null
    }

    fun debtDescriptionOptional(value: String): String? =
        if (value.length > 200) "Izoh 200 ta belgidan oshmasligi kerak" else null
}
