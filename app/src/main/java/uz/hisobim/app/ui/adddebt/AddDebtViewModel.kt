package uz.hisobim.app.ui.adddebt

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uz.hisobim.app.Graph
import uz.hisobim.app.data.model.CreateDebtPayload
import uz.hisobim.app.util.Validation

enum class EntryType { debt, payment }

/** RN'dagi `debt/add.tsx` form mantig'i (segment + summa + izoh). */
class AddDebtViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val debtRepo = Graph.debtRepository
    private val customerId: String = checkNotNull(savedStateHandle["customerId"])

    var entryType by mutableStateOf(EntryType.debt)
        private set
    var amount by mutableStateOf("") // faqat raqamlar
        private set
    var description by mutableStateOf("")
        private set
    var amountError by mutableStateOf<String?>(null)
        private set
    var saving by mutableStateOf(false)
        private set

    fun onEntryTypeChange(type: EntryType) { entryType = type }
    fun onAmountChange(value: String) { amount = value.filter { it.isDigit() }; amountError = null }
    fun onDescriptionChange(value: String) { description = value }

    fun submit(shopId: String, userId: String?, onDone: () -> Unit) {
        val aErr = Validation.debtAmount(amount)
        amountError = aErr
        if (aErr != null || userId == null) return

        val numeric = amount.toDouble()
        val finalAmount = if (entryType == EntryType.payment) -numeric else numeric

        saving = true
        viewModelScope.launch {
            runCatching {
                debtRepo.createDebt(
                    CreateDebtPayload(
                        shopId = shopId,
                        customerId = customerId,
                        amount = finalAmount,
                        description = description.ifBlank { null },
                        createdBy = userId,
                    )
                )
            }.onSuccess { onDone() }
            saving = false
        }
    }
}
