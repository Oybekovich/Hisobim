package uz.hisobim.app.ui.devices

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import uz.hisobim.app.data.model.UserDevice
import uz.hisobim.app.ui.AppViewModel
import uz.hisobim.app.ui.components.AppIcons
import uz.hisobim.app.ui.components.ConfirmDialog
import uz.hisobim.app.ui.components.EmptyState
import uz.hisobim.app.ui.components.GlassBackground
import uz.hisobim.app.ui.components.GlassCard
import uz.hisobim.app.ui.components.SimpleTopBar
import uz.hisobim.app.ui.theme.AppColors
import uz.hisobim.app.ui.theme.AppType
import uz.hisobim.app.ui.theme.AppWeight
import uz.hisobim.app.ui.theme.Radius
import uz.hisobim.app.ui.theme.Spacing
import uz.hisobim.app.util.formatDate

@Composable
fun DevicesScreen(
    appViewModel: AppViewModel,
    onBack: () -> Unit,
    onLoggedOut: () -> Unit,
    viewModel: DevicesViewModel = viewModel(),
) {
    val context = LocalContext.current
    val userId = appViewModel.currentUser()?.id

    LaunchedEffect(userId) { viewModel.load(userId) }

    viewModel.errorMsg?.let { msg ->
        LaunchedEffect(msg) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }

    var pending by rememberSaveable { mutableStateOf<String?>(null) }
    val devices = viewModel.state.data ?: emptyList()
    val pendingDevice = devices.firstOrNull { it.id == pending }

    GlassBackground {
        Column(Modifier.fillMaxSize()) {
            SimpleTopBar(title = "Kirgan qurilmalar", onBack = onBack)

            when {
                viewModel.state.loading && viewModel.state.data == null -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = AppColors.accentBlue600)
                    }
                }

                devices.isEmpty() -> {
                    Box(Modifier.fillMaxSize()) {
                        EmptyState(
                            message = "Qurilma topilmadi",
                            icon = AppIcons.phonePortraitOutline,
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(
                            start = Spacing.md, end = Spacing.md, top = Spacing.s, bottom = 40.dp,
                        ),
                        verticalArrangement = Arrangement.spacedBy(Spacing.m),
                    ) {
                        item {
                            Text(
                                "Hisobingizga kirgan qurilmalar. Notanish qurilmani ro'yxatdan chiqarib yuboring.",
                                style = AppType.caption,
                                modifier = Modifier.padding(bottom = Spacing.s, start = Spacing.xs, end = Spacing.xs),
                            )
                        }
                        items(devices, key = { it.id }) { device ->
                            DeviceRow(
                                device = device,
                                isCurrent = viewModel.isCurrent(device),
                                revoking = viewModel.revokingId == device.id,
                                onRevoke = { pending = device.id },
                            )
                        }
                    }
                }
            }
        }
    }

    if (pendingDevice != null) {
        val current = viewModel.isCurrent(pendingDevice)
        ConfirmDialog(
            visible = true,
            title = if (current) "Ushbu qurilmadan chiqish" else "Qurilmani chiqarish",
            message = if (current) {
                "Bu — joriy qurilma. Chiqarsangiz, ilovadan ham chiqasiz."
            } else {
                "\"${pendingDevice.model ?: "Qurilma"}\" hisobingizdan chiqariladi va qayta kirish talab qilinadi."
            },
            confirmLabel = "Chiqarish",
            icon = AppIcons.logOut,
            onConfirm = {
                val device = pendingDevice
                pending = null
                viewModel.revoke(device, userId, onSelfRevoked = { appViewModel.signOut(onLoggedOut) })
            },
            onDismiss = { pending = null },
        )
    }
}

@Composable
private fun DeviceRow(
    device: UserDevice,
    isCurrent: Boolean,
    revoking: Boolean,
    onRevoke: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    GlassCard(modifier = Modifier.fillMaxWidth(), onClick = { expanded = !expanded }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
                .padding(horizontal = Spacing.lg, vertical = Spacing.m),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .padding(end = Spacing.m)
                    .size(38.dp)
                    .clip(RoundedCornerShape(Radius.avatarBox))
                    .background(Color(0x1A2F6FD6)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    AppIcons.phonePortraitOutline,
                    contentDescription = null,
                    tint = AppColors.accentBlue600,
                    modifier = Modifier.size(20.dp),
                )
            }

            Column(Modifier.weight(1f).padding(end = Spacing.m)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        device.model ?: "Android qurilma",
                        style = AppType.body.copy(fontWeight = AppWeight.bold),
                        maxLines = if (expanded) Int.MAX_VALUE else 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false),
                    )
                    if (isCurrent) {
                        Text(
                            "Shu qurilma",
                            style = AppType.label.copy(color = AppColors.accentBlue600),
                            modifier = Modifier
                                .padding(start = Spacing.s)
                                .clip(RoundedCornerShape(Radius.round))
                                .background(Color(0x1A2F6FD6))
                                .padding(horizontal = Spacing.s, vertical = 2.dp),
                        )
                    }
                }
                Text(
                    "Faol: ${formatDate(device.lastActiveAt)}",
                    style = AppType.caption,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(Radius.round))
                    .border(1.dp, Color(0x80C0504C), RoundedCornerShape(Radius.round))
                    .background(Color(0x0AC0504C))
                    .clickable(enabled = !revoking, onClick = onRevoke)
                    .padding(horizontal = Spacing.m, vertical = Spacing.sm),
                contentAlignment = Alignment.Center,
            ) {
                if (revoking) {
                    CircularProgressIndicator(color = AppColors.debtRed, strokeWidth = 2.dp, modifier = Modifier.size(14.dp))
                } else {
                    Text("Chiqarish", style = AppType.label.copy(color = AppColors.debtRed))
                }
            }
        }
    }
}
