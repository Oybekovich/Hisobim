package uz.hisobim.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import uz.hisobim.app.ui.theme.AppColors
import uz.hisobim.app.ui.theme.GlassVariant
import uz.hisobim.app.ui.theme.Radius
import uz.hisobim.app.ui.theme.softShadow

/**
 * Liquid Glass karta — yarim shaffof oq + nozik oq qirra + yuqori yorug' chiziq.
 * RN'dagi `GlassCard.tsx` ekvivalenti (BlurView o'rniga dominant oq qatlam opacity bilan).
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    variant: GlassVariant = GlassVariant.Card,
    shape: Shape = RoundedCornerShape(Radius.card),
    shadow: Boolean = true,
    borderColor: Color = AppColors.glassBorder,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    val withShadow = if (shadow) modifier.softShadow(shape) else modifier
    Box(
        withShadow
            .clip(shape)
            .background(AppColors.white.copy(alpha = variant.opacity))
            .border(1.dp, borderColor, shape)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
    ) {
        // Yuqori qirradagi yorug' chiziq (shisha effekti) — rgba(255,255,255,0.85)
        Box(
            Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0xD9FFFFFF)),
        )
        content()
    }
}
