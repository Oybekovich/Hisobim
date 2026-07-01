package uz.hisobim.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.hisobim.app.data.model.Customer
import uz.hisobim.app.ui.theme.AppColors
import uz.hisobim.app.ui.theme.AppType
import uz.hisobim.app.ui.theme.BlueGradient
import uz.hisobim.app.ui.theme.Radius
import uz.hisobim.app.ui.theme.Spacing
import uz.hisobim.app.util.formatAmount
import uz.hisobim.app.util.getInitials

/** RN'dagi `CustomerCard.tsx` ekvivalenti. Bosilganda mijoz sahifasini ochadi. */
@Composable
fun CustomerCard(customer: Customer, onClick: () -> Unit) {
    val hasDebt = customer.totalDebt > 0
    val initials = getInitials(customer.name)

    Box(Modifier.padding(horizontal = Spacing.md, vertical = Spacing.xs)) {
        GlassCard(modifier = Modifier.fillMaxWidth(), onClick = onClick) {
            Row(
                modifier = Modifier.padding(Spacing.m),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(BlueGradient)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(initials, style = AppType.label.copy(color = AppColors.white, fontSize = 13.sp))
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = Spacing.m),
                ) {
                    Text(
                        customer.name,
                        style = AppType.body,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    if (!customer.phone.isNullOrEmpty()) {
                        Text(
                            customer.phone,
                            style = AppType.caption,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(top = 2.dp),
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Text(
                        formatAmount(customer.totalDebt),
                        style = AppType.amount.copy(
                            color = if (hasDebt) AppColors.debtRed else AppColors.slate300,
                        ),
                    )
                    Icon(
                        AppIcons.chevronForward,
                        contentDescription = null,
                        tint = AppColors.slate200,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
        }
    }
}
