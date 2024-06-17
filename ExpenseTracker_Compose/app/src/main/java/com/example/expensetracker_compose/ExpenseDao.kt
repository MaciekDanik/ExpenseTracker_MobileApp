package com.example.expensetracker_compose

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Upsert
    suspend fun upsertExpense(expense: Expense)
    @Delete
    suspend fun deleteExpense(expense: Expense)
    @Query("SELECT * FROM expenses ORDER BY amount DESC")
    fun getExpensesOrderedByAmount(): Flow<List<Expense>>

    @Query("SELECT * FROM expenses ORDER BY title ASC")
    fun getExpensesOrderedByTitle(): Flow<List<Expense>>

    @Query("SELECT * FROM expenses ORDER BY type ASC")
    fun getExpensesOrderedByType(): Flow<List<Expense>>

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getExpensesOrderedByDate(): Flow<List<Expense>>

    //Wyciąganie po tytule
    @Query("SELECT * FROM expenses WHERE type LIKE :type")
    fun filterExpensesByType(type: String): Flow<List<Expense>>
}