package uz.hisobim.app.ui.editcustomer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uz.hisobim.app.Graph
import uz.hisobim.app.data.model.UpdateCustomerPayload
import uz.hisobim.app.util.formatUzPhone

/** RN'dagi `customer/edit.tsx` form mantig'i. */
class EditCustomerViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val customerRepo = Graph.customerRepository
    private val id: String = checkNotNull(savedStateHandle["id"])

    var name by mutableStateOf("")
        private set
    var phone by mutableStateOf("")
        private set
    var note by mutableStateOf("")
        private set
    var saving by mutableStateOf(false)
        private set

    private var prefilled = false

    init {
        viewModelScope.launch {
            runCatching { customerRepo.fetchCustomerById(id) }
                .onSuccess { c ->
                    if (!prefilled) {
                        name = c.name
                        phone = formatUzPhone(c.phone ?: "")
                        note = c.note ?: ""
                        prefilled = true
                    }
                }
        }
    }

    val nameError: Boolean get() = name.trim().isEmpty()

    fun onNameChange(value: String) { name = value }
    fun onPhoneChange(value: String) { phone = formatUzPhone(value) }
    fun onNoteChange(value: String) { note = value }

    fun save(onDone: () -> Unit) {
        if (nameError) return
        saving = true
        viewModelScope.launch {
            runCatching {
                customerRepo.updateCustomer(
                    id,
                    UpdateCustomerPayload(
                        name = name.trim(),
                        phone = phone.trim().ifBlank { null },
                        note = note.trim().ifBlank { null },
                    )
                )
            }.onSuccess { onDone() }
            saving = false
        }
    }
}
