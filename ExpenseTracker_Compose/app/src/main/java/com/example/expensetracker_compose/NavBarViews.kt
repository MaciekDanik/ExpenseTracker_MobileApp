package com.example.expensetracker_compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Divider
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetracker_compose.ui.theme.MyDarkPurple
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen2(
    state: ExpenseState,
    onEvent: (ExpenseEvent) -> Unit
){
    var kwota by rememberSaveable { mutableStateOf(0.0) }
    var limit by rememberSaveable { mutableStateOf(3200.00) }
    var date by rememberSaveable { mutableStateOf(LocalDate.now()) }

    LaunchedEffect(Unit) {
        kwota = 0.0
        state.expenses.forEach{
            if(it.date.month == date.month) {
                kwota += it.amount
            }

        }
    }


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(ExpenseEvent.ShowDialog)
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add expense"
                )
            }
        },
        containerColor = colorResource(id = R.color.MyLightBlue),
        floatingActionButtonPosition = FabPosition.Center
    ) {padding ->
        if (state.isAddingExpense){
            AddExpenseDialog(state = state, onEvent = onEvent)
        }
        if (state.isDetail){
            DetailDialog(state = state, onEvent = onEvent)
        }
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically

                ){
                    Text(
                        text = "Spent: " + kwota.toString() + " this month.",
                        fontSize = 28.sp,
                        fontStyle = FontStyle(3),
                        fontWeight = FontWeight.Bold,
//                        modifier = Modifier.padding(padding),
                        textAlign = TextAlign.Center

                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
            items(state.expenses.take(5)) { expense ->
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${expense.amount.toString()} - ${expense.title}",
                            fontSize = 20.sp
                        )
                        Text(
                            text = expense.type,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = expense.description,
                            fontSize = 12.sp
                        )
                    }
                    IconButton(onClick = {
                        onEvent(ExpenseEvent.ShowDetailDialog)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Details"
                        )
                    }
                }
            }
        }

        Box(modifier = Modifier.fillMaxSize()){
            Text(
                text = "You're ${limit-kwota} under the limit.",
                fontSize = 22.sp,
                fontStyle = FontStyle(3),
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(vertical = 120.dp)

            )
        }
    }
}

@Composable
fun HistoryScreen(
    state: ExpenseState,
    onEvent: (ExpenseEvent) -> Unit
){
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(ExpenseEvent.ShowFilterDialog)
            }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Filter by"
                )
            }
        },
        containerColor = colorResource(id = R.color.MyLightBlue),
        floatingActionButtonPosition = FabPosition.Center
    ) {padding ->
        if (state.isFiltering){
            FilterDialog(state = state, onEvent = onEvent)
        }
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically

                ){
                    SortType.values().forEach { sortType ->
                        Row(
                            modifier = Modifier
                                .clickable {
                                    onEvent(ExpenseEvent.SortExpenses(sortType))
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = state.sortType == sortType,
                                onClick = {
                                    onEvent(ExpenseEvent.SortExpenses(sortType))
                                }
                            )
                            Text(text = sortType.name)
                        }
                    }
                }
            }
            items(state.expenses){expense ->
                Row(
                    modifier = Modifier.fillMaxWidth()
                ){
                    Column (
                        modifier = Modifier.weight(1f)
                    ){
                        Text(
                            text = "${expense.amount.toString()} - ${expense.title} - ${expense.date}",
                            fontSize = 20.sp
                        )
                        Text(
                            text = expense.type,
                            fontSize = 16.sp
                        )
                        Text(
                            text = expense.description,
                            fontSize = 12.sp
                        )
                    }
                    IconButton(onClick = {
                        onEvent(ExpenseEvent.DeleteExpense(expense))
                    }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete expense")
                    }
                }
            }
        }
    }
}

@Composable
fun StateScreen(
    state: ExpenseState,
    onEvent: (ExpenseEvent) -> Unit
){
    var kwota by rememberSaveable { mutableStateOf(0.0) }
    val array by rememberSaveable { mutableStateOf(Array(7){0.0}) }
    var displayingPercent by rememberSaveable { mutableStateOf(false) }
    val date by rememberSaveable { mutableStateOf(LocalDate.now()) }
    val hashMapOfTypes = hashMapOf<String,Int>(
        "Food" to 0,
        "Fun & Hobbies" to 1,
        "Housing" to 2,
        "Maintenance" to 3,
        "Other" to 4,
        "Taxes" to 5,
        "Utilities" to 6
    )

    LaunchedEffect(Unit) {
        array.fill(0.0)
        state.expenses.forEach{expense ->
            if(expense.date.month == date.month) {
                kwota += state.amount
                hashMapOfTypes.forEach{
                    if (it.key == expense.type)
                    {
                        array[it.value] += expense.amount
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.MyLightBlue))
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = "Spending habits this month.",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 32.sp
        )
        hashMapOfTypes.forEach{
            if (displayingPercent == false) {
                Text(
                    text = "You've spent ${array[it.value]} on ${it.key}",
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    fontSize = 21.sp
                )
                Spacer(modifier = Modifier.height(5.dp))
            }
            else{
                var zmienna: Double = array[it.value] / kwota * 100.0
                Text(
//                    text = "You've spent ${array[it.value] / kwota * 100}% on ${it.key}",
                    text = "You've spent ${zmienna}% on ${it.key}",
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    fontSize = 21.sp
                )
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
        Spacer(modifier = Modifier.height(25.dp))


        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                modifier = Modifier.padding(vertical = 125.dp),
                onClick = { displayingPercent = !displayingPercent }) {
                Text(text = "Displaying type")
            }
        }
    }
}