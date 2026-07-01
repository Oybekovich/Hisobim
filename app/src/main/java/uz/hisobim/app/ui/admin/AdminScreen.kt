package uz.hisobim.app.ui.admin

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import uz.hisobim.app.data.model.AdminAccount
import uz.hisobim.app.ui.AppViewModel
import uz.hisobim.app.ui.components.AppIcons
import uz.hisobim.app.ui.components.ConfirmDialog
import uz.hisobim.app.ui.components.EmptyState
import uz.hisobim.app.ui.components.GlassBackground
import uz.hisobim.app.ui.components.GlassCard
import uz.hisobim.app.ui.components.GlassInput
import uz.hisobim.app.ui.components.PrimaryButton
import uz.hisobim.app.ui.theme.AppColors
import uz.hisobim.app.ui.theme.AppType
import uz.hisobim.app.ui.theme.AppWeight
import uz.hisobim.app.ui.theme.Radius
import uz.hisobim.app.ui.theme.Spacing
import uz.hisobim.app.util.formatAmount
import uz.hisobim.app.util.formatDate

@Composable
fun AdminScreen(
    appViewModel: AppViewModel,
    viewModel: AdminViewModel = viewModel(),
) {
    val context = LocalContext.current

    viewModel.message?.let { msg ->
        LaunchedEffect(msg) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.clearMessage()
        }
    }

    var deleteFor by remember { mutableStateOf<AdminAccount?>(null) }
    var editEmailFor by remember { mutableStateOf<AdminAccount?>(null) }
    var editPwFor by remember { mutableStateOf<AdminAccount?>(null) }

    val accounts = viewModel.state.data ?: emptyList()

    GlassBackground {
        Column(Modifier.fillMaxSize()) {
            // ── Sarlavha ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(start = Spacing.lg, end = Spacing.lg, top = Spacing.m, bottom = Spacing.s),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(Modifier.weight(1f)) {
                    Text("Admin panel", style = AppType.h1)
                    Text("${accounts.size} ta hisob", style = AppType.caption, modifier = Modifier.padding(top = 2.dp))
                }
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(Radius.avatarBox))
                        .border(1.dp, Color(0x80C0504C), RoundedCornerShape(Radius.avatarBox))
                        .background(Color(0x0AC0504C))
                        .clickable { appViewModel.signOut {} },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(AppIcons.logOut, contentDescription = "Chiqish", tint = AppColors.debtRed, modifier = Modifier.size(20.dp))
                }
            }

            when {
                viewModel.state.loading && viewModel.state.data == null ->
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = AppColors.accentBlue600)
                    }

                accounts.isEmpty() ->
                    Box(Modifier.fillMaxSize()) {
                        EmptyState(message = "Hisob yo'q", icon = AppIcons.peopleOutline)
                    }

                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = Spacing.md, end = Spacing.md, top = Spacing.xs, bottom = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(Spacing.m),
                ) {
                    items(accounts, key = { it.userId }) { acc ->
                        AccountCard(
                            account = acc,
                            busy = viewModel.busyUserId == acc.userId,
                            onEditEmail = { editEmailFor = acc },
                            onEditPassword = { editPwFor = acc },
                            onDelete = { deleteFor = acc },
                        )
                    }
                }
            }
        }
    }

    // ── O'chirish tasdig'i ──
    deleteFor?.let { acc ->
        ConfirmDialog(
            visible = true,
            title = "Hisobni o'chirish",
            message = "\"${acc.email}\" hisobi va uning do'koni, mijozlari, qarzlari butunlay o'chiriladi. Bu amalni ortga qaytarib bo'lmaydi.",
            confirmLabel = "O'chirish",
            icon = AppIcons.trashOutline,
            onConfirm = { viewModel.deleteUser(acc.userId); deleteFor = null },
            onDismiss = { deleteFor = null },
        )
    }

    // ── Email (login) o'zgartirish ──
    editEmailFor?.let { acc ->
        AdminEditDialog(
            title = "Login (email)",
            label = "Yangi email",
            initial = acc.email,
            secure = false,
            keyboardType = KeyboardType.Email,
            confirmLabel = "Saqlash",
            busy = viewModel.busyUserId == acc.userId,
            onConfirm = { value -> viewModel.updateEmail(acc.userId, value) { editEmailFor = null } },
            onDismiss = { editEmailFor = null },
        )
    }

    // ── Parol o'zgartirish ──
    editPwFor?.let { acc ->
        AdminEditDialog(
            title = "Parolni o'zgartirish",
            label = "Yangi parol (kamida 6 belgi)",
            initial = "",
            secure = true,
            keyboardType = KeyboardType.Password,
            confirmLabel = "Saqlash",
            busy = viewModel.busyUserId == acc.userId,
            minLength = 6,
            onConfirm = { value -> viewModel.updatePassword(acc.userId, value) { editPwFor = null } },
            onDismiss = { editPwFor = null },
        )
    }
}

@Composable
private fun AccountCard(
    account: AdminAccount,
    busy: Boolean,
    onEditEmail: () -> Unit,
    onEditPassword: () -> Unit,
    onDelete: () -> Unit,
) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.fillMaxWidth().padding(Spacing.lg)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    account.email,
                    style = AppType.body.copy(fontWeight = AppWeight.bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )
                if (busy) {
                    CircularProgressIndicator(color = AppColors.accentBlue600, strokeWidth = 2.dp, modifier = Modifier.size(16.dp))
                }
            }
            Text(
                account.shopName ?: "Do'kon ochilmagan",
                style = AppType.caption,
                modifier = Modifier.padding(top = 2.dp),
            )

            Row(modifier = Modifier.padding(top = Spacing.s), horizontalArrangement = Arrangement.spacedBy(Spacing.s)) {
                Chip("Mijoz: ${account.customerCount}")
                Chip("Qarz: ${formatAmount(account.totalDebt)}")
            }
            Text(
                "Ro'yxatdan: ${formatDate(account.createdAt)}",
                style = AppType.caption.copy(color = AppColors.slate400),
                modifier = Modifier.padding(top = Spacing.s),
            )

            Row(
                modifier = Modifier.padding(top = Spacing.m),
                horizontalArrangement = Arrangement.spacedBy(Spacing.s),
            ) {
                ActionButton("Login", onClick = onEditEmail, modifier = Modifier.weight(1f))
                ActionButton("Parol", onClick = onEditPassword, modifier = Modifier.weight(1f))
                ActionButton("O'chirish", destructive = true, onClick = onDelete, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun Chip(text: String) {
    Text(
        text,
        style = AppType.label.copy(color = AppColors.slate500),
        modifier = Modifier
            .clip(RoundedCornerShape(Radius.round))
            .background(Color(0x0F2F6FD6))
            .padding(horizontal = Spacing.m, vertical = Spacing.xs),
    )
}

@Composable
private fun ActionButton(
    label: String,
    modifier: Modifier = Modifier,
    destructive: Boolean = false,
    onClick: () -> Unit,
) {
    val color = if (destructive) AppColors.debtRed else AppColors.accentBlue600
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(Radius.avatarBox))
            .border(1.dp, color.copy(alpha = 0.45f), RoundedCornerShape(Radius.avatarBox))
            .background(color.copy(alpha = 0.06f))
            .clickable(onClick = onClick)
            .padding(vertical = Spacing.s),
        contentAlignment = Alignment.Center,
    ) {
        Text(label, style = AppType.label.copy(color = color))
    }
}

@Composable
private fun AdminEditDialog(
    title: String,
    label: String,
    initial: String,
    secure: Boolean,
    keyboardType: KeyboardType,
    confirmLabel: String,
    busy: Boolean,
    minLength: Int = 1,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var text by remember { mutableStateOf(initial) }
    val canSave = text.trim().length >= minLength && !busy

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Column(
            modifier = Modifier
                .padding(horizontal = Spacing.xl)
                .fillMaxWidth()
                .clip(RoundedCornerShape(Radius.hero))
                .background(AppColors.white)
                .padding(Spacing.xl),
        ) {
            Text(title, style = AppType.h1, modifier = Modifier.padding(bottom = Spacing.lg))
            GlassInput(
                label = label,
                value = text,
                onValueChange = { text = it },
                secure = secure,
                keyboardType = keyboardType,
                autoCapitalizeNone = true,
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = Spacing.s),
                horizontalArrangement = Arrangement.spacedBy(Spacing.m),
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(54.dp)
                        .clip(RoundedCornerShape(Radius.card))
                        .background(Color(0x1A6B7387))
                        .clickable(onClick = onDismiss),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("Bekor", style = AppType.h2.copy(color = AppColors.ink700))
                }
                Box(Modifier.weight(1f)) {
                    PrimaryButton(label = confirmLabel, onClick = { onConfirm(text.trim()) }, loading = busy, enabled = canSave)
                }
            }
        }
    }
}
