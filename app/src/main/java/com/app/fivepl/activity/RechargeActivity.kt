package com.app.fivepl.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.app.fivepl.databinding.ActivityRechargeBinding
import com.app.fivepl.gateway.MainActivity
import com.app.fivepl.helper.Constant
import com.phonepe.intent.sdk.api.models.PhonePeEnvironment

class RechargeActivity : AppCompatActivity() {

    lateinit var binding: ActivityRechargeBinding
    private lateinit var activity: Activity
    private lateinit var environment: PhonePeEnvironment
    private lateinit var session: com.app.fivepl.helper.Session
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRechargeBinding.inflate(layoutInflater)
        activity = this
        session = com.app.fivepl.helper.Session(activity)
        //PhonePe.init(activity, environment, "", "")




        binding.ibBack.setOnClickListener {
            onBackPressed()
        }

        // etAmount  on typing change it decimal format

        binding.btnRecharge.setOnClickListener {

            // etAmount not 0 or empty
            if (binding.etAmount.text.toString().isEmpty() || binding.etAmount.text.toString() == "0" ) {
                Toast.makeText(activity, "Please enter amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else{
                val Intent = Intent(this,MainActivity ::class.java)
                startActivity(Intent)
                session.setData(Constant.AMOUNT,binding.etAmount.text.toString())
            }


        }



        setContentView(binding.root)

    }



//    private fun phonepeCall() {
//        PhonePe.init(this@RechargeActivity, PhonePeEnvironment.RELEASE, "M22TPYBERG1U0", "")
//        val data = JSONObject()
//        data.put("merchantTransactionId",System.currentTimeMillis().toString())
//        data.put("merchantId", "M22TPYBERG1U0")
//        data.put("merchantUserId", System.currentTimeMillis().toString())
//        data.put("amount", 200)
//        data.put("mobileNumber", "1234567890")
//        data.put("callbackUrl", "https://api.phonepe.com/apis/hermes")
//        val mPaymentInstrument = JSONObject()
//        mPaymentInstrument.put("type", "PAY_PAGE")
//        data.put("paymentInstrument", mPaymentInstrument)
//        val base64Body: String = Base64.encodeToString(data.toString().toByteArray(Charset.defaultCharset()), Base64.NO_WRAP)
//        val checksum =sha256(base64Body + "/pg/v1/pay" + "2aee1031-a822-4b78-9b97-86bc5906b3bc") + "###" + 1
//        val b2BPGRequest =
//            B2BPGRequestBuilder().setData(base64Body).setChecksum(checksum).setUrl("/pg/v1/pay").build()
//        binding.btnLogin.setOnClickListener {
//            try {
//                startActivityForResult(PhonePe.getImplicitIntent(this, b2BPGRequest, "")!!, 1)
//            } catch (_: PhonePeInitException) {
//            }
//
//
//
//        }
//
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == 1) {
//            Toast.makeText(this@RechargeActivity, "Success", Toast.LENGTH_SHORT).show()
//        }
//    }
//    fun sha256(data: String): String {
//        try {
//            val digest = MessageDigest.getInstance("SHA-256")
//            val bytes = digest.digest(data.toByteArray(Charset.defaultCharset()))
//            val hexString = StringBuilder(bytes.size * 2)
//            for (byte in bytes) {
//                val int = byte.toInt() and 0xff
//                val hexChar = Integer.toHexString(int)
//                if (hexChar.length == 1) {
//                    hexString.append('0')
//                }
//                hexString.append(hexChar)
//            }
//            return hexString.toString()
//        } catch (e: Exception) {
//            throw RuntimeException("Error computing SHA-256 hash: $e")
//        }
//    }

}