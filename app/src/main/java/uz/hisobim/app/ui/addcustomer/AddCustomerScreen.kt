package uz.hisobim.app.ui.addcustomer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import uz.hisobim.app.ui.AppViewModel
import uz.hisobim.app.ui.components.GlassBackground
import uz.hisobim.app.ui.components.GlassInput
import uz.hisobim.app.ui.components.PrimaryButton
import uz.hisobim.app.ui.components.SimpleTopBar
import uz.hisobim.app.ui.theme.AppColors
import uz.hisobim.app.ui.theme.Spacing
import uz.hisobim.app.util.groupThousands

@Composable
fun AddCustomerScreen(
    appViewModel: AppViewModel,
    onBack: () -> Unit,
    viewModel: AddCustomerViewModel = viewModel(),
) {
    val shop by appViewModel.activeShop.collectAsStateWithLifecycle()
    val user = appViewModel.currentUser()

    GlassBackground {
        Column(Modifier.fillMaxSize()) {
            SimpleTopBar(title = "Mijoz qo'shish", onBack = onBack, backTint = AppColors.ink900)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
                    .verticalScroll(rememberScrollState())
                    .padding(start = Spacing.lg, end = Spacing.lg, top = Spacing.m, bottom = Spacing.lg),
            ) {
                GlassInput(
                    label = "Ism *",
                    value = viewModel.name,
                    onValueChange = viewModel::onNameChange,
                    placeholder = "Mijoz ismi",
                    error = viewModel.nameError,
                )
                GlassInput(
                    label = "Qarz summasi (ixtiyoriy)",
                    value = groupThousands(viewModel.amount),
                    onValueChange = viewModel::onAmountChange,
                    keyboardType = KeyboardType.Number,
                    placeholder = "0",
                    suffix = "UZS",
                    error = viewModel.amountError,
                )
                GlassInput(
                    label = "Telefon (ixtiyoriy)",
                    value = viewModel.phone,
                    onValueChange = viewModel::onPhoneChange,
                    keyboardType = KeyboardType.Phone,
                    placeholder = "+998 90-123-45-67",
                    error = viewModel.phoneError,
                )
                GlassInput(
                    label = "Izoh (ixtiyoriy)",
                    value = viewModel.note,
                    onValueChange = viewModel::onNoteChange,
                    placeholder = "Qo'shimcha izoh",
                    multiline = true,
                )

                PrimaryButton(
                    label = "Saqlash",
                    onClick = {
                        shop?.let { viewModel.submit(it.id, user?.id, onBack) }
                    },
                    loading = viewModel.saving,
                )
            }
        }
    }
}
