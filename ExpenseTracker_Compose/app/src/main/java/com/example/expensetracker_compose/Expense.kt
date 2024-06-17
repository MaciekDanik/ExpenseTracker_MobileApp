package com.example.expensetracker_compose

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int =0,
    val amount: Double,
    val type: String,
    val title: String,
    val description: String,
    val date: LocalDate = LocalDate.now()
//    val type: ExpenseType
)