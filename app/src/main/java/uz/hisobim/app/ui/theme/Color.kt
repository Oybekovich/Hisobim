package uz.hisobim.app.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Hisobim — Liquid Glass rang palitrasi.
 * `legacy-react-native/src/theme.ts` dagi `colors` ning aniq ko'chirmasi.
 */
object AppColors {
    // Brand (ko'k urg'u)
    val accentBlue600 = Color(0xFF2F6FD6)
    val accentBlue500 = Color(0xFF4A86E4)
    val accentBlue700 = Color(0xFF2A62C4)
    val accentBlueDeep = Color(0xFF2A4FB0)

    // Neytral (ink & gray)
    val ink900 = Color(0xFF1A2235)
    val ink700 = Color(0xFF27304A)
    val ink600 = Color(0xFF3A4258)
    val slate500 = Color(0xFF6B7387)
    val slate400 = Color(0xFF8B93A5)
    val slate300 = Color(0xFFA8AEBC)
    val slate200 = Color(0xFFAEB4C2)
    val slate100 = Color(0xFFCBD1DC)

    // Semantik
    val debtRed = Color(0xFFC0504C)      // FAQAT qarz summasi (mayin terakota)
    val paidGreen = Color(0xFF5A8F6B)    // juda kam ishlatiladi
    val paidNeutral = Color(0xFFB6BCC8)  // "to'lagan" — toza, neytral

    // Fon
    val bgTop = Color(0xFFF4F7FC)
    val bgBottom = Color(0xFFFBFCFE)
    val pageTop = Color(0xFFF3F5F9)
    val pageBottom = Color(0xFFECEFF4)

    val white = Color(0xFFFFFFFF)

    // Shisha qirrasidagi nozik oq chegara — rgba(255,255,255,0.72)
    val glassBorder = Color(0xB8FFFFFF)
}

// Ko'k gradient (hero, asosiy tugma)
val BlueGradient = listOf(AppColors.accentBlue500, AppColors.accentBlue600, AppColors.accentBlue700)
val DeepBlueGradient = listOf(AppColors.accentBlue600, AppColors.accentBlueDeep)

// Qizil (o'chirish) gradienti — ConfirmDialog'dagi redGradient
val RedGradient = listOf(Color(0xFFD4625E), AppColors.debtRed)
