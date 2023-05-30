package com.hiworld.snotes.home.adapter

import android.app.Activity
import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hiworld.snotes.R
import com.hiworld.snotes.model.Notes

class NotesAdapter() : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    private var list = mutableListOf<Notes>()
    private var actionEdit: ((Notes) -> Unit)? = null
    private var actionDelete: ((Notes) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_model, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val jdl_notes: TextView = itemView.findViewById(R.id.jdl_notes)
        val tgl_notes: TextView = itemView.findViewById(R.id.tgl_notes)
        val actionEdit: ImageView = itemView.findViewById(R.id.editNotes)
        val actionDelete: ImageView = itemView.findViewById(R.id.deleteNotes)
    }

    override fun getItemCount() = list.size

    fun setData(data: List<Notes>) {
        list.apply {
            clear()
            addAll(data)
        }
        notifyDataSetChanged()
    }

    fun setOnActionEditListener(callback: (Notes) -> Unit){
        this.actionEdit = callback
    }

    fun setOnActionDeleteListener(callback: (Notes) -> Unit){
        this.actionDelete = callback
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notes = list[position]
        holder.jdl_notes.text = notes.judul
        holder.tgl_notes.text = notes.tanggal
        holder.actionEdit.setOnClickListener { actionEdit?.invoke(notes) }
        holder.actionDelete.setOnClickListener { actionDelete?.invoke(notes) }
    }
}
