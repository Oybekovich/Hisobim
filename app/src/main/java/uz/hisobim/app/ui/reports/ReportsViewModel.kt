package uz.hisobim.app.ui.reports

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uz.hisobim.app.Graph
import uz.hisobim.app.data.model.ReportData
import uz.hisobim.app.ui.common.UiState

/** RN'dagi `useReports` hook ekvivalenti. */
class ReportsViewModel : ViewModel() {

    private val repo = Graph.reportsRepository
    private var shopId: String? = null

    var state by mutableStateOf(UiState<ReportData>(loading = true))
        private set

    fun setShopId(id: String) {
        if (shopId == id) return
        shopId = id
    }

    fun refresh() {
        val sid = shopId ?: return
        viewModelScope.launch {
            state = state.copy(loading = true)
            runCatching { repo.fetchReports(sid) }
                .onSuccess { state = UiState(data = it, loading = false) }
                .onFailure { state = state.copy(loading = false, error = it.message) }
        }
    }
}
