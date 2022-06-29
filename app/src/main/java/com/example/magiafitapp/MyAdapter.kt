package com.example.magiafitapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter (private val userList : ArrayList<User>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){ 
        val tvName: TextView = itemView.findViewById(R.id.tvfirstName)
        val tvAddress: TextView = itemView.findViewById(R.id.tvaddress)
        val tvSurname: TextView = itemView.findViewById(R.id.tvsurname)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvAddress.text = userList[position].address
        holder.tvName.text = userList[position].name
        holder.tvSurname.text = userList[position].surname
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}