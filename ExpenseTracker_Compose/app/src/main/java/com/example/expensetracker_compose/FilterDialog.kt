package com.example.expensetracker_compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vanpra.composematerialdialogs.rememberMaterialDialogState

@Composable
fun FilterDialog(
    state: ExpenseState,
    onEvent: (ExpenseEvent) -> Unit,
    modifier: Modifier = Modifier,
) {

    val dateDialogState = rememberMaterialDialogState()
    var filterTitle by rememberSaveable { mutableStateOf("") }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(ExpenseEvent.HideFilterDialog)
        },
        title = { Text(text = "Filter expenses")},
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                TextField(
                    value = filterTitle,
                    onValueChange = {
                        filterTitle = it
                    },
                    placeholder = {
                        Text(text = "Filter by")
                    }
                )

            }
        },
        buttons = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Button(onClick = {
                    onEvent(ExpenseEvent.FilterExpenses(filterTitle))
                }) {
                    Text(text = "Filter")
                }
            }
        }
    )
}