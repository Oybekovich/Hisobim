package uz.hisobim.app.ui.theme

import androidx.compose.ui.unit.dp

/**
 * Spacing, radius va glass presetlari — `theme.ts` dagi `spacing`, `radius`, `glass`.
 */
object Spacing {
    val xs = 4.dp
    val sm = 6.dp
    val s = 9.dp
    val m = 12.dp
    val md = 13.dp
    val lg = 15.dp
    val xl = 18.dp
    val xxl = 22.dp
}

object Radius {
    val screen = 34.dp
    val hero = 19.dp
    val card = 17.dp
    val input = 16.dp
    val avatarBox = 11.dp
    val fab = 16.dp
    val segment = 14.dp
    val round = 999.dp
}

/**
 * Liquid Glass presetlari — yarim shaffof oq fonning shaffoflik darajasi.
 * RN'da BlurView intensity = blur*4; bu yerda dominant vizual bo'lgan oq qatlam
 * opacity bilan aniq takrorlanadi.
 */
enum class GlassVariant(val opacity: Float, val blur: Int) {
    // Kartalar va inputlar to'liq oq (fon tinti ko'rinmasligi + tezroq render uchun).
    Card(opacity = 1.0f, blur = 16),
    Input(opacity = 1.0f, blur = 16),
    Nav(opacity = 0.80f, blur = 22),
    Segment(opacity = 0.78f, blur = 16),
}
