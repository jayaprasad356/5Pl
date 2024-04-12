package com.app.fivepl.gateway

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.app.fivepl.R
import com.app.fivepl.activity.HomeActivity
import com.app.fivepl.databinding.ActivityPaymentStatusBinding
import com.app.fivepl.helper.ApiConfig
import com.app.fivepl.helper.Constant
import com.app.fivepl.helper.Session
import org.json.JSONException
import org.json.JSONObject

class PaymentStatusActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentStatusBinding
    private lateinit var session: Session
    private lateinit var activity: Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)
        activity = this
        session = Session(this)




        //Timer 10 sec
        val timer = object : CountDownTimer(20000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // do something after 1s

                // set time in binding.tvStatus
                binding.tvStatus.text = "Please wait... " + millisUntilFinished / 1000 + " sec"
            }

            override fun onFinish() {
                // do something end times 10s
                apicall()
            }
        }
        timer.start()


    }



    private fun apicall() {
        val params: MutableMap<String, String> = HashMap()
        params[Constant.USER_ID] = session!!.getData(Constant.USER_ID)
        params[Constant.TXN_ID] = session!!.getData(Constant.TXN_ID)
        params[Constant.DATE] = session!!.getData(Constant.DATE)
        params[Constant.KEY] = session!!.getData(Constant.KEY)
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val intent = Intent(activity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                        Toast.makeText(activity, ""+jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show()
                    } else {
                        val intent = Intent(activity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                        Toast.makeText(activity, ""+jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show()

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()

                }
            }
        }, this, Constant.RECHARGE_STATUS, params, true)

        // Return a dummy intent, as the actual navigation is handled inside the callback

    }




    private fun showCustomDialog(status: String) {
        val builder = AlertDialog.Builder(activity)
        val dialogView: View = activity.layoutInflater.inflate(R.layout.payment_dialog, null)

        builder.setView(dialogView)
        val dialog = builder.create()
        val ivSuccess = dialogView.findViewById<ImageView>(R.id.ivSuccess)
        val tvStatus = dialogView.findViewById<TextView>(R.id.tvStatus)

        if (status == "success") {
            ivSuccess.setImageResource(R.drawable.success)
            tvStatus.text = "Payment Successful"
        } else {
            ivSuccess.setImageResource(R.drawable.warning)
            tvStatus.text = "Payment Failed"
        }

        // handler 1 sec to move to the next activity
        val handler = android.os.Handler()
        handler.postDelayed({
            val intent = Intent(activity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }, 1000)



        dialog.show()

    }


    override fun onBackPressed() {
        // Do nothing
    }


}

