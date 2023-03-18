package com.example.roomdemo

import android.app.Application

class EmployeeApp : Application() {

    // lazy is a delegate that will create the database only when it is needed.
    val db by lazy { EmployeeDatabase.getInstance(this) }
}