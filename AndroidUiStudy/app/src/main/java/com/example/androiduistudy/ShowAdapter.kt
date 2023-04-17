package com.example.androiduistudy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.androiduistudy.bean.NavItem

class ShowAdapter(private val navItems:List<NavItem>,private val findNavController: NavController): RecyclerView.Adapter<ShowAdapter.ViewHolder>() {
    class ViewHolder(itemView: View)  :RecyclerView.ViewHolder(itemView){
        val btnShow = itemView.findViewById<Button>(R.id.btn_show)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_show, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.btnShow.text = navItems[position].name
        holder.btnShow.setOnClickListener {
            findNavController.navigate(navItems[position].action)
        }
    }

    override fun getItemCount(): Int = navItems.size
}