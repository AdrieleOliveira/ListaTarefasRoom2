package com.example.listatarefas.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tarefa")
data class Tarefa(
    @ColumnInfo(name = "titulo") var titulo:String?,
    @ColumnInfo(name = "descricao") var descricao: String?,
    @ColumnInfo(name = "finalizada") var finalizada: Boolean = false,
    var acao: String = "cadastrado"
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
