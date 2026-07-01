package uz.hisobim.app.ui.customerdetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uz.hisobim.app.Graph
import uz.hisobim.app.data.model.Customer
import uz.hisobim.app.data.model.Debt
import uz.hisobim.app.ui.common.UiState

/** RN'dagi `customer/[id].tsx` (useCustomerById + useDebts + delete mutations). */
class CustomerDetailViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val customerRepo = Graph.customerRepository
    private val debtRepo = Graph.debtRepository

    val id: String = checkNotNull(savedStateHandle["id"])

    var customer by mutableStateOf(UiState<Customer>(loading = true))
        private set
    var debts by mutableStateOf(UiState<List<Debt>>(loading = true))
        private set

    fun refresh() {
        loadCustomer()
        loadDebts()
    }

    private fun loadCustomer() {
        viewModelScope.launch {
            runCatching { customerRepo.fetchCustomerById(id) }
                .onSuccess { customer = UiState(data = it, loading = false) }
                .onFailure { customer = customer.copy(loading = false, error = it.message) }
        }
    }

    private fun loadDebts() {
        viewModelScope.launch {
            debts = debts.copy(loading = true)
            runCatching { debtRepo.fetchDebtsByCustomer(id) }
                .onSuccess { debts = UiState(data = it, loading = false) }
                .onFailure { debts = debts.copy(loading = false, error = it.message) }
        }
    }

    fun deleteDebt(debtId: String) {
        viewModelScope.launch {
            runCatching { debtRepo.deleteDebt(debtId) }
                .onSuccess { refresh() } // total_debt trigger orqali yangilanadi
        }
    }

    fun deleteCustomer(onDone: () -> Unit) {
        viewModelScope.launch {
            runCatching { customerRepo.deleteCustomer(id) }
                .onSuccess { onDone() }
        }
    }
}
