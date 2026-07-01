package uz.hisobim.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import uz.hisobim.app.ui.theme.BlueGradient
import uz.hisobim.app.ui.theme.Radius
import uz.hisobim.app.ui.theme.heroShadow

/**
 * Hero karta — to'liq ko'k gradient + yumaloq yorug' dog'.
 * RN'dagi `HeroCard.tsx` ekvivalenti. Har ekranda FAQAT bitta hero.
 */
@Composable
fun HeroCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(Radius.hero),
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier
            .heroShadow(shape)
            .clip(shape)
            .background(
                Brush.linearGradient(
                    colors = BlueGradient,
                    start = Offset(0f, 0f),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
                ),
            ),
    ) {
        // Radial yorug' dog' imitatsiyasi — matchParentSize overlay ichida,
        // shunda dog'ning 180dp o'lchami kartaning balandligini cho'zmaydi.
        Box(Modifier.matchParentSize()) {
            Box(
                Modifier
                    .size(180.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 40.dp, y = (-60).dp)
                    .background(Color(0x29FFFFFF), CircleShape),
            )
        }
        // Yuqori qirradagi inset highlight — rgba(255,255,255,0.5)
        Box(
            Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0x80FFFFFF)),
        )
        content()
    }
}
