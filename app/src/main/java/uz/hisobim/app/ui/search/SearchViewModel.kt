package uz.hisobim.app.ui.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uz.hisobim.app.Graph
import uz.hisobim.app.data.model.Customer

/** RN'dagi `search.tsx` mantig'i (server-side qidiruv). */
class SearchViewModel : ViewModel() {

    private val repo = Graph.customerRepository

    var query by mutableStateOf("")
        private set
    var results by mutableStateOf<List<Customer>>(emptyList())
        private set
    var searched by mutableStateOf(false)
        private set

    fun onQueryChange(text: String, shopId: String?) {
        query = text
        if (shopId == null || text.trim().length < 1) {
            results = emptyList()
            searched = false
            return
        }
        viewModelScope.launch {
            runCatching { repo.searchCustomers(shopId, text.trim()) }
                .onSuccess { results = it; searched = true }
                .onFailure { results = emptyList() }
        }
    }
}
