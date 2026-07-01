package uz.hisobim.app.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.github.jan.supabase.auth.status.SessionStatus
import uz.hisobim.app.ui.AppViewModel
import uz.hisobim.app.ui.adddebt.AddDebtScreen
import uz.hisobim.app.ui.addcustomer.AddCustomerScreen
import uz.hisobim.app.ui.auth.LoginScreen
import uz.hisobim.app.ui.auth.RegisterScreen
import uz.hisobim.app.ui.components.AppIcons
import uz.hisobim.app.ui.components.GlassBackground
import uz.hisobim.app.ui.customerdetail.CustomerDetailScreen
import uz.hisobim.app.ui.customers.CustomersScreen
import uz.hisobim.app.ui.editcustomer.EditCustomerScreen
import uz.hisobim.app.ui.reports.ReportsScreen
import uz.hisobim.app.ui.settings.SettingsScreen
import uz.hisobim.app.ui.theme.AppColors
import uz.hisobim.app.ui.theme.AppType
import uz.hisobim.app.ui.theme.GlassVariant
import uz.hisobim.app.ui.theme.softShadow

/** Ildiz: sessiya holatiga qarab auth yoki asosiy grafni ko'rsatadi. */
@Composable
fun HisobimRoot(appViewModel: AppViewModel) {
    val status by appViewModel.sessionStatus.collectAsStateWithLifecycle()

    when (val s = status) {
        is SessionStatus.Authenticated -> {
            val email = s.session.user?.email
            if (email.equals(uz.hisobim.app.data.repo.AuthRepository.ADMIN_EMAIL, ignoreCase = true)) {
                uz.hisobim.app.ui.admin.AdminScreen(appViewModel = appViewModel)
            } else {
                MainGraph(appViewModel)
            }
        }
        is SessionStatus.Initializing -> LoadingScreen()
        else -> AuthGraph() // NotAuthenticated, RefreshFailure
    }
}

@Composable
private fun LoadingScreen() {
    GlassBackground {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = AppColors.accentBlue600)
        }
    }
}

// ── Auth grafi ──────────────────────────────────────────────────
@Composable
private fun AuthGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.LOGIN) {
        composable(Routes.LOGIN) {
            LoginScreen(onNavigateToRegister = { navController.navigate(Routes.REGISTER) })
        }
        composable(Routes.REGISTER) {
            RegisterScreen(onNavigateToLogin = { navController.popBackStack() })
        }
    }
}

// ── Asosiy graf (tablar + stack) ────────────────────────────────
@Composable
private fun MainGraph(appViewModel: AppViewModel) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBottomBar = currentRoute == Routes.CUSTOMERS ||
        currentRoute == Routes.REPORTS ||
        currentRoute == Routes.SETTINGS

    Box(Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = Routes.CUSTOMERS,
            // Oynalar orasida darhol o'tish (standart fade animatsiyasi olib tashlandi — tezroq).
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
        ) {
            composable(Routes.CUSTOMERS) {
                CustomersScreen(
                    appViewModel = appViewModel,
                    onOpenCustomer = { navController.navigate(Routes.customerDetail(it)) },
                    onAddCustomer = { navController.navigate(Routes.CUSTOMER_ADD) },
                )
            }
            composable(Routes.REPORTS) {
                ReportsScreen(
                    appViewModel = appViewModel,
                    onOpenCustomer = { navController.navigate(Routes.customerDetail(it)) },
                )
            }
            composable(Routes.SETTINGS) {
                SettingsScreen(
                    appViewModel = appViewModel,
                    onLoggedOut = { /* sessionStatus avtomatik auth'ga o'tadi */ },
                    onOpenDevices = { navController.navigate(Routes.DEVICES) },
                )
            }
            composable(Routes.DEVICES) {
                uz.hisobim.app.ui.devices.DevicesScreen(
                    appViewModel = appViewModel,
                    onBack = { navController.popBackStack() },
                    onLoggedOut = { /* sessionStatus avtomatik auth'ga o'tadi */ },
                )
            }
            composable(Routes.SEARCH) {
                uz.hisobim.app.ui.search.SearchScreen(
                    appViewModel = appViewModel,
                    onOpenCustomer = { navController.navigate(Routes.customerDetail(it)) },
                )
            }
            composable(
                route = Routes.CUSTOMER_DETAIL,
                arguments = listOf(navArgument("id") { type = NavType.StringType }),
            ) {
                CustomerDetailScreen(
                    onBack = { navController.popBackStack() },
                    onEdit = { navController.navigate(Routes.customerEdit(it)) },
                    onAddDebt = { navController.navigate(Routes.debtAdd(it)) },
                )
            }
            composable(Routes.CUSTOMER_ADD) {
                AddCustomerScreen(appViewModel = appViewModel, onBack = { navController.popBackStack() })
            }
            composable(
                route = Routes.CUSTOMER_EDIT,
                arguments = listOf(navArgument("id") { type = NavType.StringType }),
            ) {
                EditCustomerScreen(onBack = { navController.popBackStack() })
            }
            composable(
                route = Routes.DEBT_ADD,
                arguments = listOf(navArgument("customerId") { type = NavType.StringType }),
            ) {
                AddDebtScreen(appViewModel = appViewModel, onBack = { navController.popBackStack() })
            }
        }

        if (showBottomBar) {
            HisobimBottomBar(
                currentRoute = currentRoute,
                onSelect = { route ->
                    navController.navigate(route) {
                        popUpTo(Routes.CUSTOMERS) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}

// ── Glass tab bar ───────────────────────────────────────────────
private data class TabItem(val route: String, val icon: ImageVector, val label: String)

@Composable
private fun HisobimBottomBar(
    currentRoute: String?,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val items = listOf(
        TabItem(Routes.CUSTOMERS, AppIcons.people, "Mijozlar"),
        TabItem(Routes.REPORTS, AppIcons.barChart, "Hisobot"),
        TabItem(Routes.SETTINGS, AppIcons.settings, "Sozlamalar"),
    )

    val barShape = RoundedCornerShape(22.dp)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .softShadow(barShape)
                .clip(barShape)
                .background(AppColors.white) // to'liq oq (shaffof emas — fon tinti ko'rinmaydi)
                .height(62.dp)
                .padding(top = 6.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items.forEach { item ->
                val active = currentRoute == item.route
                val tint = if (active) AppColors.accentBlue600 else AppColors.slate500
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null, // bosilganda ripple (qora dog') chiqmasin
                            onClick = { if (!active) onSelect(item.route) },
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(item.icon, contentDescription = item.label, tint = tint, modifier = Modifier.size(24.dp))
                    Text(item.label, style = AppType.nav.copy(color = tint), modifier = Modifier.padding(top = 2.dp))
                }
            }
        }
    }
}
