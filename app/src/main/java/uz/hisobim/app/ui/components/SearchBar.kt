package uz.hisobim.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import uz.hisobim.app.ui.theme.AppColors
import uz.hisobim.app.ui.theme.AppType
import uz.hisobim.app.ui.theme.AppWeight
import uz.hisobim.app.ui.theme.GlassVariant
import uz.hisobim.app.ui.theme.Radius
import uz.hisobim.app.ui.theme.Spacing

/** RN'dagi `SearchBar.tsx` ekvivalenti. */
@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "Ism yoki telefon...",
) {
    Box(Modifier.padding(horizontal = Spacing.md, vertical = Spacing.s)) {
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            variant = GlassVariant.Input,
            shape = RoundedCornerShape(Radius.input),
            shadow = false,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = Spacing.lg),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.s),
            ) {
                Icon(
                    AppIcons.search,
                    contentDescription = null,
                    tint = AppColors.slate400,
                    modifier = Modifier.size(18.dp),
                )
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    textStyle = AppType.body.copy(fontWeight = AppWeight.medium),
                    cursorBrush = SolidColor(AppColors.accentBlue600),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(imeAction = ImeAction.Search),
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
        }
    }
}
