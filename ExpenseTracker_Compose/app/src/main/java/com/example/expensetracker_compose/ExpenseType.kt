package com.example.expensetracker_compose

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "types")
data class ExpenseType(
    @PrimaryKey(autoGenerate = true) val id: Long =0,
    val type: String
)