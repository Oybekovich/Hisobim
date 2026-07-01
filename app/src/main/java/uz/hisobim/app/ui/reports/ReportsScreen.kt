package uz.hisobim.app.ui.reports

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import uz.hisobim.app.ui.AppViewModel
import uz.hisobim.app.ui.components.AppIcons
import uz.hisobim.app.ui.components.EmptyState
import uz.hisobim.app.ui.components.GlassBackground
import uz.hisobim.app.ui.components.GlassCard
import uz.hisobim.app.ui.components.HeroCard
import uz.hisobim.app.ui.theme.AppColors
import uz.hisobim.app.ui.theme.AppType
import uz.hisobim.app.ui.theme.BlueGradient
import uz.hisobim.app.ui.theme.Spacing
import uz.hisobim.app.util.formatAmount
import uz.hisobim.app.util.getInitials

@Composable
fun ReportsScreen(
    appViewModel: AppViewModel,
    onOpenCustomer: (String) -> Unit,
    viewModel: ReportsViewModel = viewModel(),
) {
    val shop by appViewModel.activeShop.collectAsStateWithLifecycle()

    LaunchedEffect(shop?.id) {
        shop?.id?.let { viewModel.setShopId(it); viewModel.refresh() }
    }
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.refresh()
    }

    val data = viewModel.state.data

    if (viewModel.state.loading && data == null) {
        GlassBackground {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AppColors.accentBlue600)
            }
        }
        return
    }

    val hasDebtors = (data?.topDebtors?.size ?: 0) > 0

    GlassBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(start = Spacing.md, end = Spacing.md, top = Spacing.md, bottom = 90.dp),
        ) {
            // ── Umumiy qarzdorlik (hero) ──
            HeroCard(modifier = Modifier.fillMaxWidth().padding(bottom = Spacing.m)) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(Spacing.xxl),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("UMUMIY QARZDORLIK", style = AppType.overline.copy(color = AppColors.white.copy(alpha = 0.75f)))
                    Text(
                        formatAmount(data?.totalDebt ?: 0.0),
                        style = AppType.display.copy(color = AppColors.white, fontSize = 30.sp),
                        modifier = Modifier.padding(top = Spacing.s),
                    )
                    Text(
                        "${data?.debtorCount ?: 0} ta mijoz qarzdor",
                        style = AppType.caption.copy(color = AppColors.white.copy(alpha = 0.7f)),
                        modifier = Modifier.padding(top = Spacing.s),
                    )
                }
            }

            // ── Statistika paneli ──
            GlassCard(modifier = Modifier.fillMaxWidth().padding(bottom = Spacing.m)) {
                Row(
                    modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min).padding(Spacing.lg),
                ) {
                    StatBox(value = "${data?.customerCount ?: 0}", label = "Jami mijoz", color = AppColors.ink900)
                    StatDivider()
                    StatBox(value = "${data?.debtorCount ?: 0}", label = "Qarzdor", color = AppColors.debtRed)
                    StatDivider()
                    StatBox(value = "${data?.clearedCount ?: 0}", label = "To'lagan", color = AppColors.paidNeutral)
                }
            }

            // ── Eng ko'p qarzdorlar ──
            Text(
                "ENG KO'P QARZDORLAR",
                style = AppType.overline,
                modifier = Modifier.padding(bottom = Spacing.s, start = Spacing.xs),
            )
            if (hasDebtors && data != null) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        data.topDebtors.forEachIndexed { index, customer ->
                            if (index > 0) {
                                Box(
                                    Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .background(androidx.compose.ui.graphics.Color(0x66CBD1DC)), // rgba(203,209,220,0.4)
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onOpenCustomer(customer.id) }
                                    .padding(horizontal = Spacing.md, vertical = Spacing.m),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    "${index + 1}",
                                    style = AppType.amount.copy(color = AppColors.slate100),
                                    modifier = Modifier.width(18.dp).padding(end = Spacing.s),
                                )
                                Box(
                                    modifier = Modifier
                                        .padding(end = Spacing.s)
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Brush.linearGradient(BlueGradient)),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(getInitials(customer.name), style = AppType.label.copy(color = AppColors.white))
                                }
                                Column(Modifier.weight(1f)) {
                                    Text(customer.name, style = AppType.body, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                    if (!customer.phone.isNullOrEmpty()) {
                                        Text(customer.phone, style = AppType.caption, modifier = Modifier.padding(top = 1.dp))
                                    }
                                }
                                Text(formatAmount(customer.totalDebt), style = AppType.amount.copy(color = AppColors.debtRed))
                                Icon(
                                    AppIcons.chevronForward,
                                    contentDescription = null,
                                    tint = AppColors.slate200,
                                    modifier = Modifier.padding(start = 4.dp).size(16.dp),
                                )
                            }
                        }
                    }
                }
            } else {
                EmptyState(
                    message = "Qarzdor mijozlar yo'q",
                    subMessage = "Barcha mijozlar hisobini tozalagan",
                    icon = AppIcons.checkmarkCircleOutline,
                )
            }
        }
    }
}

@Composable
private fun androidx.compose.foundation.layout.RowScope.StatBox(value: String, label: String, color: androidx.compose.ui.graphics.Color) {
    Column(
        modifier = Modifier.weight(1f).padding(vertical = Spacing.xs),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(value, style = AppType.display.copy(fontSize = 24.sp, color = color))
        Text(label, style = AppType.caption, textAlign = TextAlign.Center, modifier = Modifier.padding(top = Spacing.xs))
    }
}

@Composable
private fun StatDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .fillMaxHeight()
            .background(AppColors.slate100.copy(alpha = 0.6f)),
    )
}
