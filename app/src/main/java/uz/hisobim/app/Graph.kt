package uz.hisobim.app

import android.content.Context
import uz.hisobim.app.data.local.DeviceIdProvider
import uz.hisobim.app.data.local.ShopPreferences
import uz.hisobim.app.data.repo.AdminRepository
import uz.hisobim.app.data.repo.AuthRepository
import uz.hisobim.app.data.repo.CustomerRepository
import uz.hisobim.app.data.repo.DebtRepository
import uz.hisobim.app.data.repo.DeviceRepository
import uz.hisobim.app.data.repo.ReportsRepository
import uz.hisobim.app.data.repo.ShopRepository

/**
 * Oddiy service locator — repozitoriyalar va store'lar uchun yagona kirish nuqtasi.
 * (Hilt o'rniga yengil DI, KSP/kapt'siz.)
 */
object Graph {

    lateinit var shopPreferences: ShopPreferences
        private set
    lateinit var deviceIdProvider: DeviceIdProvider
        private set

    val authRepository: AuthRepository by lazy { AuthRepository() }
    val customerRepository: CustomerRepository by lazy { CustomerRepository() }
    val debtRepository: DebtRepository by lazy { DebtRepository() }
    val reportsRepository: ReportsRepository by lazy { ReportsRepository() }
    val shopRepository: ShopRepository by lazy { ShopRepository() }
    val deviceRepository: DeviceRepository by lazy { DeviceRepository() }
    val adminRepository: AdminRepository by lazy { AdminRepository() }

    fun init(context: Context) {
        shopPreferences = ShopPreferences(context.applicationContext)
        deviceIdProvider = DeviceIdProvider(context.applicationContext)
    }
}
