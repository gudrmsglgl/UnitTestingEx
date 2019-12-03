package com.fastival.unittestex.models

import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Test

class NoteTest {
    /*
    *  1. Compare two equal Notes
    *  2. Compare notes with 2 different ids
    *  3. Compare two notes with different timestamps
    *  4. Compare two notes with different content
    * */
    companion object{
        const val TIMESTAMP_1 = "05-2019"
        const val TIMESTAMP_2 = "04-2019"
    }
    @Test
    internal fun isNotesEqual_identicalProperties_returnTrue (){

        // Arrange
        val note: Note = Note(title = "Note#1",content = "this is note #1",timestamp =  TIMESTAMP_1)
        note.id = 1

        val note2: Note = Note(null,"Note#1", "this is note #1",TIMESTAMP_1)
        note2.id = 1

        // Assert
        assertEquals(note, note2)
        println("The notes are equal!")
    }

    @Test
    internal fun isNotesEqual_differentIds_returnFalse() {
        // Arrange
        val note: Note = Note(title = "Note#1",content = "this is note #1",timestamp =  TIMESTAMP_1)
        note.id = 1

        val note2: Note = Note(null,"Note#1", "this is note #1",TIMESTAMP_1)
        note2.id = 2

        // Assert
        assertNotEquals(note, note2)
        println("The notes are not equal!")
    }

    @Test
    internal fun isNotesEqual_differentTimestamps_Equal() {
        // Arrange
        val note: Note = Note(title = "Note#1",content = "this is note #1",timestamp =  TIMESTAMP_1)
        note.id = 1

        val note2: Note = Note(null,"Note#1", "this is note #1",TIMESTAMP_2)
        note2.id = 1

        // Assert
        assertEquals(note, note2)
        println("The notes are equal!")
    }

    @Test
    internal fun isNotesEqual_differentContent_returnFalse() {
        // Arrange
        val note: Note = Note(title = "Note#1",content = "this is note #1",timestamp =  TIMESTAMP_1)
        note.id = 1

        val note2: Note = Note(null,"Note#1", "this is note #2",TIMESTAMP_2)
        note2.id = 1

        // Assert
        assertNotEquals(note, note2);
        println("The notes are not equal! They have different content.");
    }

}
