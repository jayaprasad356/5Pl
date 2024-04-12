package com.app.fivepl.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.fivepl.databinding.ActivityForgototpBinding
import com.app.fivepl.helper.ApiConfig
import com.app.fivepl.helper.Constant
import com.app.fivepl.helper.Session
import com.app.fivepl.utils.DialogUtils
import org.json.JSONException
import org.json.JSONObject

class ForgototpActivity : AppCompatActivity() {

    lateinit var binding: ActivityForgototpBinding
    lateinit var activity: Activity
    lateinit var session: Session


    var otp_get: String? = null

    var verify: Boolean? = false

    private var countDownTimer: CountDownTimer? = null
    private val COUNTDOWN_TIME = 45000L // 45 seconds in milliseconds
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgototpBinding.inflate(layoutInflater)


        binding.ibBack.setOnClickListener() {
            onBackPressed()
        }

        activity = this
        session = Session(activity)


        binding.tvResend.setOnClickListener {
            // Reset the timer
            resetCountdown()
            // Start the countdown timer again
            startCountdown()
            otp()
            // Disable the button
        }

        binding.btnsent.setOnClickListener{
            if (binding.etPhoneNumber.text.toString().isEmpty()) {
                binding.etPhoneNumber.error = "Please enter mobile number"
                binding.etPhoneNumber.requestFocus()
            } else if (binding.etPhoneNumber.text.toString().length != 10) {
                binding.etPhoneNumber.error = "Please enter valid mobile number"
                binding.etPhoneNumber.requestFocus()
            }
            else{
                binding.btnsent.visibility = View.GONE

                otp()
                binding.etPhoneNumber.isEnabled = false



            }

        }


        binding.btnverify.setOnClickListener{

            if (binding.etOTP.text.toString() == otp_get){
                binding.btnverify.visibility  = View.GONE
                binding.rlcheck.visibility  = View.VISIBLE
                binding.etOTP.isEnabled = false
                verify = true
                binding.tvResend.visibility = View.GONE
                binding.password.visibility = View.VISIBLE

            }

            else {
                Toast.makeText(this,"Invalid", Toast.LENGTH_SHORT).show()
            }


        }


        binding.btnSave.setOnClickListener {
            if (verify!!.equals(true)){

                val cp = binding.etPassword.text.toString()
                if (binding.etPassword.text.toString().length <= 3){
                    binding.etPassword.error = "Password minimum 4 letters"
                    binding.etPassword.requestFocus()
                }
                else if (binding.etConfirmPassword.text.toString() != cp) {
                    binding.etConfirmPassword.error = "Password mismatch"
                    binding.etConfirmPassword.requestFocus()
                }

                else {
                    forgot()
                }

            }
            else{
                Toast.makeText(this,"Verify Otp",Toast.LENGTH_SHORT).show()
            }


        }


        setContentView(binding.root)
    }

    private fun otp() {
        val params = HashMap<String, String>()
        params[Constant.MOBILE] = binding.etPhoneNumber.text.toString().trim()

        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    Log.d("SIGNUP_RES", response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val jsonArray =
                            jsonObject.getJSONArray(Constant.DATA)

//                        session.setBoolean("is_logged_in", true)
//                        session.setData(Constant.USER_ID, jsonArray.getJSONObject(0).getString(Constant.ID))
//
                        val  otp = jsonArray.getJSONObject(0).getString("otp")
//
                        //  Toast.makeText(this, otp, Toast.LENGTH_SHORT).show()
                        // Toast.makeText(this, jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show()

                        sendotp(otp);



                    } else {

                        Toast.makeText(
                            this,
                            "" + jsonObject.getString(Constant.MESSAGE),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {

            }
        }, this, Constant.OTP, params, true)
    }

    private fun sendotp(otp : String) {
        val params: MutableMap<String, String> = HashMap()
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                binding.tvResend.visibility = View.VISIBLE
                binding.btnverify.visibility = View.VISIBLE
                binding.btnsent.visibility = View.GONE
                otp_get = otp
                startCountdown()
                Toast.makeText(this,"OTP Sent Successfully", Toast.LENGTH_SHORT).show()
            } else {
                // Toast.makeText(this, , Toast.LENGTH_SHORT).show()
                Toast.makeText(this,"OTP Failed", Toast.LENGTH_SHORT).show()

            }
        }, this, Constant.getOTPUrl("b45c58db6d261f2a",binding.etPhoneNumber.text.toString().trim(),otp), params, true)

    }


    private fun startCountdown() {
        countDownTimer = object : CountDownTimer(COUNTDOWN_TIME, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000

                // Update UI to show remaining seconds
                binding.tvResend.text = "Resend in $secondsLeft seconds"
            }

            override fun onFinish() {
                // Enable the button when countdown finishes
                binding.tvResend.text = "Don’t  receive any code  ? Resent"
            }
        }.start()
    }

    private fun resetCountdown() {
        countDownTimer?.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        resetCountdown()
    }


    private fun forgot() {
        val params: MutableMap<String, String> = HashMap()
        params[Constant.MOBILE] = binding.etPhoneNumber.text.toString().trim()
        params[Constant.PASSWORD] = binding.etPassword.text.toString().trim()
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    Log.d("SIGNUP_RES", response)
                    if (jsonObject.getBoolean(com.app.fivepl.helper.Constant.SUCCESS)) {

                        Toast.makeText(this, "Password Changed", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
                // Toast.makeText(this, , Toast.LENGTH_SHORT).show()
                DialogUtils.showCustomDialog(this, ""+java.lang.String.valueOf(response) + java.lang.String.valueOf(result))

            }
        }, this, com.app.fivepl.helper.Constant.FORGOT_PASSWORD, params, true)


    }


}