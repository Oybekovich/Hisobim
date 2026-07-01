package uz.hisobim.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import uz.hisobim.app.Graph
import uz.hisobim.app.data.model.Shop

/**
 * Ilova darajasidagi holat — RN'dagi `auth.store.ts` + `shop.store.ts` ekvivalenti.
 * Activity'ga scoped, barcha ekranlar uchun yagona sessiya/do'kon manbasi.
 */
class AppViewModel : ViewModel() {

    private val authRepo = Graph.authRepository
    private val shopRepo = Graph.shopRepository
    private val shopPrefs = Graph.shopPreferences

    val sessionStatus: StateFlow<SessionStatus> = authRepo.sessionStatus

    val activeShop: StateFlow<Shop?> =
        shopPrefs.activeShop.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun currentUser(): UserInfo? = authRepo.currentUser()

    // Bir sessiya uchun qurilmani faqat bir marta ro'yxatga qo'shish (refresh'da takrorlamaslik).
    private var lastRegisteredSession: String? = null

    init {
        // Sessiya tugaganda (chiqish yoki token yangilash muvaffaqiyatsizligi)
        // faol do'konni tozalaymiz — aks holda boshqa foydalanuvchi kirsa,
        // eski do'kon id'si ishlatilib qoladi (RLS rad etadi).
        viewModelScope.launch {
            sessionStatus.collect { status ->
                if (status is SessionStatus.Authenticated) {
                    registerDeviceIfNeeded(status.session.user?.id)
                } else {
                    lastRegisteredSession = null
                    val loggedOut = status !is SessionStatus.Initializing
                    if (loggedOut && activeShop.value != null) {
                        shopPrefs.setShop(null)
                    }
                }
            }
        }
    }

    /** Kirgan qurilmani `user_devices` ro'yxatiga qo'shadi (sessiya boshiga bir marta). */
    private suspend fun registerDeviceIfNeeded(userId: String?) {
        if (userId == null) return
        val session = authRepo.currentSession()?.accessToken ?: return
        if (session == lastRegisteredSession) return
        lastRegisteredSession = session
        runCatching { Graph.deviceRepository.registerCurrentDevice(userId) }
    }

    @Volatile
    private var ensuring = false

    /** RN home ekranidagi `fetchOrCreateShop` effekti. */
    fun ensureShop() {
        val user = authRepo.currentUser() ?: return
        if (activeShop.value != null || ensuring) return
        ensuring = true
        viewModelScope.launch {
            runCatching { shopRepo.fetchOrCreateShop(user.id, user.phone) }
                .onSuccess { shopPrefs.setShop(it) }
            ensuring = false
        }
    }

    fun setShop(shop: Shop?) {
        viewModelScope.launch { shopPrefs.setShop(shop) }
    }

    fun signOut(onDone: () -> Unit) {
        viewModelScope.launch {
            runCatching { authRepo.signOut() }
            shopPrefs.setShop(null)
            onDone()
        }
    }
}
