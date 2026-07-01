package uz.hisobim.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import uz.hisobim.app.ui.theme.AppColors
import uz.hisobim.app.ui.theme.AppType
import uz.hisobim.app.ui.theme.Spacing

/** RN'dagi `EmptyState.tsx` ekvivalenti. */
@Composable
fun EmptyState(
    message: String,
    modifier: Modifier = Modifier,
    subMessage: String? = null,
    icon: ImageVector = AppIcons.peopleOutline,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .size(84.dp)
                .background(Color(0x8CFFFFFF), CircleShape) // rgba(255,255,255,0.55)
                .border(1.dp, Color(0xB3FFFFFF), CircleShape), // rgba(255,255,255,0.7)
            contentAlignment = Alignment.Center,
        ) {
            Icon(icon, contentDescription = null, tint = AppColors.slate300, modifier = Modifier.size(42.dp))
        }
        Text(
            message,
            style = AppType.h2.copy(color = AppColors.slate500),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = Spacing.xl),
        )
        if (subMessage != null) {
            Text(
                subMessage,
                style = AppType.caption,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = Spacing.sm),
            )
        }
    }
}
