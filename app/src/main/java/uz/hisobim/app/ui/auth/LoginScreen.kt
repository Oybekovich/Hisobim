package uz.hisobim.app.ui.auth

import androidx.compose.foundation.Image
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import uz.hisobim.app.R
import uz.hisobim.app.ui.components.GlassBackground
import uz.hisobim.app.ui.components.GlassCard
import uz.hisobim.app.ui.components.GlassInput
import uz.hisobim.app.ui.components.PrimaryButton
import uz.hisobim.app.ui.theme.AppColors
import uz.hisobim.app.ui.theme.AppType
import uz.hisobim.app.ui.theme.Radius
import uz.hisobim.app.ui.theme.Spacing
import uz.hisobim.app.ui.theme.heroShadow
import androidx.compose.foundation.clickable

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    viewModel: LoginViewModel = viewModel(),
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
                modifier = Modifier.padding(bottom = Spacing.xxl),
            ) {
                Box(
                    modifier = Modifier
                        .padding(bottom = Spacing.lg)
                        .size(84.dp)
                        .heroShadow(RoundedCornerShape(Radius.hero))
                        .clip(RoundedCornerShape(Radius.hero)),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_logo),
                        contentDescription = "Hisobim logosi",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                Text("Hisobim", style = AppType.display.copy(fontSize = 30.sp))
                Text(
                    "Do'koningiz uchun raqamli daftar",
                    style = AppType.caption,
                    modifier = Modifier.padding(top = Spacing.sm),
                )
            }

            // ── Karta ──
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(Spacing.xxl)) {
                    Text("Kirish", style = AppType.h1, modifier = Modifier.padding(bottom = Spacing.xs))
                    Text(
                        "Email va parolingizni kiriting",
                        style = AppType.caption,
                        modifier = Modifier.padding(bottom = Spacing.xl),
                    )

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
                        placeholder = "Kamida 6 ta belgi",
                        secure = true,
                        error = viewModel.passwordError,
                    )

                    if (viewModel.errorMsg.isNotEmpty()) {
                        Text(
                            viewModel.errorMsg,
                            style = AppType.caption.copy(color = AppColors.debtRed),
                            modifier = Modifier.padding(bottom = Spacing.m),
                        )
                    }

                    PrimaryButton(
                        label = "Kirish",
                        onClick = viewModel::submit,
                        loading = viewModel.loading,
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = Spacing.xl),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text("Hisobingiz yo'qmi? ", style = AppType.caption)
                        Text(
                            "Ro'yxatdan o'tish",
                            style = AppType.label.copy(color = AppColors.accentBlue600),
                            modifier = Modifier.clickable(onClick = onNavigateToRegister),
                        )
                    }
                }
            }
        }
    }
}
