package com.example.listatarefas.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.listatarefas.database.dao.TarefaDao
import com.example.listatarefas.model.Tarefa

@Database(entities = [Tarefa::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tarefaDao() : TarefaDao
}