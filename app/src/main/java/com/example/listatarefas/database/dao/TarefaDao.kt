package com.example.listatarefas.database.dao

import androidx.room.*
import com.example.listatarefas.model.Tarefa

@Dao
interface TarefaDao {

    @Query("SELECT * FROM tarefa")
    fun getAll(): List<Tarefa>

    @Query("SELECT * FROM tarefa WHERE id IN (:ids)")
    fun getAllByIds(ids: IntArray): List<Tarefa>

    @Query("SELECT * FROM tarefa where id = :id ")
    fun findById(id: Int) : Tarefa

    @Insert
    fun insert(tarefa: Tarefa) : Long

    @Delete
    fun delete(tarefa: Tarefa?)

    @Update
    fun update(tarefa: Tarefa)
}