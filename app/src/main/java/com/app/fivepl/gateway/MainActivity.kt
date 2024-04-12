package com.app.fivepl.gateway

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.fivepl.R
import com.app.fivepl.activity.HomeActivity
import com.app.fivepl.helper.ApiConfig
import com.app.fivepl.helper.Constant
import com.app.fivepl.helper.Session
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    var mWebView: WebView? = null
    var context: Context? = null
    var TAG = "MainActivity"


    private lateinit var session: Session
    private lateinit var gpayLink: String
    private lateinit var paytmLink: String
    private lateinit var phonepeLink: String
    private lateinit var paymentUrl: String
    private lateinit var bhim_link: String
    private lateinit var currentDateAndTime: String
    private var amount: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        session = Session(this)


        amount = session.getData(Constant.AMOUNT) + ".00"

        val sdf = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        currentDateAndTime = sdf.format(Date())
        apicall1(currentDateAndTime)

        if (0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) {
            WebView.setWebContentsDebuggingEnabled(true)
        }
        context = this
        mWebView = findViewById<View>(R.id.payment_webview) as WebView
        initWebView()






    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        mWebView!!.settings.javaScriptEnabled = true
        mWebView!!.settings.loadWithOverviewMode = true
        mWebView!!.settings.setSupportMultipleWindows(true)
        // Do not change Useragent otherwise it will not work. even if not working uncommit below
        // mWebView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 4.4.4; One Build/KTU84L.H4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.135 Mobile Safari/537.36");
        mWebView!!.webChromeClient = WebChromeClient()
        mWebView!!.addJavascriptInterface(WebviewInterface(), "Interface")
    }

    inner class WebviewInterface {
        @JavascriptInterface
        fun paymentResponse(client_txn_id: String, txn_id: String) {
            Log.i(TAG, client_txn_id)
            Log.i(TAG, txn_id)
            // this function is called when payment is done (success, scanning ,timeout or cancel by user).
            // You must call the check order status API in server and get update about payment.
            // 🚫 Do not Call UpiGateway API in Android App Directly.
            Toast.makeText(context, "Order ID: $client_txn_id, Txn ID: $txn_id", Toast.LENGTH_SHORT).show()

            apicall2()

            // Close the Webview.
        }

        @JavascriptInterface
        fun errorResponse() {
            // this function is called when Transaction in Already Done or Any other Issue.
            Toast.makeText(context, "Transaction Error.", Toast.LENGTH_SHORT).show()
            // Close the Webview.

        }
    }



    private fun apicall1(currentDateAndTime: String) {
        val params = hashMapOf(
            Constant.USER_ID to session!!.getData(Constant.USER_ID),
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


                        val qrValue = paymentUrl.toString()
                        val PAYMENT_URL = ""+qrValue

                        if (PAYMENT_URL.startsWith("upi:")) {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse(PAYMENT_URL)
                            startActivity(intent)
                        } else {
                            mWebView!!.loadUrl(PAYMENT_URL)
                        }



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



    private fun apicall2() {
        val params: MutableMap<String, String> = HashMap()
        params[Constant.USER_ID] = session!!.getData(Constant.USER_ID)
        params[Constant.TXN_ID] = currentDateAndTime
        params[Constant.DATE] = currentDate()
        params[Constant.KEY] = "707029bb-78d4-44b6-9f72-0d7fe80e338b"
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                        Toast.makeText(this, ""+jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show()
                    } else {
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                        Toast.makeText(this, ""+jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show()

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()

                }
            }
        }, this, Constant.RECHARGE_STATUS, params, true)

        // Return a dummy intent, as the actual navigation is handled inside the callback

    }

    override fun onBackPressed() {
        // Do nothing
    }


    private fun currentDate(): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return sdf.format(Date())
    }


    // Assume you have integrated a QR code scanning library and received the scanned data in 'scannedData' variable



}