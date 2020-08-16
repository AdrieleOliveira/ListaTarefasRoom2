package com.example.listatarefas.adapters

import android.content.Context
import android.graphics.Paint
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.listatarefas.R
import com.example.listatarefas.database.AppDatabase
import com.example.listatarefas.database.dao.TarefaDao
import com.example.listatarefas.model.Tarefa
import kotlinx.android.synthetic.main.item_tarefa.view.*
import kotlinx.android.synthetic.main.item_tarefa_editar.view.*
import kotlinx.android.synthetic.main.item_tarefa_novo.view.btnSalvar
import kotlinx.android.synthetic.main.item_tarefa_novo.view.textDescricao
import kotlinx.android.synthetic.main.item_tarefa_novo.view.textTitulo


class TarefaAdapter(val listener: TarefaAdapterListener, context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val LAYOUT_CADASTRADO: Int = 0
    val LAYOUT_NOVO: Int = 1
    val LAYOUT_EDITAR: Int = 2

        private val dao: TarefaDao
        private var tarefas: MutableList<Tarefa>

        init {
            val db = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "tarefa-db"
            )
                .allowMainThreadQueries()
                .build()

            dao = db.tarefaDao()
            tarefas = dao.getAll().toMutableList()
        }

        fun save(tarefa: Tarefa): Int {
            return if (tarefa.id == 0L){
                tarefa.acao = "cadastrado"
                tarefa.id = dao.insert(tarefa)

                val position = 0
                notifyItemChanged(position)
                return position
            } else {
                tarefa.acao = "cadastrado"
                dao.update(tarefa);

                val position = tarefas.indexOf(tarefa)
                notifyItemChanged(position)
                position
            }
        }

        fun novo() : Int {
            val tarefa = Tarefa("", "", false, "novo")

            val position = 0
            tarefas.add(position, tarefa)
            notifyItemInserted(position)
            return position
        }

        fun edit(tarefa: Tarefa){
            val position = tarefas.indexOf(tarefa)
            notifyItemChanged(position)
        }

        fun remove(tarefa: Tarefa?){
            dao.delete(tarefa)

            val position = tarefas.indexOf(tarefa)
            tarefas.removeAt(position)
            notifyItemRemoved(position)
        }

    override fun getItemCount() = tarefas.size;

    override fun getItemViewType(position: Int): Int {
        val tarefa = tarefas[position]

        return when (tarefa.acao) {
            "cadastrado" -> this.LAYOUT_CADASTRADO
            "novo" -> this.LAYOUT_NOVO
            "editar" -> this.LAYOUT_EDITAR
            else -> -1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : RecyclerView.ViewHolder{

        var view: View?
        var viewHolder: RecyclerView.ViewHolder?

        when (viewType) {
            this.LAYOUT_CADASTRADO -> {
                view = LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.item_tarefa, parent, false)
                viewHolder = ViewHolderCadastrado(view)
            }
            this.LAYOUT_NOVO -> {
                view = LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.item_tarefa_novo, parent, false)
                viewHolder = ViewHolderNovo(view)
            }
            else -> {
                view = LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.item_tarefa_editar, parent, false)
                viewHolder = ViewHolderEditar(view)
            }
        }

        return viewHolder!!
    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val tarefa = tarefas[position]

        when (holder.itemViewType) {
            0 -> {
                var viewHolderCadastrado: ViewHolderCadastrado = holder as ViewHolderCadastrado
                viewHolderCadastrado.fillView(tarefa, position)
            }
            1 -> {
                var viewHolderNovo: ViewHolderNovo = holder as ViewHolderNovo
                viewHolderNovo.fillView(tarefa)
            }
            else -> {
                var viewHolderEditar: ViewHolderEditar = holder as ViewHolderEditar
                viewHolderEditar.fillView(tarefa)
            }
        }
    }

    inner class ViewHolderCadastrado(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun fillView(tarefa: Tarefa, position: Int){
            itemView.lbTitulo.text = tarefa.titulo

            if(tarefa.finalizada){
                itemView.lbTitulo.setTextColor(	"#BEBEBE".toColorInt())
                itemView.lbTitulo.setPaintFlags(itemView.lbTitulo.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
            } else {
                itemView.btnShare.visibility = View.INVISIBLE
            }

            itemView.btnShare.setOnClickListener {
                listener.share(tarefa)
            }

            itemView.setOnClickListener {
                tarefa.acao = "editar"
                listener.editState(tarefa)
            }

            itemView.setOnLongClickListener {
                tarefa.finalizada = !tarefa.finalizada
                listener.save(tarefa)
                true
            }
        }
    }

    inner class ViewHolderNovo(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun fillView(tarefa: Tarefa){
            itemView.btnSalvar.setOnClickListener {
                tarefa.titulo = itemView.textTitulo.text.toString()
                tarefa.descricao = itemView.textDescricao.text.toString()

                listener.save(tarefa)

                itemView.textTitulo.text = null
                itemView.textDescricao.text = null
            }

        }
    }

    inner class ViewHolderEditar(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun fillView(tarefa:Tarefa){
            itemView.textTitulo.setText(tarefa.titulo)
            itemView.textDescricao.setText(tarefa.descricao)

            itemView.btnSalvar.setOnClickListener{
                tarefa.titulo = itemView.textTitulo.text.toString()
                tarefa.descricao = itemView.textDescricao.text.toString()

                listener.save(tarefa)

                itemView.textTitulo.text = null
                itemView.textDescricao.text = null
            }

            itemView.btnRemover.setOnClickListener {
                listener.remove(tarefa)
            }
        }
    }

}