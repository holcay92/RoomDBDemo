package com.example.roomdemo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdemo.databinding.ItemsRowBinding

class ItemAdapter(
    private val items: ArrayList<EmployeeEntity>,
   // private val updateListener: (id: Int) -> Unit,
   // private val deleteListener: (id: Int) -> Unit
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemsRowBinding) : RecyclerView.ViewHolder(binding.root) {
        val llMain = binding.llMain
        val tvName = binding.tvName
        val tvSurname = binding.tvSurname
        val tvAge = binding.tvAge
        val tvEmail = binding.tvEmail
        val ivEdit = binding.ivEdit
        val ivDelete = binding.ivDelete

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemsRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val item = items[position]
        holder.tvName.text = item.name
        holder.tvSurname.text = item.surname
        holder.tvAge.text = item.age.toString()
        holder.tvEmail.text = item.email
        // background color change for even and odd rows
        if (item.id % 2 == 0) {
            holder.llMain.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorLightGrey
                )
            )
        }else{
            holder.llMain.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.white
                )
            )
        }

        holder.ivEdit.setOnClickListener {
           // updateListener(item.id)
        }
        holder.ivDelete.setOnClickListener {
          //  deleteListener(item.id)
        }



    }
}