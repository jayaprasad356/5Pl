package com.app.fivepl.gateway

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.app.fivepl.helper.ApiConfig
import com.app.fivepl.helper.Constant
import com.app.fivepl.helper.Session
import com.app.fivepl.databinding.ActivityApiCallBinding
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ApiCallActivity : AppCompatActivity() {

    private lateinit var binding: ActivityApiCallBinding
    private lateinit var session: Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApiCallBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sdf = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        val currentDateAndTime = sdf.format(Date())
        session = Session(this)
        apicall(currentDateAndTime)
    }

    private fun apicall(currentDateAndTime: String) {
        val params: MutableMap<String, String> = HashMap()
        params["key"] = "707029bb-78d4-44b6-9f72-0d7fe80e338b"
        params["client_txn_id"] = currentDateAndTime
        params["amount"] = "1.00"
        params["p_info"] = "Product Name"
        params["customer_name"] = "Jon Doe"
        params["customer_email"] = "jondoe@gmail.com"
        params["customer_mobile"] = "9876543210"
        params["redirect_url"] = "https://chat.openai.com/"
        params["udf1"] = "user defined field 1 (max 25 char)"
        params["udf2"] = "user defined field 2 (max 25 char)"
        params["udf3"] = "user defined field 3 (max 25 char)"

        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.STATUS)) {
                        val dataObject = jsonObject.getJSONObject(Constant.DATA)
                        val orderId = dataObject.getInt("order_id")
                        val paymentUrl = dataObject.getString("payment_url")
                        val upiIntent = dataObject.getJSONObject("upi_intent")


                        val gpayLink = upiIntent.getString("gpay_link")
                        val phonepeLink = upiIntent.getString("phonepe_link")



                        Toast.makeText(this, "GPay Link: $gpayLink\nPhonePe Link: $phonepeLink", Toast.LENGTH_SHORT).show()
                    } else {
                        val message = jsonObject.getString(Constant.MESSAGE)
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Network error: $response", Toast.LENGTH_SHORT).show()
            }
        }, this, Constant.GATEWAY, params, true)
    }
}
