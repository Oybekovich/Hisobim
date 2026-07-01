package uz.hisobim.app.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uz.hisobim.app.Graph
import uz.hisobim.app.util.Validation

/** RN'dagi `register.tsx` form mantig'i. */
class RegisterViewModel : ViewModel() {

    private val auth = Graph.authRepository

    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var confirmPassword by mutableStateOf("")
        private set
    var emailError by mutableStateOf<String?>(null)
        private set
    var passwordError by mutableStateOf<String?>(null)
        private set
    var confirmError by mutableStateOf<String?>(null)
        private set
    var loading by mutableStateOf(false)
        private set
    var errorMsg by mutableStateOf("")
        private set
    var infoMsg by mutableStateOf("")
        private set

    fun onEmailChange(value: String) { email = value; emailError = null; clearMsgs() }
    fun onPasswordChange(value: String) { password = value; passwordError = null; clearMsgs() }
    fun onConfirmChange(value: String) { confirmPassword = value; confirmError = null; clearMsgs() }

    private fun clearMsgs() { errorMsg = ""; infoMsg = "" }

    fun submit() {
        val eErr = Validation.email(email)
        val pErr = Validation.password(password)
        val cErr = Validation.confirmPassword(password, confirmPassword)
        emailError = eErr
        passwordError = pErr
        confirmError = cErr
        if (eErr != null || pErr != null || cErr != null) return

        loading = true
        clearMsgs()
        viewModelScope.launch {
            runCatching {
                auth.signUp(email, password)
                auth.currentSession()
            }.onSuccess { session ->
                // Sessiya bo'lmasa — email tasdiqlash kerak (RN'dagidek infoMsg).
                if (session == null) {
                    infoMsg = "Hisob yaratildi. Endi kirish sahifasidan kiring."
                }
                // Sessiya bor bo'lsa, NavGraph avtomatik tabs'ga o'tadi.
            }.onFailure {
                errorMsg = "Ro'yxatdan o'tishda xatolik. Bu email band bo'lishi mumkin."
            }
            loading = false
        }
    }
}
