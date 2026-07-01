package uz.hisobim.app.ui.common

/** React Query holatining yengil ekvivalenti: data + loading + error. */
data class UiState<T>(
    val data: T? = null,
    val loading: Boolean = false,
    val error: String? = null,
)
