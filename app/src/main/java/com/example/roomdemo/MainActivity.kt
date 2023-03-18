package com.example.roomdemo

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roomdemo.databinding.ActivityMainBinding
import com.example.roomdemo.databinding.DialogUpdateBinding
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
            employeeDao.fetchAllEmployee().collect() {
                val list = ArrayList(it)
                setUpListOfDataIntoRecyclerView(list, employeeDao)
            }
        }
    }

    private fun setUpListOfDataIntoRecyclerView(
        employeesList: ArrayList<EmployeeEntity>,
        employeeDao: EmployeeDao
    ) {
        if (employeesList.isNotEmpty()) {
            val itemAdapter = ItemAdapter(employeesList, { updateId ->
                updateRecordDialog(updateId, employeeDao)
            }) { deleteId ->
                lifecycleScope.launch {
                    employeeDao.fetchEmployeeById(deleteId).collect {
                        if (it != null) {
                            deleteRecordAlertDialog(deleteId, employeeDao, it)
                        }
                    }
                }

            }
            binding?.rvItemsList?.layoutManager = LinearLayoutManager(this)
            binding?.rvItemsList?.adapter = itemAdapter
            binding?.rvItemsList?.visibility = View.VISIBLE
            binding?.tvNoRecordsAvailable?.visibility = View.GONE

        } else {
            binding?.rvItemsList?.visibility = View.GONE
            binding?.tvNoRecordsAvailable?.visibility = View.VISIBLE
        }
    }

    private fun addRecord(employeeDao: EmployeeDao) {
        val name = binding?.etName?.text.toString()
        val surname = binding?.etSurname?.text.toString()
        val age = binding?.etAge?.text.toString().toInt()
        val email = binding?.etEmail?.text.toString()

        if (name.isNotEmpty() && surname.isNotEmpty() && age != 0 && email.isNotEmpty()) {
            lifecycleScope.launch {
                employeeDao.insert(
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

    private fun updateRecordDialog(id: Int, employeeDao: EmployeeDao) {
        val updateDialog = Dialog(this, R.style.Theme_Dialog)
        updateDialog.setCancelable(false)
        /*Set the screen content from a layout resource.
         The resource will be inflated, adding all top-level views to the screen.*/
        val binding = DialogUpdateBinding.inflate(layoutInflater)
        updateDialog.setContentView(binding.root)

        lifecycleScope.launch {
            employeeDao.fetchEmployeeById(id).collect {
                if (it != null) {
                    binding.etUpdateName.setText(it.name)
                    binding.etUpdateEmailId.setText(it.email)
                    binding.etUpdateSurname.setText(it.surname)
                    binding.etUpdateAge.setText(it.age.toString())
                }
            }
        }
        binding.tvUpdate.setOnClickListener {

            val name = binding.etUpdateName.text.toString()
            val email = binding.etUpdateEmailId.text.toString()
            val surname = binding.etUpdateSurname.text.toString()
            val age = binding.etUpdateAge.text.toString().toInt()

            if (name.isNotEmpty() && email.isNotEmpty() && surname.isNotEmpty() && age != 0) {
                lifecycleScope.launch {
                    employeeDao.update(EmployeeEntity(id, name, surname, age, email))
                    Toast.makeText(applicationContext, "Record Updated.", Toast.LENGTH_LONG)
                        .show()
                    updateDialog.dismiss() // Dialog will be dismissed
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Name or Email cannot be blank",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        binding.tvCancel.setOnClickListener {
            updateDialog.dismiss()
        }
        //Start the dialog and display it on screen.
        updateDialog.show()
    }

    private fun deleteRecordAlertDialog(
        id: Int,
        employeeDao: EmployeeDao,
        employee: EmployeeEntity
    ) {
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("Delete Record")
        //set message for alert dialog
        builder.setMessage("Are you sure you wants to delete ${employee.name}.")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes") { dialogInterface, _ ->
            lifecycleScope.launch {
                employeeDao.delete(employee)
                Toast.makeText(
                    applicationContext,
                    "Record deleted successfully.",
                    Toast.LENGTH_LONG
                ).show()
                dialogInterface.dismiss() // Dialog will be dismissed
            }
        }

        //performing negative action
        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss() // Dialog will be dismissed
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false) // Will not allow user to cancel after clicking on remaining screen area.
        alertDialog.show()  // show the dialog to UI
    }

}
