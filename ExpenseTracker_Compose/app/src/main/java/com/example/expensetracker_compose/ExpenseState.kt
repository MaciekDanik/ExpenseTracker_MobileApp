package com.example.expensetracker_compose

import java.time.LocalDate

data class ExpenseState(
    val expenses: List<Expense> = emptyList(),
    val amount: Double = 0.0,
    val title: String = "",
    val type: String = "",
    val description: String = "",
    val date: LocalDate = LocalDate.now(),
    val isAddingExpense: Boolean = false,
    val isFiltering: Boolean = false,
    val isDetail: Boolean = false,
    val sortType: SortType = SortType.DATE
)
