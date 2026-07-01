package uz.hisobim.app.ui.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import uz.hisobim.app.ui.AppViewModel
import uz.hisobim.app.ui.components.CustomerCard
import uz.hisobim.app.ui.components.EmptyState
import uz.hisobim.app.ui.components.GlassBackground
import uz.hisobim.app.ui.components.SearchBar
import uz.hisobim.app.ui.theme.Spacing

@Composable
fun SearchScreen(
    appViewModel: AppViewModel,
    onOpenCustomer: (String) -> Unit,
    viewModel: SearchViewModel = viewModel(),
) {
    val shop by appViewModel.activeShop.collectAsStateWithLifecycle()

    GlassBackground {
        Column(Modifier.fillMaxSize().statusBarsPadding()) {
            SearchBar(
                value = viewModel.query,
                onValueChange = { viewModel.onQueryChange(it, shop?.id) },
                placeholder = "Ism yoki telefon...",
            )

            if (viewModel.results.isEmpty()) {
                Box(Modifier.weight(1f)) {
                    if (viewModel.searched) {
                        EmptyState(message = "Hech narsa topilmadi")
                    } else {
                        EmptyState(message = "Qidirish uchun yozing", subMessage = "Ism yoki telefon raqam")
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(bottom = 90.dp),
                ) {
                    items(viewModel.results, key = { it.id }) { customer ->
                        CustomerCard(customer = customer, onClick = { onOpenCustomer(customer.id) })
                    }
                }
            }
        }
    }
}
