package com.app.fivepl.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.app.fivepl.model.Transaction
import com.app.fivepl.R

class TransactionAdapter(
    val activity: Activity,
    transaction : ArrayList<Transaction>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val transaction: ArrayList<Transaction>
    val activitys: Activity

    init {
        this.transaction = transaction
        this.activitys = activity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(activity).inflate(R.layout.layout_transaction, parent, false)
        return ItemHolder(view)
    }

    override fun onBindViewHolder(holderParent: RecyclerView.ViewHolder, position: Int) {
        val holder: ItemHolder = holderParent as ItemHolder
        val report: Transaction = transaction[position]




        holder.tvTitle.text = report.type
        holder.tvDate.text = report.datetime
        holder.tvAmount.text ="₹" + report.amount




    }


    override fun getItemCount(): Int {
        return transaction.size
    }

    internal class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView
        val  tvDate: TextView
        val tvAmount: TextView




        init {
            tvTitle = itemView.findViewById(R.id.tvTitle)
            tvDate = itemView.findViewById(R.id.tvDate)
            tvAmount = itemView.findViewById(R.id.tvAmount)


        }
    }
}