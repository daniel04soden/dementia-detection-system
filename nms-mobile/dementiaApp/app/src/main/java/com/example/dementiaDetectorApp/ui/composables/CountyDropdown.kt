package com.example.dementiaDetectorApp.ui.composables

import android.util.Log
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
import com.example.dementiaDetectorApp.ui.theme.outLinedTFColours

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountyDropdown(
    selectedCounty: String,
    onCountySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val counties = listOf(
        "Antrim", "Armagh", "Carlow", "Cavan", "Clare", "Cork",
        "Derry", "Donegal", "Down", "Dublin", "Fermanagh", "Galway",
        "Kerry", "Kildare", "Kilkenny", "Laois", "Leitrim", "Limerick",
        "Longford", "Louth", "Mayo", "Meath", "Monaghan", "Offaly",
        "Roscommon", "Sligo", "Tipperary", "Tyrone", "Waterford",
        "Westmeath", "Wexford", "Wicklow"
    )
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            readOnly = true,
            value = selectedCounty,
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
            counties.forEach { county ->
                DropdownMenuItem(
                    text = { Text(county) },
                    onClick = {
                        onCountySelected(county)
                        expanded = false
                    }
                )
            }
        }
    }
}
