package uz.hisobim.app.ui.editcustomer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import uz.hisobim.app.ui.components.GlassBackground
import uz.hisobim.app.ui.components.GlassInput
import uz.hisobim.app.ui.components.PrimaryButton
import uz.hisobim.app.ui.components.SimpleTopBar
import uz.hisobim.app.ui.theme.Spacing

@Composable
fun EditCustomerScreen(
    onBack: () -> Unit,
    viewModel: EditCustomerViewModel = viewModel(),
) {
    GlassBackground {
        Column(Modifier.fillMaxSize()) {
            SimpleTopBar(title = "Mijozni tahrirlash", onBack = onBack)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
                    .verticalScroll(rememberScrollState())
                    .padding(start = Spacing.lg, end = Spacing.lg, top = Spacing.xl, bottom = Spacing.lg),
            ) {
                GlassInput(
                    label = "Ism *",
                    value = viewModel.name,
                    onValueChange = viewModel::onNameChange,
                    placeholder = "Mijoz ismi",
                    autoFocus = true,
                    error = if (viewModel.nameError && viewModel.name.isNotEmpty()) "Ism kiritilishi shart" else null,
                )
                GlassInput(
                    label = "Telefon raqam",
                    value = viewModel.phone,
                    onValueChange = viewModel::onPhoneChange,
                    keyboardType = KeyboardType.Phone,
                    placeholder = "+998 90-123-45-67",
                )
                GlassInput(
                    label = "Izoh",
                    value = viewModel.note,
                    onValueChange = viewModel::onNoteChange,
                    placeholder = "Qo'shimcha izoh",
                    multiline = true,
                )

                PrimaryButton(
                    label = "Saqlash",
                    onClick = { viewModel.save(onBack) },
                    loading = viewModel.saving,
                    enabled = !viewModel.nameError,
                )
            }
        }
    }
}
