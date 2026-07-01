package uz.hisobim.app.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import uz.hisobim.app.ui.components.AppIcons
import uz.hisobim.app.ui.components.GlassBackground
import uz.hisobim.app.ui.components.GlassCard
import uz.hisobim.app.ui.components.GlassInput
import uz.hisobim.app.ui.components.PrimaryButton
import uz.hisobim.app.ui.theme.AppColors
import uz.hisobim.app.ui.theme.AppType
import uz.hisobim.app.ui.theme.BlueGradient
import uz.hisobim.app.ui.theme.Radius
import uz.hisobim.app.ui.theme.Spacing
import uz.hisobim.app.ui.theme.heroShadow

@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    viewModel: RegisterViewModel = viewModel(),
) {
    GlassBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(Spacing.lg),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // ── Brand ──
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = Spacing.xl),
            ) {
                Box(
                    modifier = Modifier
                        .padding(bottom = Spacing.m)
                        .size(68.dp)
                        .heroShadow(RoundedCornerShape(Radius.hero))
                        .clip(RoundedCornerShape(Radius.hero))
                        .background(Brush.linearGradient(BlueGradient)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(AppIcons.personAddOutline, contentDescription = null, tint = AppColors.white, modifier = Modifier.size(32.dp))
                }
                Text("Ro'yxatdan o'tish", style = AppType.display.copy(fontSize = 24.sp))
                Text(
                    "Yangi do'kon hisobini oching",
                    style = AppType.caption,
                    modifier = Modifier.padding(top = Spacing.sm),
                )
            }

            // ── Karta ──
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(Spacing.xxl)) {
                    GlassInput(
                        label = "Email",
                        value = viewModel.email,
                        onValueChange = viewModel::onEmailChange,
                        placeholder = "misol@hisobim.uz",
                        keyboardType = KeyboardType.Email,
                        autoCapitalizeNone = true,
                        error = viewModel.emailError,
                    )
                    GlassInput(
                        label = "Parol",
                        value = viewModel.password,
                        onValueChange = viewModel::onPasswordChange,
                        secure = true,
                        error = viewModel.passwordError,
                    )
                    GlassInput(
                        label = "Parolni tasdiqlang",
                        value = viewModel.confirmPassword,
                        onValueChange = viewModel::onConfirmChange,
                        secure = true,
                        error = viewModel.confirmError,
                    )

                    if (viewModel.errorMsg.isNotEmpty()) {
                        Text(
                            viewModel.errorMsg,
                            style = AppType.caption.copy(color = AppColors.debtRed),
                            modifier = Modifier.padding(bottom = Spacing.m),
                        )
                    }
                    if (viewModel.infoMsg.isNotEmpty()) {
                        Text(
                            viewModel.infoMsg,
                            style = AppType.caption.copy(color = AppColors.accentBlue600),
                            modifier = Modifier.padding(bottom = Spacing.m),
                        )
                    }

                    PrimaryButton(
                        label = "Ro'yxatdan o'tish",
                        onClick = viewModel::submit,
                        loading = viewModel.loading,
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = Spacing.xl),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text("Hisobingiz bormi? ", style = AppType.caption)
                        Text(
                            "Kirish",
                            style = AppType.label.copy(color = AppColors.accentBlue600),
                            modifier = Modifier.clickable(onClick = onNavigateToLogin),
                        )
                    }
                }
            }
        }
    }
}
