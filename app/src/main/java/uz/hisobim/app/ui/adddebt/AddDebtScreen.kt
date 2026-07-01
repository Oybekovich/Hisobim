package uz.hisobim.app.ui.adddebt

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import uz.hisobim.app.ui.AppViewModel
import uz.hisobim.app.ui.components.GlassBackground
import uz.hisobim.app.ui.components.GlassInput
import uz.hisobim.app.ui.components.PrimaryButton
import uz.hisobim.app.ui.components.SegmentToggle
import uz.hisobim.app.ui.components.SimpleTopBar
import uz.hisobim.app.ui.theme.AppColors
import uz.hisobim.app.ui.theme.AppType
import uz.hisobim.app.ui.theme.Spacing
import uz.hisobim.app.util.formatAmount

@Composable
fun AddDebtScreen(
    appViewModel: AppViewModel,
    onBack: () -> Unit,
    viewModel: AddDebtViewModel = viewModel(),
) {
    val shop by appViewModel.activeShop.collectAsStateWithLifecycle()
    val user = appViewModel.currentUser()

    val parsed = viewModel.amount.toDoubleOrNull()
    val showPreview = viewModel.amount.isNotBlank() && parsed != null && parsed > 0
    val displayAmount = if (showPreview) {
        formatAmount(if (viewModel.entryType == EntryType.payment) -parsed!! else parsed!!)
    } else ""

    GlassBackground {
        Column(Modifier.fillMaxSize()) {
            SimpleTopBar(title = "Yozuv qo'shish", onBack = onBack)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
                    .verticalScroll(rememberScrollState())
                    .padding(start = Spacing.lg, end = Spacing.lg, top = Spacing.xl, bottom = Spacing.lg),
            ) {
                Column(Modifier.padding(bottom = Spacing.xl)) {
                    SegmentToggle(
                        value = viewModel.entryType.name,
                        onChange = { viewModel.onEntryTypeChange(EntryType.valueOf(it)) },
                        options = listOf(
                            EntryType.debt.name to "Qarz qildi",
                            EntryType.payment.name to "To'lov qildi",
                        ),
                    )
                }

                GlassInput(
                    label = "Summa (so'm)",
                    value = viewModel.amount,
                    onValueChange = viewModel::onAmountChange,
                    keyboardType = KeyboardType.Number,
                    placeholder = "0",
                    error = viewModel.amountError,
                )

                if (showPreview) {
                    Text(
                        displayAmount,
                        style = AppType.display.copy(
                            color = if (viewModel.entryType == EntryType.payment) AppColors.paidGreen else AppColors.debtRed,
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(y = -Spacing.s)
                            .padding(bottom = Spacing.lg),
                    )
                }

                GlassInput(
                    label = "Izoh (ixtiyoriy)",
                    value = viewModel.description,
                    onValueChange = viewModel::onDescriptionChange,
                    placeholder = "Qo'shimcha izoh",
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
