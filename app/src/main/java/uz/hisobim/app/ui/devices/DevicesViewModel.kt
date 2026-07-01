package uz.hisobim.app.ui.devices

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uz.hisobim.app.Graph
import uz.hisobim.app.data.model.UserDevice
import uz.hisobim.app.ui.common.UiState

/** Kirgan qurilmalar ro'yxati + masofadan chiqarish. */
class DevicesViewModel : ViewModel() {

    private val repo = Graph.deviceRepository

    var state by mutableStateOf(UiState<List<UserDevice>>(loading = true))
        private set
    var currentDeviceId by mutableStateOf<String?>(null)
        private set
    var revokingId by mutableStateOf<String?>(null)
        private set
    var errorMsg by mutableStateOf<String?>(null)
        private set

    fun load(userId: String?) {
        if (userId == null) return
        viewModelScope.launch {
            if (currentDeviceId == null) {
                currentDeviceId = runCatching { repo.currentDeviceId() }.getOrNull()
            }
            state = state.copy(loading = true)
            runCatching { repo.fetchDevices(userId) }
                .onSuccess { state = UiState(data = it, loading = false) }
                .onFailure { state = state.copy(loading = false, error = it.message) }
        }
    }

    /**
     * Qurilmani chiqaradi. Agar bu joriy qurilma bo'lsa — `onSelfRevoked`
     * (ilovadan chiqish) chaqiriladi, aks holda ro'yxat yangilanadi.
     */
    fun revoke(device: UserDevice, userId: String?, onSelfRevoked: () -> Unit) {
        if (revokingId != null) return
        revokingId = device.id
        errorMsg = null
        viewModelScope.launch {
            runCatching { repo.revokeDevice(device.id) }
                .onSuccess {
                    revokingId = null
                    if (device.deviceId == currentDeviceId) {
                        onSelfRevoked()
                    } else {
                        load(userId)
                    }
                }
                .onFailure {
                    revokingId = null
                    errorMsg = "Qurilmani chiqarishda xatolik yuz berdi"
                }
        }
    }

    fun clearError() { errorMsg = null }

    fun isCurrent(device: UserDevice): Boolean =
        currentDeviceId != null && device.deviceId == currentDeviceId
}
