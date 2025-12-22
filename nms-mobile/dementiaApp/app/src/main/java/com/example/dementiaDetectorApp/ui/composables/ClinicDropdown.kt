package com.example.dementiaDetectorApp.ui.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.dementiaDetectorApp.models.Clinic
import com.example.dementiaDetectorApp.ui.theme.outLinedTFColours

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClinicDropdown(
    clinics: List<Clinic>,
    selectedClinicId: Int?,
    onClinicSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedName = clinics.find { it.clinicID == selectedClinicId }?.name ?: "Select Clinic"

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            readOnly = true,
            value = selectedName,
            onValueChange = {},
            colors = outLinedTFColours(),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            clinics.forEach { clinic ->
                DropdownMenuItem(
                    text = { Text(clinic.name) },
                    onClick = {
                        onClinicSelected(clinic.clinicID)
                        expanded = false
                    }
                )
            }
        }
    }
}
