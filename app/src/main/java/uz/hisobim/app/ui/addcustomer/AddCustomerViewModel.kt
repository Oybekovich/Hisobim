package uz.hisobim.app.ui.addcustomer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uz.hisobim.app.Graph
import uz.hisobim.app.data.model.CreateCustomerPayload
import uz.hisobim.app.data.model.CreateDebtPayload
import uz.hisobim.app.util.Validation
import uz.hisobim.app.util.formatUzPhone

/** RN'dagi `customer/add.tsx` form mantig'i. */
class AddCustomerViewModel : ViewModel() {

    private val customerRepo = Graph.customerRepository
    private val debtRepo = Graph.debtRepository

    var name by mutableStateOf("")
        private set
    var amount by mutableStateOf("") // faqat raqamlar (raw)
        private set
    var phone by mutableStateOf("")
        private set
    var note by mutableStateOf("")
        private set

    var nameError by mutableStateOf<String?>(null)
        private set
    var amountError by mutableStateOf<String?>(null)
        private set
    var phoneError by mutableStateOf<String?>(null)
        private set
    var saving by mutableStateOf(false)
        private set

    fun onNameChange(value: String) { name = value; nameError = null }
    fun onAmountChange(value: String) { amount = value.filter { it.isDigit() }; amountError = null }
    fun onPhoneChange(value: String) { phone = formatUzPhone(value); phoneError = null }
    fun onNoteChange(value: String) { note = value }

    fun submit(shopId: String, userId: String?, onDone: () -> Unit) {
        val nErr = Validation.customerName(name)
        val aErr = Validation.customerAmountOptional(amount)
        val pErr = Validation.phoneOptional(phone)
        nameError = nErr
        amountError = aErr
        phoneError = pErr
        if (nErr != null || aErr != null || pErr != null) return

        saving = true
        viewModelScope.launch {
            runCatching {
                val customer = customerRepo.createCustomer(
                    CreateCustomerPayload(
                        shopId = shopId,
                        name = name,
                        phone = phone.ifBlank { null },
                        note = note.ifBlank { null },
                    )
                )
                val amt = amount.toDoubleOrNull()
                if (amount.isNotBlank() && amt != null && amt > 0 && userId != null) {
                    debtRepo.createDebt(
                        CreateDebtPayload(
                            shopId = shopId,
                            customerId = customer.id,
                            amount = amt,
                            description = "Boshlang'ich qarz",
                            createdBy = userId,
                        )
                    )
                }
            }.onSuccess { onDone() }
            saving = false
        }
    }
}
