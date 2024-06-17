package com.example.expensetracker_compose

import java.time.LocalDate

sealed interface ExpenseEvent {
    object SaveExpense: ExpenseEvent
    data class SetAmount(val amount: Double): ExpenseEvent
    data class SetTitle(val title: String): ExpenseEvent
    data class SetType(val type: String): ExpenseEvent
    data class SetDate(val date: LocalDate):ExpenseEvent
    data class SetDescription(val description: String): ExpenseEvent
    object ShowDialog: ExpenseEvent
    object HideDialog: ExpenseEvent
    object ShowFilterDialog: ExpenseEvent
    object HideFilterDialog: ExpenseEvent
    object ShowDetailDialog: ExpenseEvent
    object HideDetailDialog: ExpenseEvent
    data class FilterExpenses(val title: String): ExpenseEvent
    data class SortExpenses(val sortType: SortType): ExpenseEvent
    data class DeleteExpense(val expense: Expense): ExpenseEvent
}