package uz.hisobim.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val HisobimColorScheme = lightColorScheme(
    primary = AppColors.accentBlue600,
    secondary = AppColors.accentBlue500,
    error = AppColors.debtRed,
    background = AppColors.bgTop,
    surface = AppColors.white,
    onPrimary = AppColors.white,
    onSecondary = AppColors.white,
    onBackground = AppColors.ink900,
    onSurface = AppColors.ink900,
)

private val HisobimTypography = Typography(
    bodyLarge = AppType.body,
    bodyMedium = AppType.body,
    titleMedium = AppType.h2,
    labelLarge = AppType.label,
)

@Composable
fun HisobimTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = HisobimColorScheme,
        typography = HisobimTypography,
        content = content,
    )
}
