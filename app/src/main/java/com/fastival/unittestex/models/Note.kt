package com.fastival.unittestex.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "content") var content: String,
    @ColumnInfo(name = "timestamp") var timestamp: String
) : Parcelable
{

    @Ignore
    constructor(note: Note) : this(note.id, note.title, note.content, note.timestamp)



    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }

        val note: Note = other as Note
        return note.id == this.id && note.title == this.title && note.content == this.content
    }
}