package uz.hisobim.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import uz.hisobim.app.ui.theme.AppColors
import uz.hisobim.app.ui.theme.AppType
import uz.hisobim.app.ui.theme.AppWeight
import uz.hisobim.app.ui.theme.BlueGradient
import uz.hisobim.app.ui.theme.GlassVariant
import uz.hisobim.app.ui.theme.Radius
import uz.hisobim.app.ui.theme.Spacing
import uz.hisobim.app.ui.theme.heroShadow

// ── Glass input maydoni ─────────────────────────────────────────
@Composable
fun GlassInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    error: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    secure: Boolean = false,
    multiline: Boolean = false,
    suffix: String? = null,
    autoFocus: Boolean = false,
    autoCapitalizeNone: Boolean = false,
) {
    val focusRequester = remember { FocusRequester() }
    if (autoFocus) {
        LaunchedEffect(Unit) { focusRequester.requestFocus() }
    }

    // TextFieldValue orqali kursorni boshqaramiz: tashqi `value` formatlanib
    // o'zgarsa (masalan +998 telefon yoki summa guruhlanishi), kursorni matn
    // oxiriga surib qo'yamiz — aks holda kursor qotib, yozib bo'lmaydi.
    var fieldValue by remember { mutableStateOf(TextFieldValue(value, TextRange(value.length))) }
    if (fieldValue.text != value) {
        fieldValue = fieldValue.copy(text = value, selection = TextRange(value.length))
    }

    // Parol maydonida ko'z ikonasi holati (ko'rsatish / yashirish).
    var passwordVisible by remember { mutableStateOf(false) }

    Column(modifier.padding(bottom = Spacing.lg)) {
        Text(
            label,
            style = AppType.label,
            modifier = Modifier.padding(bottom = Spacing.sm, start = Spacing.xs),
        )
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            variant = GlassVariant.Input,
            shape = RoundedCornerShape(Radius.input),
            shadow = false,
            borderColor = if (error != null) AppColors.debtRed else AppColors.slate100,
        ) {
            Row(
                verticalAlignment = if (multiline) Alignment.Top else Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = if (multiline) 84.dp else 50.dp)
                        .padding(
                            start = Spacing.lg,
                            end = if (suffix != null || secure) Spacing.sm else Spacing.lg,
                            top = Spacing.m,
                            bottom = Spacing.m,
                        ),
                    contentAlignment = if (multiline) Alignment.TopStart else Alignment.CenterStart,
                ) {
                    BasicTextField(
                        value = fieldValue,
                        onValueChange = {
                            fieldValue = it
                            if (it.text != value) onValueChange(it.text)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(if (autoFocus) Modifier.focusRequester(focusRequester) else Modifier),
                        singleLine = !multiline,
                        maxLines = if (multiline) 6 else 1,
                        textStyle = AppType.body.copy(fontWeight = AppWeight.medium),
                        cursorBrush = SolidColor(AppColors.accentBlue600),
                        visualTransformation = if (secure && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = keyboardType,
                            capitalization = if (autoCapitalizeNone) KeyboardCapitalization.None else KeyboardCapitalization.Sentences,
                        ),
                        decorationBox = { inner ->
                            if (value.isEmpty()) {
                                Text(
                                    placeholder,
                                    style = AppType.body.copy(
                                        fontWeight = AppWeight.medium,
                                        color = AppColors.slate300,
                                    ),
                                )
                            }
                            inner()
                        },
                    )
                }
                if (secure) {
                    Icon(
                        imageVector = if (passwordVisible) AppIcons.eyeOff else AppIcons.eye,
                        contentDescription = if (passwordVisible) "Parolni yashirish" else "Parolni ko'rsatish",
                        tint = AppColors.slate400,
                        modifier = Modifier
                            .padding(end = Spacing.md)
                            .size(22.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                            ) { passwordVisible = !passwordVisible },
                    )
                }
                if (suffix != null) {
                    Text(
                        suffix,
                        style = AppType.label.copy(color = AppColors.slate400, fontSize = 13.sp),
                        modifier = Modifier.padding(end = Spacing.lg),
                    )
                }
            }
        }
        if (error != null) {
            Text(
                error,
                style = AppType.caption.copy(color = AppColors.debtRed),
                modifier = Modifier.padding(top = Spacing.xs, start = Spacing.xs),
            )
        }
    }
}

// ── Asosiy (Primary) tugma — ko'k gradient ──────────────────────
@Composable
fun PrimaryButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
) {
    val disabled = !enabled || loading
    val shape = RoundedCornerShape(Radius.card)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .then(if (disabled) Modifier.alpha(0.5f) else Modifier)
            .heroShadow(shape)
            .clip(shape)
            .background(Brush.linearGradient(BlueGradient))
            .clickable(enabled = !disabled, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        if (loading) {
            CircularProgressIndicator(
                color = AppColors.white,
                strokeWidth = 2.dp,
                modifier = Modifier.size(24.dp),
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
            ) {
                if (leadingIcon != null) {
                    Icon(leadingIcon, contentDescription = null, tint = AppColors.white)
                }
                Text(label, style = AppType.h2.copy(color = AppColors.white, fontSize = 15.sp))
            }
        }
    }
}

// ── Segment toggle (Qarz / To'lov) ──────────────────────────────
@Composable
fun SegmentToggle(
    value: String,
    onChange: (String) -> Unit,
    options: List<Pair<String, String>>, // value to label
) {
    GlassCard(
        variant = GlassVariant.Segment,
        shape = RoundedCornerShape(Radius.segment),
        shadow = false,
    ) {
        Row(
            modifier = Modifier.padding(Spacing.xs),
            horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
        ) {
            options.forEach { (optValue, optLabel) ->
                val active = optValue == value
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(Radius.avatarBox))
                        .then(
                            if (active) Modifier.background(Brush.linearGradient(BlueGradient))
                            else Modifier,
                        )
                        .clickable { onChange(optValue) }
                        .padding(vertical = Spacing.m),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        optLabel,
                        style = AppType.label.copy(
                            color = if (active) AppColors.white else AppColors.slate400,
                            letterSpacing = 0.sp,
                        ),
                    )
                }
            }
        }
    }
}
