package ru.kaminfsol.notes.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.kaminfsol.notes.database.Note
import ru.kaminfsol.notes.database.NoteDao

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}