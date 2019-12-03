package com.fastival.unittestex

import com.fastival.unittestex.models.Note

class TestUtil {

    companion object{
        const val TIMESTAMP_1 = "05-2019"
        val TEST_NOTE_1: Note = Note(null, "Take out the trash", "It's garbage day tomorrow.", TIMESTAMP_1)

        const val TIMESTAMP_2 = "05-2019"
        val TEST_NOTE_2: Note = Note(null, "Anniversary gift", "Buy an anniversary gift", TIMESTAMP_2)

        val TEST_NOTES_LIST = listOf(
            TEST_NOTE_1, TEST_NOTE_2
        )
    }

}