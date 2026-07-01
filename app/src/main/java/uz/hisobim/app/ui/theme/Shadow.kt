package uz.hisobim.app.ui.theme

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Yumshoq ko'kimtir soya — `theme.ts` dagi `softShadow`.
 * (Compose soyasi elevation bilan boshqariladi; rang API 28+ da qo'llanadi.)
 */
private val SoftShadowColor = Color(0xFF284682) // rgba(40,70,130)

fun Modifier.softShadow(shape: Shape, elevation: Dp = 7.dp): Modifier = this.shadow(
    elevation = elevation,
    shape = shape,
    clip = false,
    ambientColor = SoftShadowColor,
    spotColor = SoftShadowColor,
)

/** Kuchli ko'k soya — `theme.ts` dagi `heroShadow`. */
fun Modifier.heroShadow(shape: Shape, elevation: Dp = 14.dp): Modifier = this.shadow(
    elevation = elevation,
    shape = shape,
    clip = false,
    ambientColor = AppColors.accentBlue700,
    spotColor = AppColors.accentBlue700,
)
