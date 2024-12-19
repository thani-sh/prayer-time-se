package me.thanish.prayers.se.routes.settings.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectNotifyBefore(
    notifyBefore: Int, onNotifyBeforeChange: (Int) -> Unit
) {
    val options: List<String> = listOf("Off", "10 min", "15 min", "20 min")
    var expanded by remember { mutableStateOf(false) }

    val selected = when (notifyBefore) {
        1000 * 60 * 10 -> options[1]
        1000 * 60 * 15 -> options[2]
        1000 * 60 * 20 -> options[3]
        else -> options[0]
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Aviseringar",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Right,
                modifier = Modifier.width(120.dp)
            )
            Spacer(modifier = Modifier.width(20.dp))
            TextButton(
                onClick = { expanded = true }, modifier = Modifier
                    .width(120.dp)
                    .menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true)
            ) {
                Text(selected, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, style = MaterialTheme.typography.bodyLarge) },
                    onClick = {
                        val selectedValue = when (option) {
                            "10 min" -> 1000 * 60 * 10
                            "15 min" -> 1000 * 60 * 15
                            "20 min" -> 1000 * 60 * 20
                            else -> -1
                        }
                        onNotifyBeforeChange(selectedValue)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}
