package com.example.demonotesapp

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.demonotesapp.databinding.ViewBinding

class ModelAdapter(private var data: List<NoteInfo>) :
    RecyclerView.Adapter<ModelAdapter.ViewHolder>() {

    class ViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvTitle = binding.tvTitle
        val tvDescription = binding.tvDescription
        val spinnerPriority = binding.spinnerPriority
        val llMain = binding.llMain
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvTitle.text = data[position].title
        holder.tvDescription.text = data[position].description
        holder.spinnerPriority.text = data[position].spinner

        when (data[position].spinner.lowercase()) {
            "high" -> holder.llMain.setBackgroundColor(Color.parseColor("#F05454"))
            "medium" -> holder.llMain.setBackgroundColor(Color.parseColor("#EDC988"))
            else -> holder.llMain.setBackgroundColor(Color.parseColor("#00917C"))
        }

        holder.itemView.setOnClickListener{
            val intent= Intent(holder.itemView.context, UpdateNoteActivity::class.java)
            intent.putExtra("id",position)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}