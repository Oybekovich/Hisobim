package uz.hisobim.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import uz.hisobim.app.ui.theme.AppColors
import uz.hisobim.app.ui.theme.AppType
import uz.hisobim.app.ui.theme.AppWeight
import uz.hisobim.app.ui.theme.BlueGradient
import uz.hisobim.app.ui.theme.Radius
import uz.hisobim.app.ui.theme.RedGradient
import uz.hisobim.app.ui.theme.Spacing

/** RN'dagi `ConfirmDialog.tsx` ekvivalenti. */
@Composable
fun ConfirmDialog(
    visible: Boolean,
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    confirmLabel: String = "O'chirish",
    cancelLabel: String = "Bekor qilish",
    destructive: Boolean = true,
    icon: ImageVector? = null,
) {
    if (!visible) return

    val accent = if (destructive) AppColors.debtRed else AppColors.accentBlue600
    val iconVector = icon ?: if (destructive) AppIcons.trashOutline else AppIcons.helpCircleOutline

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = Spacing.xl)
                .widthIn(max = 360.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(Radius.hero))
                .background(AppColors.white)
                .padding(
                    start = Spacing.xl,
                    end = Spacing.xl,
                    top = Spacing.xl,
                    bottom = Spacing.lg,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Tepadagi ikonka
            Box(
                modifier = Modifier
                    .padding(bottom = Spacing.m)
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(accent.copy(alpha = 0.122f)), // ${accent}1f
                contentAlignment = Alignment.Center,
            ) {
                Icon(iconVector, contentDescription = null, tint = accent, modifier = Modifier.size(26.dp))
            }

            Text(
                title,
                style = AppType.h1,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = Spacing.sm),
            )
            Text(
                message,
                style = AppType.label.copy(
                    fontWeight = AppWeight.bold,
                    color = AppColors.slate500,
                    lineHeight = 20.sp,
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = Spacing.xl),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.m),
            ) {
                // Bekor
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .clip(RoundedCornerShape(Radius.card))
                        .background(Color(0x1A6B7387)) // rgba(107,115,135,0.10)
                        .clickable(onClick = onDismiss),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(cancelLabel, style = AppType.h2.copy(fontSize = 15.sp, color = AppColors.ink700))
                }
                // Tasdiqlash
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .clip(RoundedCornerShape(Radius.card))
                        .background(Brush.linearGradient(if (destructive) RedGradient else BlueGradient))
                        .clickable(onClick = onConfirm),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(confirmLabel, style = AppType.h2.copy(fontSize = 15.sp, color = AppColors.white))
                }
            }
        }
    }
}
