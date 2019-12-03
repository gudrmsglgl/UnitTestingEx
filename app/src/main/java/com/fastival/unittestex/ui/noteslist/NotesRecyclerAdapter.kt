package com.fastival.unittestex.ui.noteslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fastival.unittestex.R
import com.fastival.unittestex.models.Note
import com.fastival.unittestex.util.DateUtil
import kotlinx.android.synthetic.main.layout_note_list_item.view.*

class NotesRecyclerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private var onNoteListener: OnNoteListener

    private var notes = ArrayList<Note>()

    constructor(onNoteListener: OnNoteListener) {
        this.onNoteListener = onNoteListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_note_list_item, parent, false)
        return ViewHolder(view, onNoteListener)
    }

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var month = notes[position].timestamp.substring(0,2)
        month = DateUtil.getMonthFromNumber(month)
        val year = notes[position].timestamp.substring(3)
        val timestamp = "$month $year"

        holder.itemView.run {
            note_timestamp.text = timestamp
            note_title.text = notes[position].title
        }

    }

    fun getNote(position: Int) = if (notes.size > 0) notes[position] else null

    fun removeNote(note: Note) {
        notes.remove(note)
        notifyDataSetChanged()
    }

    fun setNote(notes: List<Note>){
        this.notes = notes as ArrayList<Note>
        notifyDataSetChanged()
    }

    inner class ViewHolder : RecyclerView.ViewHolder, View.OnClickListener {

        private val mOnNoteListener: OnNoteListener

        constructor(itemView: View, onNoteListener: OnNoteListener): super(itemView){
            this.mOnNoteListener = onNoteListener

            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            getNote(adapterPosition)?.let { mOnNoteListener.onNoteClick(it) }
        }
    }

    interface OnNoteListener{
        fun onNoteClick(note: Note)
    }
}