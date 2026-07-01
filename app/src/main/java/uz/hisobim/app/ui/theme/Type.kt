package uz.hisobim.app.ui.theme

import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import uz.hisobim.app.R

/**
 * Manrope (variable font) — 5 og'irlik FontVariation orqali.
 * RN'dagi `@expo-google-fonts/manrope` Regular/Medium/SemiBold/Bold/ExtraBold ekvivalenti.
 */
@OptIn(ExperimentalTextApi::class)
private fun manropeFont(weight: Int) = Font(
    resId = R.font.manrope,
    weight = FontWeight(weight),
    style = FontStyle.Normal,
    variationSettings = FontVariation.Settings(FontVariation.weight(weight)),
)

val Manrope = FontFamily(
    manropeFont(400),
    manropeFont(500),
    manropeFont(600),
    manropeFont(700),
    manropeFont(800),
)

object AppWeight {
    val regular = FontWeight.W400
    val medium = FontWeight.W500
    val semibold = FontWeight.W600
    val bold = FontWeight.W700
    val extrabold = FontWeight.W800
}

/**
 * Tipografika presetlari — `theme.ts` dagi `type` obyekti.
 * Rang har bir uslubga kiritilgan (RN'dagidek).
 */
object AppType {
    val display = TextStyle(
        fontFamily = Manrope, fontWeight = AppWeight.extrabold,
        fontSize = 25.sp, letterSpacing = (-0.5).sp, color = AppColors.ink900,
    )
    val h1 = TextStyle(
        fontFamily = Manrope, fontWeight = AppWeight.extrabold,
        fontSize = 18.sp, letterSpacing = (-0.4).sp, color = AppColors.ink900,
    )
    val h2 = TextStyle(
        fontFamily = Manrope, fontWeight = AppWeight.bold,
        fontSize = 16.sp, letterSpacing = (-0.3).sp, color = AppColors.ink900,
    )
    val body = TextStyle(
        fontFamily = Manrope, fontWeight = AppWeight.bold,
        fontSize = 14.sp, color = AppColors.ink900,
    )
    val amount = TextStyle(
        fontFamily = Manrope, fontWeight = AppWeight.extrabold,
        fontSize = 13.sp, color = AppColors.ink900,
    )
    val label = TextStyle(
        fontFamily = Manrope, fontWeight = AppWeight.semibold,
        fontSize = 12.sp, letterSpacing = 0.2.sp, color = AppColors.slate500,
    )
    val caption = TextStyle(
        fontFamily = Manrope, fontWeight = AppWeight.semibold,
        fontSize = 11.sp, color = AppColors.slate400,
    )
    val overline = TextStyle(
        fontFamily = Manrope, fontWeight = AppWeight.extrabold,
        fontSize = 10.sp, letterSpacing = 1.sp, color = AppColors.slate500,
    )
    val nav = TextStyle(
        fontFamily = Manrope, fontWeight = AppWeight.semibold,
        fontSize = 9.sp,
    )
}
