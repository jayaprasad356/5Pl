package com.app.fivepl.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import com.app.fivepl.model.MyPlan
import com.app.fivepl.R
import com.app.fivepl.activity.HomeActivity
import com.app.fivepl.activity.SelltomarketActivity
import com.app.fivepl.helper.ApiConfig
import com.app.fivepl.helper.Constant
import com.app.fivepl.helper.Session
import org.json.JSONException
import org.json.JSONObject

class ActivateplansAdapter(
    val activity: Activity,
    myplan: java.util.ArrayList<MyPlan>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val myplan: ArrayList<MyPlan>
    val activitys: Activity

    init {
        this.myplan = myplan
        this.activitys = activity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(activity).inflate(R.layout.layout_activateplan, parent, false)
        return ItemHolder(view)
    }

    override fun onBindViewHolder(holderParent: RecyclerView.ViewHolder, position: Int) {
        val holder: ItemHolder = holderParent as ItemHolder
        val report: MyPlan = myplan[position]
        holder.cardView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.context, R.anim.recycler_anim1))


        val context = holder.itemView.context
        if (report.claim.equals("1")) {
            // Set background color to primary_color
            holder.btnActivate.setBackgroundColor(ContextCompat.getColor(context, R.color.primary_color))
         //   apicall(report.id)
        } else {
            // Set background color to grey_extra_light
            holder.btnActivate.setBackgroundColor(ContextCompat.getColor(context, R.color.grey_extra_light))
        }


        holder.btnActivate.setOnClickListener {

            if (report.claim.equals("1")) {
//                showCustomDialog(report.plan_id,report.daily_quantity,report.unit,report.products,report.daily_income)

                // next activity
                val intent = Intent(activity, SelltomarketActivity::class.java)
                intent.putExtra("plan_id", report.plan_id)
                intent.putExtra("daily_quantity", report.daily_quantity)
                intent.putExtra("unit", report.unit)
                intent.putExtra("products", report.products)
                intent.putExtra("daily_income", report.daily_income)
                activity.startActivity(intent)
                activity.finish()

            } else {

            }
        }



        holder.tvplanName.text = report.products

        holder.tvplan.text = "₹ " + report.income

        holder.tvstartedate.text =report.joined_date
        holder.tvprice.text = "₹ " + report.price

        Glide.with(activity).load(report.image).placeholder(R.drawable.sample_agri).into(holder.ivImage)



    }

    private fun apicall(id: String?) {
        val session = Session(activity)

        val params: MutableMap<String, String> = HashMap()
        params["user_id"] = session.getData(Constant.USER_ID)!!
        params["plan_id"] = id!!
        ApiConfig.RequestToVolley({ result, response ->

            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    val msg = jsonObject.getString(Constant.MESSAGE).toString()
                    if (jsonObject.getBoolean(com.app.fivepl.helper.Constant.SUCCESS)) {
                        val `object` = JSONObject(response)



                     //   showCustomDialog(msg)

                    Toast.makeText(activity, "" + jsonObject.getString(com.app.fivepl.helper.Constant.MESSAGE).toString(), Toast.LENGTH_SHORT).show()

                        moveToHomeActivity()

                    } else {

                   Toast.makeText(activity, "" + jsonObject.getString(com.app.fivepl.helper.Constant.MESSAGE).toString(), Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
                }
            }

        }, activity, Constant.CLAIM, params, true)


    }
    private fun moveToHomeActivity() {
        val intent = Intent(activity, HomeActivity::class.java)
        activity.startActivity(intent)
        activity.finish() // Optional: finish the current activity if you don't want to keep it in the stack
    }



    override fun getItemCount(): Int {
        return myplan.size
    }

    internal class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvplan: TextView
    //    val tvValidity : TextView
        val  ivImage : ImageView
        val  btnActivate : Button
        val cardView : CardView
        var tvplanName : TextView
        var tvstartedate : TextView
        var tvprice : TextView


        init {
            tvplan = itemView.findViewById(R.id.tvplan)
            ivImage = itemView.findViewById(R.id.ivImage)
            btnActivate = itemView.findViewById(R.id.btnActivate)
            cardView = itemView.findViewById(R.id.cardView)
            tvplanName = itemView.findViewById(R.id.tvplanName)
            tvstartedate = itemView.findViewById(R.id.tvstartedate)
            tvprice = itemView.findViewById(R.id.tvprice)

        }
    }


    private fun showCustomDialog(
        id: String?,
        dailyQuantity: String?,
        unit: String?,
        products: String?,
        income: String?
    ) {
        val builder = AlertDialog.Builder(activity)
        val dialogView: View = activity.layoutInflater.inflate(R.layout.active_plan_dialog, null)

        builder.setView(dialogView)
        val dialog = builder.create()
        val tvMessage = dialogView.findViewById<TextView>(R.id.tvMessage)
        val  btnOk = dialogView.findViewById<Button>(R.id.btnOk)


        val formattedIncome = "<font color='#00B251'>Rs.</font> "+" <font color='#00B251'>$income</font>"
        val message = "$dailyQuantity $unit $products = $formattedIncome"
        tvMessage.text = Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT)


        btnOk.setOnClickListener {
            apicall(id)
            dialog.dismiss()
        }


        dialog.show()

    }

}