package uz.hisobim.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import uz.hisobim.app.ui.theme.AppColors

/**
 * Liquid Glass fon — yumshoq gradient + ikkita mayin rangli dog' (ko'k + binafsha).
 * RN'dagi `GlassBackground.tsx` ekvivalenti.
 */
@Composable
fun GlassBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val gradient = remember { Brush.verticalGradient(listOf(AppColors.bgTop, AppColors.bgBottom)) }
    Box(
        modifier
            .fillMaxSize()
            .clipToBounds()
            .background(gradient),
    ) {
        // Ko'k mayin dog' (yuqori o'ng) — alfa rangga singdirilgan (offscreen qatlamsiz).
        Box(
            Modifier
                .size(320.dp)
                .align(Alignment.TopEnd)
                .offset(x = 90.dp, y = (-120).dp)
                .background(Color(0x174A86E4), CircleShape),
        )
        // Binafsha mayin dog' (pastki chap)
        Box(
            Modifier
                .size(320.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-100).dp, y = 110.dp)
                .background(Color(0x127A6ED6), CircleShape),
        )
        content()
    }
}
