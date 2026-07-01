package uz.hisobim.app.ui.customers

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import uz.hisobim.app.ui.AppViewModel
import uz.hisobim.app.ui.components.AppIcons
import uz.hisobim.app.ui.components.CustomerCard
import uz.hisobim.app.ui.components.EmptyState
import uz.hisobim.app.ui.components.GlassBackground
import uz.hisobim.app.ui.components.SearchBar
import uz.hisobim.app.ui.theme.AppColors
import uz.hisobim.app.ui.theme.AppType
import uz.hisobim.app.ui.theme.BlueGradient
import uz.hisobim.app.ui.theme.Radius
import uz.hisobim.app.ui.theme.Spacing
import uz.hisobim.app.ui.theme.heroShadow
import uz.hisobim.app.util.formatAmount

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomersScreen(
    appViewModel: AppViewModel,
    onOpenCustomer: (String) -> Unit,
    onAddCustomer: () -> Unit,
    viewModel: CustomersViewModel = viewModel(),
) {
    val shop by appViewModel.activeShop.collectAsStateWithLifecycle()

    LaunchedEffect(shop) {
        if (shop == null) appViewModel.ensureShop()
    }
    LaunchedEffect(shop?.id) {
        shop?.id?.let { viewModel.setShopId(it); viewModel.refresh() }
    }
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.refresh()
    }

    var query by rememberSaveable { mutableStateOf("") }
    val currentShop = shop

    if (currentShop == null) {
        GlassBackground {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AppColors.accentBlue600)
            }
        }
        return
    }

    val customers = viewModel.state.data ?: emptyList()
    val filtered = customers.filter {
        it.name.lowercase().contains(query.lowercase()) || (it.phone ?: "").contains(query)
    }
    val totalDebt = customers.sumOf { it.totalDebt }

    GlassBackground {
        Column(Modifier.fillMaxSize().statusBarsPadding()) {
            // ── Do'kon sarlavhasi ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = Spacing.lg, end = Spacing.lg, top = Spacing.s, bottom = Spacing.m),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
            ) {
                Column(Modifier.weight(1f).padding(end = Spacing.m)) {
                    Text(currentShop.name, style = AppType.h1, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(
                        "${customers.size} ta mijoz",
                        style = AppType.caption,
                        modifier = Modifier.padding(top = 2.dp),
                    )
                }
                if (totalDebt > 0) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text("JAMI QARZ", style = AppType.overline.copy(color = AppColors.slate400))
                        Text(
                            formatAmount(totalDebt),
                            style = AppType.amount.copy(color = AppColors.debtRed, fontSize = 15.sp),
                            modifier = Modifier.padding(top = 2.dp),
                        )
                    }
                }
            }

            SearchBar(value = query, onValueChange = { query = it })

            PullToRefreshBox(
                isRefreshing = viewModel.state.loading && viewModel.state.data != null,
                onRefresh = viewModel::refresh,
                modifier = Modifier.weight(1f).fillMaxWidth(),
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = Spacing.xs, bottom = 100.dp),
                ) {
                    if (filtered.isEmpty()) {
                        item {
                            Box(Modifier.fillParentMaxSize()) {
                                EmptyState(
                                    message = if (query.isNotEmpty()) "Hech narsa topilmadi" else "Hali mijoz yo'q",
                                    subMessage = if (query.isNotEmpty()) null else "Qo'shish uchun + tugmasini bosing",
                                    icon = if (query.isNotEmpty()) AppIcons.searchOutline else AppIcons.peopleOutline,
                                )
                            }
                        }
                    } else {
                        items(filtered, key = { it.id }) { customer ->
                            CustomerCard(customer = customer, onClick = { onOpenCustomer(customer.id) })
                        }
                    }
                }
            }
        }

        // ── FAB ──
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = Spacing.lg, bottom = 100.dp)
                .size(63.dp)
                .heroShadow(RoundedCornerShape(Radius.fab))
                .clip(RoundedCornerShape(Radius.fab))
                .background(Brush.linearGradient(BlueGradient))
                .clickable(onClick = onAddCustomer),
            contentAlignment = Alignment.Center,
        ) {
            Icon(AppIcons.add, contentDescription = "Mijoz qo'shish", tint = AppColors.white, modifier = Modifier.size(36.dp))
        }
    }
}
