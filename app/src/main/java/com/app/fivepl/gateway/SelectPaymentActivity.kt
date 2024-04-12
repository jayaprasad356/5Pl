package com.app.fivepl.gateway

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.fivepl.R
import com.app.fivepl.activity.HomeActivity
import com.app.fivepl.databinding.ActivitySelectPaymentBinding
import com.app.fivepl.helper.ApiConfig
import com.app.fivepl.helper.Constant
import com.app.fivepl.helper.Session
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SelectPaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectPaymentBinding
    private lateinit var session: Session
    private lateinit var gpayLink: String
    private lateinit var paytmLink: String
    private lateinit var phonepeLink: String
    private lateinit var paymentUrl: String
    private lateinit var bhim_link: String
    private lateinit var currentDateAndTime: String
    private var amount: String? = null

    private val REQUEST_CODE = 123


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_payment)
        binding = ActivitySelectPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sdf = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        currentDateAndTime = sdf.format(Date())
        session = Session(this)

        amount = session.getData(Constant.AMOUNT) + ".00"
        binding.tvAmount.text = "₹ $amount"

        apicall(currentDateAndTime)






    }

    private fun upi() {


        val BHIM_UPI = "in.org.npci.upiapp"
        val GOOGLE_PAY = "com.google.android.apps.nbu.paisa.user"
        val PHONE_PE = "com.phonepe.app"
        val PAYTM = "net.one97.paytm"





        val upiApps = listOf<String>(PAYTM, GOOGLE_PAY, PHONE_PE, BHIM_UPI)


        val upiButton = findViewById<RelativeLayout>(R.id.upi)
        val paytmButton = findViewById<RelativeLayout>(R.id.paytm)
        val gpayButton = findViewById<RelativeLayout>(R.id.gpay)
        val phonepeButton = findViewById<RelativeLayout>(R.id.phonepe)
        val bhimButton = findViewById<RelativeLayout>(R.id.bhim)

        val upiAppButtons = listOf<RelativeLayout>(paytmButton, gpayButton, phonepeButton, bhimButton)

        /*
            3. Defining a UPI intent with a Paytm merchant UPI spec deeplink
        */

        for(i in upiApps.indices){
            val b = upiAppButtons[i]
            val p = upiApps[i]
            Log.d("UpiAppVisibility", p + " | " + isAppInstalled(p).toString() + " | " + isAppUpiReady(p))
            if(isAppInstalled(p)&&isAppUpiReady(p)) {
                b.visibility = View.VISIBLE
//                b.setOnClickListener{
//                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(bhim_link))
//                    intent.data = Uri.parse(bhim_link)
//                    intent.setPackage(p)
//                    startActivityForResult(intent, REQUEST_CODE)
//                }
            }
            else{
                b.visibility = View.GONE
            }
        }


        val uri = "$bhim_link"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        intent.data = Uri.parse(uri)

        upiButton.setOnClickListener{
//            val chooser = Intent.createChooser(intent, "Pay with...")
//            startActivityForResult(chooser, REQUEST_CODE)
            // open MainActivity with result

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("QR_VALUE", paymentUrl)
            session.setData(Constant.TXN_ID, currentDateAndTime)
            session.setData(Constant.DATE, currentDate())
            session.setData(Constant.KEY, "707029bb-78d4-44b6-9f72-0d7fe80e338b")
            startActivity(intent)
            finish()

        }

        paytmButton.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(paytmLink))
            intent.data = Uri.parse(paytmLink)
            startActivityForResult(intent, REQUEST_CODE)
        }

        gpayButton.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(gpayLink))
            intent.data = Uri.parse(gpayLink)
            startActivityForResult(intent, REQUEST_CODE)
        }

        phonepeButton.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(phonepeLink))
            intent.data = Uri.parse(phonepeLink)
            startActivityForResult(intent, REQUEST_CODE)
        }

        bhimButton.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(bhim_link))
            intent.data = Uri.parse(bhim_link)
            startActivityForResult(intent, REQUEST_CODE)
        }





    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            // Process based on the data in response.
            Log.d("result", data.toString())
            data?.getStringExtra("Status")?.let { Log.d("result", it) }
            data?.getStringExtra("Status")?.let {

                if (it == "success") {
                    val intent = Intent(this, PaymentStatusActivity::class.java)
                    session.setData(Constant.TXN_ID, currentDateAndTime)
                    session.setData(Constant.DATE, currentDate())
                    session.setData(Constant.KEY, "707029bb-78d4-44b6-9f72-0d7fe80e338b")
                    intent.putExtra("status", "success")
                    startActivity(intent)
                    finish()

                } else {
//                    val intent = Intent(this, PaymentStatusActivity::class.java)
//                    session.setData(Constant.TXN_ID, currentDateAndTime)
//                    session.setData(Constant.DATE, currentDate())
//                    session.setData(Constant.KEY, "707029bb-78d4-44b6-9f72-0d7fe80e338b")
//                    intent.putExtra("status", "failed")
//                    startActivity(intent)

                    Toast.makeText(applicationContext, "Payment Failed", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }

//                Toast.makeText(applicationContext, it +"1", Toast.LENGTH_LONG).show()
                        }

        }
    }


    fun isAppInstalled(packageName: String): Boolean {
        val pm = packageManager
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return false
    }


    fun isAppUpiReady(packageName: String): Boolean {
        var appUpiReady = false
        val upiIntent = Intent(Intent.ACTION_VIEW, Uri.parse("upi://pay"))
        val pm = packageManager
        val upiActivities: List<ResolveInfo> = pm.queryIntentActivities(upiIntent, 0)
        for (a in upiActivities){
            if (a.activityInfo.packageName == packageName) appUpiReady = true
        }
        return appUpiReady
    }


    private fun apicall(currentDateAndTime: String) {
        val params = hashMapOf(
            Constant.USER_ID to session.getData(Constant.USER_ID),
            Constant.KEY to "707029bb-78d4-44b6-9f72-0d7fe80e338b",
            Constant.TXN_ID to currentDateAndTime,
            Constant.AMOUNT to amount.toString(),
            Constant.DATE to currentDate()
        )

        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.STATUS)) {
                        val dataObject = jsonObject.getJSONObject(Constant.DATA)
                        val orderId = dataObject.getInt("order_id")
                        paymentUrl = dataObject.getString("payment_url")
                        val upiIntent = dataObject.getJSONObject("upi_intent")

                        bhim_link = upiIntent.getString("bhim_link")
                        gpayLink = upiIntent.getString("gpay_link")
                        phonepeLink = upiIntent.getString("phonepe_link")
                        paytmLink = upiIntent.getString("paytm_link")
                        upi()


                    } else {
                        val message = jsonObject.getString(Constant.MESSAGE)
                        Toast.makeText(this,""+ message, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Network error: $response", Toast.LENGTH_SHORT).show()
            }
        }, this, Constant.RECHARGE_CREATE, params, true)
    }


    // fun currentDate dd-MM-yyyy
    private fun currentDate(): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return sdf.format(Date())
    }




}
