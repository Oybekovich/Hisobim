package uz.hisobim.app.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Locale
import kotlin.math.abs

/** `legacy-react-native/src/utils/format.ts` ning aniq ko'chirmasi. */

private val MONTHS = arrayOf(
    "Yan", "Feb", "Mar", "Apr", "May", "Iyn", "Iyl", "Avg", "Sen", "Okt", "Noy", "Dek",
)

private val amountFormat: DecimalFormat by lazy {
    val symbols = DecimalFormatSymbols(Locale.US).apply {
        groupingSeparator = ' ' // non-breaking space (ru-RU uslubi)
        decimalSeparator = '.'
    }
    DecimalFormat("#,##0.##", symbols)
}

fun formatAmount(amount: Double): String {
    val sign = if (amount < 0) "-" else ""
    return "$sign${amountFormat.format(abs(amount))} so'm"
}

/**
 * O'zbekiston telefon raqamini formatlaydi: "+998 99-666-88-99".
 * +998 avtomatik qo'shiladi, qolgan 9 raqam XX-XXX-XX-XX ko'rinishida ajratiladi.
 */
fun formatUzPhone(input: String): String {
    var digits = input.filter { it.isDigit() }
    if (digits.startsWith("998")) digits = digits.substring(3)
    digits = digits.take(9)
    if (digits.isEmpty()) return ""

    val parts = mutableListOf(digits.substring(0, minOf(2, digits.length)))
    if (digits.length > 2) parts.add(digits.substring(2, minOf(5, digits.length)))
    if (digits.length > 5) parts.add(digits.substring(5, minOf(7, digits.length)))
    if (digits.length > 7) parts.add(digits.substring(7, minOf(9, digits.length)))

    return "+998 ${parts.joinToString("-")}"
}

fun formatDate(isoString: String): String {
    val zoned: ZonedDateTime = runCatching {
        OffsetDateTime.parse(isoString).atZoneSameInstant(ZoneId.systemDefault())
    }.recoverCatching {
        Instant.parse(isoString).atZone(ZoneId.systemDefault())
    }.getOrElse {
        return isoString
    }

    val now = ZonedDateTime.now()
    val base = "${zoned.dayOfMonth} ${MONTHS[zoned.monthValue - 1]}"
    return if (zoned.year == now.year) base else "$base ${zoned.year}"
}

/** Raqamlarni har 3 xonada bo'sh joy bilan ajratadi: "600000" -> "600 000". */
fun groupThousands(digits: String): String {
    if (digits.isEmpty()) return ""
    return digits.reversed().chunked(3).joinToString(" ").reversed()
}
