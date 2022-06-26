package com.example.magiafitapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter (private val userList:ArrayList<User>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>(){

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val tvName: TextView = itemView.findViewById(R.id.name)
        val tvAddress: TextView = itemView.findViewById(R.id.address)
        val tvEmail: TextView = itemView.findViewById(R.id.email)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       val itemView=
           LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.tvName.text = userList[position].name
        holder.tvEmail.text = userList[position].email
        holder.tvAddress.text = userList[position].address
    }

    override fun getItemCount(): Int {
        return userList.size
    }

}