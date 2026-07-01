package uz.hisobim.app.ui.admin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uz.hisobim.app.Graph
import uz.hisobim.app.data.model.AdminAccount
import uz.hisobim.app.ui.common.UiState

/** Admin panel holati — barcha hisoblar ro'yxati va ular ustidagi amallar. */
class AdminViewModel : ViewModel() {

    private val repo = Graph.adminRepository

    var state by mutableStateOf(UiState<List<AdminAccount>>(loading = true))
        private set
    var busyUserId by mutableStateOf<String?>(null)
        private set
    var message by mutableStateOf<String?>(null)
        private set

    init { load() }

    fun load() {
        viewModelScope.launch {
            state = state.copy(loading = true)
            runCatching { repo.listAccounts() }
                .onSuccess { state = UiState(data = it, loading = false) }
                .onFailure { state = state.copy(loading = false, error = it.message) }
        }
    }

    fun deleteUser(id: String) {
        if (busyUserId != null) return
        busyUserId = id
        viewModelScope.launch {
            runCatching { repo.deleteUser(id) }
                .onSuccess { busyUserId = null; message = "Hisob o'chirildi"; load() }
                .onFailure { busyUserId = null; message = "O'chirishda xatolik yuz berdi" }
        }
    }

    fun updateEmail(id: String, email: String, onDone: () -> Unit) {
        if (busyUserId != null) return
        busyUserId = id
        viewModelScope.launch {
            runCatching { repo.updateEmail(id, email.trim()) }
                .onSuccess { busyUserId = null; message = "Login (email) yangilandi"; onDone(); load() }
                .onFailure { busyUserId = null; message = "Yangilashda xatolik (email band bo'lishi mumkin)" }
        }
    }

    fun updatePassword(id: String, password: String, onDone: () -> Unit) {
        if (busyUserId != null) return
        busyUserId = id
        viewModelScope.launch {
            runCatching { repo.updatePassword(id, password) }
                .onSuccess { busyUserId = null; message = "Parol yangilandi"; onDone() }
                .onFailure { busyUserId = null; message = "Parol yangilashda xatolik" }
        }
    }

    fun clearMessage() { message = null }
}
