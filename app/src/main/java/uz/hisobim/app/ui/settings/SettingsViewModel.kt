package uz.hisobim.app.ui.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uz.hisobim.app.Graph
import uz.hisobim.app.data.model.Shop

/** RN'dagi `settings.tsx` do'kon nomini tahrirlash mantig'i. */
class SettingsViewModel : ViewModel() {

    private val shopRepo = Graph.shopRepository

    var editingName by mutableStateOf(false)
        private set
    var nameDraft by mutableStateOf("")
        private set
    var saving by mutableStateOf(false)
        private set

    fun startEdit(currentName: String) {
        nameDraft = currentName
        editingName = true
    }

    fun cancelEdit(currentName: String) {
        nameDraft = currentName
        editingName = false
    }

    fun onNameChange(value: String) {
        nameDraft = value
    }

    fun saveName(shopId: String, onSuccess: (Shop) -> Unit, onError: () -> Unit) {
        val trimmed = nameDraft.trim()
        if (trimmed.isEmpty()) return
        saving = true
        viewModelScope.launch {
            runCatching { shopRepo.updateShopName(shopId, trimmed) }
                .onSuccess {
                    editingName = false
                    onSuccess(it)
                }
                .onFailure { onError() }
            saving = false
        }
    }
}
