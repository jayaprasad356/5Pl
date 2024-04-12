package com.app.fivepl.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.app.fivepl.model.Withdrawal
import com.app.fivepl.R

class WithdrawalAdapter(
    val activity: Activity,
    withdrawal : ArrayList<Withdrawal>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val withdrawal: ArrayList<Withdrawal>
    val activitys: Activity

    init {
        this.withdrawal = withdrawal
        this.activitys = activity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(activity).inflate(R.layout.layout_withdrwal, parent, false)
        return ItemHolder(view)
    }

    override fun onBindViewHolder(holderParent: RecyclerView.ViewHolder, position: Int) {
        val holder: ItemHolder = holderParent as ItemHolder
        val report: Withdrawal = withdrawal[position]



        holder.tvDate.text = report.datetime
        holder.tvAmount.text = "₹" + report.amount

        val status = report.status

        if (status == "0") {
            holder.tvStatus.setTextColor(activitys.resources.getColor(R.color.blue_color))
            holder.tvStatus.text = "Bank Processing"
        }
        else if (status == "1") {
            holder.tvStatus.setTextColor(activitys.resources.getColor(R.color.green))
            holder.tvStatus.text = "Paid"
        }
        else{
            holder.tvStatus.setTextColor(activitys.resources.getColor(R.color.red))
            holder.tvStatus.text = "Cancelled"

        }






    }


    override fun getItemCount(): Int {
        return withdrawal.size
    }

    internal class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView
        val  tvAmount: TextView
        val tvStatus: TextView




        init {
            tvDate = itemView.findViewById(R.id.tvDate)
            tvAmount = itemView.findViewById(R.id.tvAmount)
            tvStatus = itemView.findViewById(R.id.tvStatus)


        }
    }
}