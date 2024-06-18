package com.example.expensetracker_compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseDialog(
    state: ExpenseState,
    onEvent: (ExpenseEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    var pickedDate by remember { mutableStateOf(LocalDate.now()) }
    val formatedDate by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("dd MMM yyyy")
                .format(pickedDate)
        }
    }

    val dateDialogState = rememberMaterialDialogState()
    var expanded by remember { mutableStateOf(false) }
    var types = arrayOf("Food", "Fun & Hobbies", "Housing", "Maintenance", "Other", "Taxes", "Utilities")
    var selectedType by remember { mutableStateOf("Type") }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(ExpenseEvent.HideDialog)
        },
        title = { Text(text = "Add expense")},
        text = {
               Column(
                   verticalArrangement = Arrangement.spacedBy(8.dp)
               ){
                   TextField(
                       value = state.amount.toString(),
                       onValueChange = {
                           onEvent(ExpenseEvent.SetAmount(it.toDouble()))
                       },
                       keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                       label = {
                           Text("Amount")
                       }
                   )
                   TextField(
                       value = state.title,
                       onValueChange = {
                           onEvent(ExpenseEvent.SetTitle(it))
                       },
                       label = {
                           Text("Title")
                       }
                   )

                   ExposedDropdownMenuBox(
                       expanded = expanded,
                       onExpandedChange = {expanded = !expanded})
                   {
                       TextField(
                           value = selectedType,
                           onValueChange = {
                               onEvent(ExpenseEvent.SetType(it))
                           },
                           readOnly = true,
                           trailingIcon = {ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)},
                           modifier = Modifier.menuAnchor(),
                           label = {
                               Text("Type")
                           }
                       )
                       ExposedDropdownMenu(
                           expanded = expanded,
                           onDismissRequest = {expanded = false }
                       ) {
                           types.forEach { type ->
                               DropdownMenuItem(
                                   text = { Text(text = type) },
                                   onClick = {
                                       selectedType = type
                                       onEvent(ExpenseEvent.SetType(selectedType))
                                       expanded = false
                                   }
                               )
                           }
                       }
                   }

                   TextField(
                       value = state.description,
                       onValueChange = {
                           onEvent(ExpenseEvent.SetDescription(it))
                       },
                       label = {
                           Text("Description")
                       }
                   )
                   Row (
                       modifier = Modifier
                           .align(Alignment.CenterHorizontally)
                           .padding(8.dp)
                   ){
                       Text(
                           text = formatedDate,
                           modifier = Modifier
                               .align(Alignment.CenterVertically)
                               .padding(vertical = 8.dp)
                       )
                       Button(
                           onClick = { 
                               dateDialogState.show()
                           }) {
                           Text(text = "Pick date")
                       }
                   }
               }
        },
        buttons = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Button(onClick = {
                    onEvent(ExpenseEvent.SaveExpense)
                }) {
                    Text(text = "Save")
                }
            }
        }
    )

    MaterialDialog (
        dialogState = dateDialogState,
        buttons = {
            positiveButton("Ok")
            negativeButton("Cancel")
        }
    ){
        datepicker(
            initialDate = LocalDate.now(),
            title = "Pick a date",

        ){
            pickedDate = it
            onEvent(ExpenseEvent.SetDate(pickedDate))
        }
    }
}