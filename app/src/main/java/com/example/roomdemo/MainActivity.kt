package com.example.roomdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.roomdemo.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        val employeeDao = (application as EmployeeApp).db.employeeDao()
        binding?.btnAdd?.setOnClickListener {
            addRecord(employeeDao)
        }

    }

    private fun addRecord(employeeDao: EmployeeDao) {
        val name = binding?.etName?.text.toString()
        val surname = binding?.etSurname?.text.toString()
        val age = binding?.etAge?.text.toString().toInt()
        val email = binding?.etEmail?.text.toString()

        if (name.isNotEmpty() && surname.isNotEmpty() && age != 0 && email.isNotEmpty()) {
            lifecycleScope.launch {
                employeeDao.insertEmployee(
                    EmployeeEntity(
                        name = name,
                        surname = surname,
                        age = age,
                        email = email
                    )
                )
                Toast.makeText(applicationContext, "Record Added", Toast.LENGTH_SHORT).show()
                binding?.etName?.setText("")
                binding?.etSurname?.setText("")
                binding?.etAge?.setText("")
                binding?.etEmail?.setText("")
            }


        }else{
            Toast.makeText(applicationContext, "Please fill all the fields", Toast.LENGTH_SHORT).show()
        }


    }

}
