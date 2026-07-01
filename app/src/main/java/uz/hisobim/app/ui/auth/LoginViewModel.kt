package uz.hisobim.app.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uz.hisobim.app.Graph
import uz.hisobim.app.data.repo.AuthRepository
import uz.hisobim.app.util.Validation

/** RN'dagi `login.tsx` form mantig'i (react-hook-form + zod). */
class LoginViewModel : ViewModel() {

    private val auth = Graph.authRepository

    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var emailError by mutableStateOf<String?>(null)
        private set
    var passwordError by mutableStateOf<String?>(null)
        private set
    var loading by mutableStateOf(false)
        private set
    var errorMsg by mutableStateOf("")
        private set

    fun onEmailChange(value: String) {
        email = value; emailError = null; errorMsg = ""
    }

    fun onPasswordChange(value: String) {
        password = value; passwordError = null; errorMsg = ""
    }

    fun submit() {
        // "admin" logini uchun email formati tekshirilmaydi va admin emailга bog'lanadi.
        val isAdminLogin = email.trim().equals(AuthRepository.ADMIN_LOGIN, ignoreCase = true)
        val loginEmail = if (isAdminLogin) AuthRepository.ADMIN_EMAIL else email.trim()

        val eErr = if (isAdminLogin) null else Validation.email(email)
        val pErr = Validation.password(password)
        emailError = eErr
        passwordError = pErr
        if (eErr != null || pErr != null) return

        loading = true
        errorMsg = ""
        viewModelScope.launch {
            runCatching { auth.signIn(loginEmail, password) }
                .onFailure { errorMsg = "Email yoki parol noto'g'ri." }
            loading = false
        }
    }
}
