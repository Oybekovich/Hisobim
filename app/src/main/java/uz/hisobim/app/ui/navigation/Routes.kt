package uz.hisobim.app.ui.navigation

/** Marshrutlar — RN'dagi expo-router fayl tuzilishi ekvivalenti. */
object Routes {
    // Auth
    const val LOGIN = "login"
    const val REGISTER = "register"

    // Tabs
    const val CUSTOMERS = "customers" // (tabs)/index
    const val REPORTS = "reports"
    const val SETTINGS = "settings"
    const val SEARCH = "search" // (tabs)/search — RN'da href:null (yashirin)

    // Stack
    const val DEVICES = "devices_list"
    const val CUSTOMER_DETAIL = "customer/{id}"
    const val CUSTOMER_ADD = "customer_add"
    const val CUSTOMER_EDIT = "customer_edit/{id}"
    const val DEBT_ADD = "debt_add/{customerId}"

    fun customerDetail(id: String) = "customer/$id"
    fun customerEdit(id: String) = "customer_edit/$id"
    fun debtAdd(customerId: String) = "debt_add/$customerId"
}
