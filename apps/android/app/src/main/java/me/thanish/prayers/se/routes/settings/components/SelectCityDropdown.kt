package me.thanish.prayers.se.routes.settings.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.thanish.prayers.se.R
import me.thanish.prayers.se.domain.PrayerTimeCity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectCityDropdown(
    city: PrayerTimeCity,
    onCityChange: (PrayerTimeCity) -> Unit
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.route_settings_city),
                fontSize = 12.sp,
                textAlign = TextAlign.Left,
                modifier = Modifier.weight(2f)
            )
            Spacer(modifier = Modifier.width(20.dp))
            TextButton(
                onClick = { expanded = true }, modifier = Modifier
                    .weight(3f)
                    .menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    city.getLabel(context),
                    fontSize = 12.sp
                )
            }
        }

        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            PrayerTimeCity.entries.forEach { option ->
                DropdownMenuItem(
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    text = {
                        Text(
                            option.getLabel(context),
                            fontSize = 12.sp
                        )
                    },
                    onClick = {
                        onCityChange(option)
                        expanded = false
                    },
                )
            }
        }
    }
}
