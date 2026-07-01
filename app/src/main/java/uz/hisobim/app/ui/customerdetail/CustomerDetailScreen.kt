package uz.hisobim.app.ui.customerdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import uz.hisobim.app.ui.components.AppIcons
import uz.hisobim.app.ui.components.ConfirmDialog
import uz.hisobim.app.ui.components.DebtItem
import uz.hisobim.app.ui.components.EmptyState
import uz.hisobim.app.ui.components.GlassBackground
import uz.hisobim.app.ui.components.HeroCard
import uz.hisobim.app.ui.theme.AppColors
import uz.hisobim.app.ui.theme.AppType
import uz.hisobim.app.ui.theme.BlueGradient
import uz.hisobim.app.ui.theme.Radius
import uz.hisobim.app.ui.theme.Spacing
import uz.hisobim.app.ui.theme.heroShadow
import uz.hisobim.app.util.formatAmount
import uz.hisobim.app.util.getInitials

@Composable
fun CustomerDetailScreen(
    onBack: () -> Unit,
    onEdit: (String) -> Unit,
    onAddDebt: (String) -> Unit,
    viewModel: CustomerDetailViewModel = viewModel(),
) {
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.refresh()
    }

    val customer = viewModel.customer.data
    val debts = viewModel.debts.data ?: emptyList()
    val customerLoading = viewModel.customer.loading
    val debtsLoading = viewModel.debts.loading

    var debtToDelete by remember { mutableStateOf<String?>(null) }
    var confirmDeleteCustomer by remember { mutableStateOf(false) }

    GlassBackground {
        Column(Modifier.fillMaxSize()) {
            // ── Header ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = Spacing.s, vertical = Spacing.s),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier.size(40.dp).clip(CircleShape).clickable(onClick = onBack),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(AppIcons.chevronBack, contentDescription = "Orqaga", tint = AppColors.accentBlue600, modifier = Modifier.size(26.dp))
                }

                if (customer != null) {
                    Box(
                        modifier = Modifier
                            .padding(start = Spacing.xs)
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(BlueGradient)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(getInitials(customer.name), style = AppType.label.copy(color = AppColors.white, fontSize = 13.sp))
                    }
                    Column(Modifier.padding(start = Spacing.s).weight(1f)) {
                        Text(customer.name, style = AppType.h2.copy(fontSize = 15.sp), maxLines = 1, overflow = TextOverflow.Ellipsis)
                        if (!customer.phone.isNullOrEmpty()) {
                            Text(customer.phone, style = AppType.caption, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                    }
                } else {
                    Spacer(Modifier.weight(1f))
                }

                if (customer != null) {
                    Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                        HeaderActionButton(AppIcons.pencilOutline, AppColors.accentBlue600) { onEdit(viewModel.id) }
                        HeaderActionButton(AppIcons.trashOutline, AppColors.debtRed) { confirmDeleteCustomer = true }
                    }
                }
            }

            when {
                customerLoading && customer == null -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = AppColors.accentBlue600)
                    }
                }

                customer == null -> Unit

                else -> {
                    // ── Qarz hero kartasi ──
                    HeroCard(
                        modifier = Modifier
                            .padding(start = Spacing.md, end = Spacing.md, top = Spacing.xxl, bottom = Spacing.md)
                            .fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = Spacing.xl, vertical = Spacing.xl),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text("JAMI QARZ", style = AppType.overline.copy(color = AppColors.white.copy(alpha = 0.75f)))
                            Text(
                                formatAmount(customer.totalDebt),
                                style = AppType.display.copy(color = AppColors.white, fontSize = 26.sp),
                                modifier = Modifier.padding(top = Spacing.sm),
                            )
                        }
                    }

                    // ── Ikki ustunli daftar ──
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(Spacing.s),
                    ) {
                        if (debts.isEmpty() && !debtsLoading) {
                            Box(Modifier.fillMaxWidth().height(280.dp)) {
                                EmptyState(
                                    message = "Hali yozuv yo'q",
                                    subMessage = "Quyidagi tugmani bosing",
                                    icon = AppIcons.documentTextOutline,
                                )
                            }
                        } else {
                            Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                                Column(Modifier.weight(1f)) {
                                    Text(
                                        "QARZ OLDI",
                                        style = AppType.overline.copy(color = AppColors.debtRed),
                                        modifier = Modifier.padding(bottom = Spacing.s, start = 2.dp),
                                    )
                                    debts.filter { it.amount > 0 }.forEach { debt ->
                                        DebtItem(debt = debt, onDelete = { debtToDelete = debt.id })
                                    }
                                }
                                Box(
                                    Modifier
                                        .padding(horizontal = Spacing.s)
                                        .width(1.dp)
                                        .fillMaxHeight()
                                        .background(AppColors.slate100.copy(alpha = 0.5f)),
                                )
                                Column(Modifier.weight(1f)) {
                                    Text(
                                        "TO'LOV QILDI",
                                        style = AppType.overline.copy(color = AppColors.paidGreen),
                                        modifier = Modifier.padding(bottom = Spacing.s, start = 2.dp),
                                    )
                                    debts.filter { it.amount < 0 }.forEach { debt ->
                                        DebtItem(debt = debt, onDelete = { debtToDelete = debt.id })
                                    }
                                }
                            }
                        }
                    }

                    // ── Yozuv qo'shish tugmasi ──
                    Box(
                        modifier = Modifier
                            .padding(Spacing.md)
                            .fillMaxWidth()
                            .height(52.dp)
                            .heroShadow(RoundedCornerShape(Radius.card))
                            .clip(RoundedCornerShape(Radius.card))
                            .background(Brush.linearGradient(BlueGradient))
                            .clickable { onAddDebt(viewModel.id) },
                        contentAlignment = Alignment.Center,
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
                        ) {
                            Icon(AppIcons.add, contentDescription = null, tint = AppColors.white, modifier = Modifier.size(20.dp))
                            Text("Yozuv qo'shish", style = AppType.h2.copy(color = AppColors.white, fontSize = 15.sp))
                        }
                    }
                }
            }
        }
    }

    ConfirmDialog(
        visible = debtToDelete != null,
        title = "Yozuvni o'chirish",
        message = "Bu yozuvni o'chirishni tasdiqlaysizmi?",
        confirmLabel = "O'chirish",
        onConfirm = {
            debtToDelete?.let { viewModel.deleteDebt(it) }
            debtToDelete = null
        },
        onDismiss = { debtToDelete = null },
    )

    ConfirmDialog(
        visible = confirmDeleteCustomer,
        title = "Mijozni o'chirish",
        message = "${customer?.name ?: ""} va barcha qarzlarini o'chirishni tasdiqlaysizmi?",
        confirmLabel = "O'chirish",
        onConfirm = {
            confirmDeleteCustomer = false
            viewModel.deleteCustomer(onBack)
        },
        onDismiss = { confirmDeleteCustomer = false },
    )
}

@Composable
private fun HeaderActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(Radius.avatarBox))
            .background(Color(0x99FFFFFF)) // rgba(255,255,255,0.6)
            .border(1.dp, Color(0xB3FFFFFF), RoundedCornerShape(Radius.avatarBox))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(19.dp))
    }
}
