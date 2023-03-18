package com.example.roomdemo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeeDao {

    @Insert
    suspend fun insertEmployee(employeeEntity: EmployeeEntity)

    @Delete
    suspend fun deleteEmployee(employeeEntity: EmployeeEntity)

    @Update
    suspend fun updateEmployee(employeeEntity: EmployeeEntity)

    @Query("SELECT * FROM 'employee-table'")
    fun fetchAllEmployees(): Flow<List<EmployeeEntity>>
    // Flow is a class that allows you to emit multiple values sequentially,
    // just like a stream (sequence of emitted values).
    // when the data changes, the flow will emit the new data.

    @Query("SELECT * FROM 'employee-table' WHERE id = :id")
    fun fetchEmployeeById(id:Int): Flow<List<EmployeeEntity>>
}
