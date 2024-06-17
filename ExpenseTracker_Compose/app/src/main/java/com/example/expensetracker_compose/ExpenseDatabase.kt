package com.example.expensetracker_compose

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Expense::class],
    version = 1
)
@TypeConverters(DateConverter::class)
abstract class ExpenseDatabase: RoomDatabase() {
    abstract val dao: ExpenseDao
}