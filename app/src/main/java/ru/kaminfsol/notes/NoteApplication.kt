package ru.kaminfsol.notes

import android.app.Application
import androidx.room.Room

class NoteApplication : Application() {
    val database by lazy {
        Room.databaseBuilder(this, NoteDatabase::class.java, "notes_database")
            .build()
    }
}