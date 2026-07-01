package uz.hisobim.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.hisobim.app.data.model.Debt
import uz.hisobim.app.ui.theme.AppColors
import uz.hisobim.app.ui.theme.AppType
import uz.hisobim.app.ui.theme.Radius
import uz.hisobim.app.ui.theme.Spacing
import uz.hisobim.app.util.formatAmount
import uz.hisobim.app.util.formatDate
import kotlin.math.abs

/** RN'dagi `DebtItem.tsx` ekvivalenti. */
@Composable
fun DebtItem(debt: Debt, onDelete: () -> Unit) {
    val isPayment = debt.amount < 0
    val amountColor = if (isPayment) AppColors.ink600 else AppColors.debtRed

    Box(Modifier.padding(bottom = Spacing.sm)) {
        GlassCard(shape = RoundedCornerShape(Radius.avatarBox), shadow = false) {
            Column(Modifier.padding(Spacing.s)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        formatAmount(abs(debt.amount)),
                        style = AppType.amount.copy(color = amountColor),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f).padding(end = 2.dp),
                    )
                    Icon(
                        AppIcons.close,
                        contentDescription = "O'chirish",
                        tint = AppColors.slate300,
                        modifier = Modifier
                            .clickable(onClick = onDelete)
                            .padding(2.dp)
                            .size(14.dp),
                    )
                }
                if (!debt.description.isNullOrEmpty()) {
                    Text(
                        debt.description,
                        style = AppType.caption.copy(color = AppColors.slate500),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(bottom = 1.dp),
                    )
                }
                Text(
                    formatDate(debt.createdAt),
                    style = AppType.caption.copy(fontSize = 10.sp, color = AppColors.slate400),
                )
            }
        }
    }
}
