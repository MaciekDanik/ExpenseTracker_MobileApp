package com.example.expensetracker_compose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class ExpenseViewModel(
    private val dao: ExpenseDao
): ViewModel() {
    private val _sortType = MutableStateFlow(SortType.DATE)
    private val _expenses = _sortType
        .flatMapLatest { sortType ->
            when(sortType){
                SortType.AMOUNT -> dao.getExpensesOrderedByAmount()
                SortType.TITLE -> dao.getExpensesOrderedByTitle()
                SortType.TYPE -> dao.getExpensesOrderedByType()
                SortType.DATE -> dao.getExpensesOrderedByDate()
                //SortType.SPECIFIC -> dao.filterExpensesByType()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(ExpenseState())
    val state = combine(_state, _sortType, _expenses){state, sortType, expenses ->
        state.copy(
            expenses = expenses,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ExpenseState())

    fun onEvent(event: ExpenseEvent){
        when(event){
            is ExpenseEvent.DeleteExpense -> {
                viewModelScope.launch {
                    dao.deleteExpense(event.expense)
                }
            }
            ExpenseEvent.HideDialog -> {
                _state.update { it.copy(
                    isAddingExpense = false
                ) }
            }
            ExpenseEvent.HideFilterDialog -> {
                _state.update { it.copy(
                    isFiltering = false
                ) }
            }
            ExpenseEvent.HideDetailDialog ->{
                _state.update { it.copy(
                    isDetail = false
                ) }
            }
            ExpenseEvent.SaveExpense -> {
                val amount = state.value.amount
                val title = state.value.title
                val type = state.value.type
                val description = state.value.description
                val date = state.value.date

                if(amount.isNaN() || amount <= 0.0 || title.isBlank() || type.isBlank()){
                    return
                }

                val expense = Expense(
                    amount = amount,
                    title = title,
                    type = type,
                    description = description,
                    date = date
                )
                viewModelScope.launch {
                    dao.upsertExpense(expense)
                }
                _state.update { it.copy(
                    isAddingExpense = false,
                    amount = 0.0,
                    title = "",
                    type = "",
                    description = "",
                    date = LocalDate.now()
                ) }
            }
            is ExpenseEvent.SetAmount -> {
                _state.update { it.copy(
                    amount = event.amount
                ) }
            }
            is ExpenseEvent.SetTitle -> {
                _state.update { it.copy(
                    title = event.title
                ) }
            }
            is ExpenseEvent.SetType -> {
                _state.update { it.copy(
                    type = event.type
                ) }
            }
            is ExpenseEvent.SetDate -> {
                _state.update { it.copy(
                    date = event.date
                ) }
            }
            is ExpenseEvent.SetDescription -> {
                _state.update { it.copy(
                    description = event.description
                ) }
            }
            ExpenseEvent.ShowDialog -> {
                _state.update { it.copy(
                    isAddingExpense = true
                ) }
            }
            ExpenseEvent.ShowFilterDialog -> {
                _state.update { it.copy(
                    isFiltering = true
                ) }
            }
            ExpenseEvent.ShowDetailDialog -> {
                _state.update { it.copy(
                    isDetail = true
                ) }
            }
            is ExpenseEvent.SortExpenses -> {
                _sortType.value = event.sortType
            }
            is ExpenseEvent.FilterExpenses ->{
                viewModelScope.launch {
                    dao.filterExpensesByType(event.title)
                }
            }
        }
    }
}