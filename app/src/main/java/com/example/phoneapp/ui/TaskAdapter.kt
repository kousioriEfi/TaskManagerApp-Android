package com.example.phoneapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.phoneapp.R
import com.example.phoneapp.data.TaskEntity

class TaskAdapter : RecyclerView.Adapter<TaskAdapter.VH>() {

    private var items: List<TaskEntity> = emptyList()

    fun submitList(newItems: List<TaskEntity>) {
        items = newItems
        notifyDataSetChanged()
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvSubtitle: TextView = itemView.findViewById(R.id.tvSubtitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val t = items[position]
        holder.tvTitle.text = t.shortName
        holder.tvSubtitle.text = "statusId=${t.statusId} | duration=${t.durationHours}h | diff=${t.difficulty}"
    }

    override fun getItemCount(): Int = items.size
}
