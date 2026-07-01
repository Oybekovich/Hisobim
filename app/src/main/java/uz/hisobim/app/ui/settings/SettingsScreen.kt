package uz.hisobim.app.ui.settings

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import uz.hisobim.app.R
import uz.hisobim.app.ui.AppViewModel
import uz.hisobim.app.ui.components.AppIcons
import uz.hisobim.app.ui.components.ConfirmDialog
import uz.hisobim.app.ui.components.GlassBackground
import uz.hisobim.app.ui.components.GlassCard
import uz.hisobim.app.ui.components.HeroCard
import uz.hisobim.app.ui.theme.AppColors
import uz.hisobim.app.ui.theme.AppType
import uz.hisobim.app.ui.theme.AppWeight
import uz.hisobim.app.ui.theme.Radius
import uz.hisobim.app.ui.theme.Spacing

private const val APP_VERSION = "1.0.0"

@Composable
fun SettingsScreen(
    appViewModel: AppViewModel,
    onLoggedOut: () -> Unit,
    onOpenDevices: () -> Unit,
    viewModel: SettingsViewModel = viewModel(),
) {
    val context = LocalContext.current
    val shop by appViewModel.activeShop.collectAsStateWithLifecycle()
    val user = appViewModel.currentUser()
    val email = user?.email ?: "—"

    var confirmLogout by rememberSaveable { mutableStateOf(false) }

    GlassBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(start = Spacing.md, end = Spacing.md, top = Spacing.md, bottom = 90.dp),
        ) {
            // ── Profil hero kartasi ──
            HeroCard(modifier = Modifier.fillMaxWidth().padding(bottom = Spacing.lg)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(Spacing.xl),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Spacing.lg),
                ) {
                    Box(
                        modifier = Modifier.size(58.dp).clip(CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        Image(
                            painter = painterResource(R.drawable.owner_avatar),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize().scale(1.4f),
                        )
                    }
                    Column(Modifier.weight(1f)) {
                        Text(email, style = AppType.h2.copy(color = AppColors.white), maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(
                            "Do'kon egasi",
                            style = AppType.caption.copy(color = AppColors.white.copy(alpha = 0.75f)),
                            modifier = Modifier.padding(top = 2.dp),
                        )
                    }
                }
            }

            // ── Do'kon paneli ──
            Text(
                "DO'KON",
                style = AppType.overline,
                modifier = Modifier.padding(bottom = Spacing.s, start = Spacing.xs),
            )
            GlassCard(modifier = Modifier.fillMaxWidth().padding(bottom = Spacing.lg)) {
                Column {
                    // Do'kon nomi
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = Spacing.lg, vertical = Spacing.m),
                        verticalAlignment = if (viewModel.editingName) Alignment.Top else Alignment.CenterVertically,
                    ) {
                        SettingsRowIcon(AppIcons.storefrontOutline)
                        Column(Modifier.weight(1f)) {
                            Text("Do'kon nomi", style = AppType.label, modifier = Modifier.padding(bottom = 3.dp))
                            if (viewModel.editingName) {
                                BasicTextField(
                                    value = viewModel.nameDraft,
                                    onValueChange = viewModel::onNameChange,
                                    singleLine = true,
                                    textStyle = AppType.body,
                                    cursorBrush = SolidColor(AppColors.accentBlue600),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = Spacing.s)
                                        .clip(RoundedCornerShape(Radius.input))
                                        .background(Color(0x99FFFFFF)) // rgba(255,255,255,0.6)
                                        .border(1.dp, Color(0xCCFFFFFF), RoundedCornerShape(Radius.input))
                                        .padding(horizontal = Spacing.m, vertical = Spacing.s),
                                )
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.m)) {
                                    val canSave = viewModel.nameDraft.trim().isNotEmpty() && !viewModel.saving
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(Radius.avatarBox))
                                            .background(AppColors.accentBlue600)
                                            .clickable(enabled = canSave) {
                                                shop?.let { s ->
                                                    viewModel.saveName(
                                                        shopId = s.id,
                                                        onSuccess = { updated -> appViewModel.setShop(updated) },
                                                        onError = {
                                                            Toast.makeText(context, "Do'kon nomini saqlashda xatolik yuz berdi", Toast.LENGTH_SHORT).show()
                                                        },
                                                    )
                                                }
                                            }
                                            .padding(horizontal = Spacing.lg, vertical = Spacing.s),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        if (viewModel.saving) {
                                            CircularProgressIndicator(color = AppColors.white, strokeWidth = 2.dp, modifier = Modifier.size(14.dp))
                                        } else {
                                            Text("Saqlash", style = AppType.label.copy(color = AppColors.white))
                                        }
                                    }
                                    Text(
                                        "Bekor",
                                        style = AppType.label.copy(color = AppColors.slate400),
                                        modifier = Modifier.clickable { viewModel.cancelEdit(shop?.name ?: "") },
                                    )
                                }
                            } else {
                                Text(shop?.name ?: "—", style = AppType.body)
                            }
                        }
                        if (!viewModel.editingName) {
                            Row(
                                modifier = Modifier
                                    .padding(start = Spacing.m)
                                    .clip(RoundedCornerShape(Radius.round))
                                    .background(Color(0x1A2F6FD6)) // rgba(47,111,214,0.10)
                                    .border(1.dp, Color(0x2E2F6FD6), RoundedCornerShape(Radius.round))
                                    .clickable { viewModel.startEdit(shop?.name ?: "") }
                                    .padding(horizontal = Spacing.m, vertical = Spacing.sm),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp),
                            ) {
                                Icon(AppIcons.pencil, contentDescription = null, tint = AppColors.accentBlue600, modifier = Modifier.size(13.dp))
                                Text("Tahrirlash", style = AppType.label.copy(color = AppColors.accentBlue600))
                            }
                        }
                    }

                    // Chegara
                    Box(Modifier.fillMaxWidth().height(1.dp).background(Color(0x66CBD1DC)))

                    // Email
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = Spacing.lg, vertical = Spacing.m),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        SettingsRowIcon(AppIcons.mailOutline)
                        Column(Modifier.weight(1f)) {
                            Text("Email", style = AppType.label, modifier = Modifier.padding(bottom = 3.dp))
                            Text(email, style = AppType.body, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                    }
                }
            }

            // ── Xavfsizlik paneli ──
            Text(
                "XAVFSIZLIK",
                style = AppType.overline,
                modifier = Modifier.padding(bottom = Spacing.s, start = Spacing.xs),
            )
            GlassCard(modifier = Modifier.fillMaxWidth().padding(bottom = Spacing.lg)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onOpenDevices)
                        .padding(horizontal = Spacing.lg, vertical = Spacing.m),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    SettingsRowIcon(AppIcons.phonePortraitOutline)
                    Column(Modifier.weight(1f)) {
                        Text("Kirgan qurilmalar", style = AppType.body, modifier = Modifier.padding(bottom = 3.dp))
                        Text("Hisobga kirgan qurilmalarni ko'rish va chiqarish", style = AppType.caption)
                    }
                    Icon(
                        AppIcons.chevronForward,
                        contentDescription = null,
                        tint = AppColors.slate300,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }

            // ── Chiqish tugmasi ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(Radius.card))
                    .border(1.dp, Color(0x80C0504C), RoundedCornerShape(Radius.card)) // rgba(192,80,76,0.5)
                    .background(Color(0x0AC0504C)) // rgba(192,80,76,0.04)
                    .clickable { confirmLogout = true }
                    .padding(vertical = Spacing.lg),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(AppIcons.logOut, contentDescription = null, tint = AppColors.debtRed, modifier = Modifier.size(20.dp))
                Text(
                    "Ilovadan chiqish",
                    style = AppType.body.copy(fontWeight = AppWeight.bold, color = AppColors.debtRed),
                    modifier = Modifier.padding(start = Spacing.s),
                )
            }

            Text(
                "Hisobim v$APP_VERSION",
                style = AppType.caption.copy(color = AppColors.slate300),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(top = Spacing.lg),
            )
        }
    }

    ConfirmDialog(
        visible = confirmLogout,
        title = "Chiqish",
        message = "Ilovadan chiqishni tasdiqlaysizmi?",
        confirmLabel = "Chiqish",
        icon = AppIcons.logOut,
        onConfirm = {
            confirmLogout = false
            appViewModel.signOut(onLoggedOut)
        },
        onDismiss = { confirmLogout = false },
    )
}

@Composable
private fun SettingsRowIcon(icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Box(
        modifier = Modifier
            .padding(end = Spacing.m)
            .size(38.dp)
            .clip(RoundedCornerShape(Radius.avatarBox))
            .background(Color(0x1A2F6FD6)), // rgba(47,111,214,0.10)
        contentAlignment = Alignment.Center,
    ) {
        Icon(icon, contentDescription = null, tint = AppColors.accentBlue600, modifier = Modifier.size(20.dp))
    }
}
