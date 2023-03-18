package com.example.roomdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
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
        lifecycleScope.launch {
            employeeDao.fetchAllEmployees().collect() {
                val list = ArrayList(it)
                setUpListOfDataIntoRecyclerView(list, employeeDao)
            }
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


        } else {
            Toast.makeText(applicationContext, "Please fill all the fields", Toast.LENGTH_SHORT)
                .show()
        }


    }

    private fun setUpListOfDataIntoRecyclerView(
        employeesList: ArrayList<EmployeeEntity>,
        employeeDao: EmployeeDao
    ) {
        if (employeesList.isNotEmpty()) {
            val itemAdapter = ItemAdapter(
                employeesList
            )
            binding?.rvItemsList?.layoutManager = LinearLayoutManager(this)
            binding?.rvItemsList?.adapter = itemAdapter
            binding?.rvItemsList?.visibility = View.VISIBLE
            binding?.tvNoRecordsAvailable?.visibility = View.GONE

        } else {
            binding?.rvItemsList?.visibility = View.GONE
            binding?.tvNoRecordsAvailable?.visibility = View.VISIBLE

        }
    }
}