package com.app.fivepl.adapter

import android.app.Activity
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

import com.app.fivepl.R
import com.app.fivepl.helper.Constant
import com.app.fivepl.helper.Session
import com.app.fivepl.model.Selltomarket

class SelltomarketAdapter(
    val activity: Activity,
    myplan: java.util.ArrayList<Selltomarket>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val myplan: ArrayList<Selltomarket>
    val activitys: Activity
    val session: Session = Session(activity)

    private var selectedPosition: Int = -1
    init {
        this.myplan = myplan
        this.activitys = activity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(activity).inflate(R.layout.layout_selltomarket, parent, false)
        return ItemHolder(view)
    }

    override fun onBindViewHolder(holderParent: RecyclerView.ViewHolder, position: Int) {
        val holder: ItemHolder = holderParent as ItemHolder
        val report: Selltomarket = myplan[position]



        holder.tvMarketName.text = report.name


        val price = "<font color='#FF000000'>Current Selling Price:</font> "+"<font color='#00B251'>Rs.${report.price}</font>"

        holder.tvMarketPrice.text = Html.fromHtml(price)

        // Set checked state based on position
        holder.rbMarket.isChecked = position == selectedPosition

        // default selected market set position 0
        if (position == 0 && session.getData(Constant.MARKET_ID) == "market_id") {
            holder.rbMarket.isChecked = true
            selectedPosition = 0
            val select_id = report.id
            session.setData(Constant.MARKET_ID, select_id)
        }

        // Handle click event for radio button
        holder.rbMarket.setOnClickListener {
            // Update selected position
            selectedPosition = holder.adapterPosition
            val select_id = report.id
            session.setData(Constant.MARKET_ID, select_id)
            notifyDataSetChanged()
        }

        holder.cardMarket.setOnClickListener {
            holder.rbMarket.isChecked = position == selectedPosition
            selectedPosition = holder.adapterPosition
            val select_id = report.id
            session.setData(Constant.MARKET_ID, select_id)
            notifyDataSetChanged()
        }



        // selectedPosition to Activity



    }






    override fun getItemCount(): Int {
        return myplan.size
    }

    internal class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardMarket: CardView
        val tvMarketName: TextView
        val tvMarketPrice: TextView
        val rbMarket: RadioButton


        init {
            cardMarket = itemView.findViewById(R.id.cardMarket)
            tvMarketName = itemView.findViewById(R.id.tvMarketName)
            tvMarketPrice = itemView.findViewById(R.id.tvMarketPrice)
            rbMarket = itemView.findViewById(R.id.rbMarket)

        }
    }



}

