package com.example.noteapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.databinding.RecyclerRowBinding
import com.example.noteapp.model.Note

class NoteAdapter(val noteList: ArrayList<Note>, val onItemClick: (String) -> Unit) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    class NoteViewHolder(val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = noteList[position]
        holder.binding.notesNoteTitle.text = noteList[position].title
        holder.binding.notesNoteContent.text = noteList[position].content

        holder.itemView.setOnClickListener {
            onItemClick(note.id.toString())
        }

    }

}