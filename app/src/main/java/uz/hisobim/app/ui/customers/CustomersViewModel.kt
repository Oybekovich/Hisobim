package uz.hisobim.app.ui.customers

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uz.hisobim.app.Graph
import uz.hisobim.app.data.model.Customer
import uz.hisobim.app.ui.common.UiState

/** RN'dagi `useCustomers` hook + home ekran holati. */
class CustomersViewModel : ViewModel() {

    private val repo = Graph.customerRepository
    private var shopId: String? = null

    var state by mutableStateOf(UiState<List<Customer>>(loading = true))
        private set

    fun setShopId(id: String) {
        if (shopId == id) return
        shopId = id
    }

    fun refresh() {
        val sid = shopId ?: return
        if (state.loading && state.data != null) return
        viewModelScope.launch {
            state = state.copy(loading = true)
            runCatching { repo.fetchCustomers(sid) }
                .onSuccess { state = UiState(data = it, loading = false) }
                .onFailure { state = state.copy(loading = false, error = it.message) }
        }
    }
}
