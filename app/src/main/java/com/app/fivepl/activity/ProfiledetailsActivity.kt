package com.app.fivepl.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.fivepl.R
import com.app.fivepl.databinding.ActivityProfiledetailsBinding
import com.app.fivepl.helper.ApiConfig
import com.app.fivepl.helper.Constant
import com.app.fivepl.helper.Session
import com.app.fivepl.utils.DialogUtils
import com.google.firebase.dynamiclinks.PendingDynamicLinkData
import com.google.firebase.dynamiclinks.dynamicLinks
import org.json.JSONException
import org.json.JSONObject
import java.util.regex.Pattern


class ProfiledetailsActivity : AppCompatActivity() {

    lateinit var binding: ActivityProfiledetailsBinding
    lateinit var activity: Activity
    lateinit var session: Session

    var mobilenumber: String? = null
    var otp_get: String? = null

    var verify: Boolean? = false

    private var countDownTimer: CountDownTimer? = null
    private val COUNTDOWN_TIME = 45000L // 45 seconds in milliseconds



    private val indianStates = arrayOf(
        "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh",
        "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand", "Karnataka",
        "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram",
        "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu",
        "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfiledetailsBinding.inflate(layoutInflater)


        binding.ibBack.setOnClickListener() {
            onBackPressed()
        }

        activity = this
        session = Session(activity)


        com.google.firebase.Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData: PendingDynamicLinkData? ->
                // Get deep link from result (may be null if no link is found)
                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link

                    val extractedString = extractStringAfterLastSlash(deepLink.toString())

                    binding.etReferCode.setText(extractedString + "")
                }

                // Handle the deep link. For example, open the linked
                // content, or apply promotional credit to the user's
                // account.
                // ...
            }
            .addOnFailureListener(this) { e -> Log.w(ApiConfig.TAG, "getDynamicLink:onFailure", e) }

        // Set click listener for the "Resend" button
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

            }

            else {
                Toast.makeText(this,"Invalid",Toast.LENGTH_SHORT).show()
            }


        }







        mobilenumber = session.getData(Constant.MOBILE)


                val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, indianStates)
                binding.autoCompleteTextView.setAdapter(arrayAdapter)




                binding.btnSave.setOnClickListener {
                    val cp = binding.etPassword.text.toString()
                    if (verify!!.equals(false)){
                        Toast.makeText(this,"Verify Otp",Toast.LENGTH_SHORT).show()
                    }

                    else if (binding.etPhoneNumber.text.toString().isEmpty()) {
                        binding.etPhoneNumber.error = "Please enter mobile number"
                        binding.etPhoneNumber.requestFocus()
                    } else if (binding.etPhoneNumber.text.toString().length != 10) {
                        binding.etPhoneNumber.error = "Please enter valid mobile number"
                        binding.etPhoneNumber.requestFocus()
                    }
                    else if (binding.etPassword.text.toString().length <= 3){
                        binding.etPassword.error = "Password minimum 4 letters"
                        binding.etPassword.requestFocus()
                    }
                    else if (binding.etConfirmPassword.text.toString() != cp) {
                        binding.etConfirmPassword.error = "Password mismatch"
                        binding.etConfirmPassword.requestFocus()
                    }

                    else if (binding.etName.text.toString().isEmpty()) {
                        binding.etName.error = "Enter Name"
                        return@setOnClickListener
                    } else if (binding.etName.text.toString().length < 4) {
                        binding.etName.error = "Name should be atleast 4 characters"
                        return@setOnClickListener
                    } else if (binding.etEmail.text.toString().isEmpty()) {
                        binding.etEmail.error = "Enter Email"
                        return@setOnClickListener
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text).matches()) {
                        binding.etEmail.error = "Enter a valid Email address"
                        return@setOnClickListener
                    } else if (binding.etAge.text.toString().isEmpty()) {
                        binding.etAge.error = "Enter Age"
                        return@setOnClickListener
                    } else if (binding.etCity.text.toString().isEmpty()) {
                        binding.etCity.error = "Enter City"
                        return@setOnClickListener
                    } else if (!indianStates.contains(binding.autoCompleteTextView.text.toString())) {
                        // Check if the entered state is not in the list of Indian states
                        binding.autoCompleteTextView.error = "Select a valid state"
                        return@setOnClickListener
                    } else if (binding.etReferCode.text.toString().isEmpty()) {
                        binding.etReferCode.error = "Enter Referral Code"
                        return@setOnClickListener
                    } else {
                        register()
                    }
                }


                setContentView(binding.root)



    }

    private fun isValidEmail(email: String): Boolean {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }
    fun extractStringAfterLastSlash(url: String): String {
        val lastSlashIndex = url.lastIndexOf('/')
        return if (lastSlashIndex != -1 && lastSlashIndex < url.length - 1) {
            url.substring(lastSlashIndex + 1)
        } else {
            "No characters found after the last slash."
        }
    }


    private fun register() {
        val params = HashMap<String, String>()
        params[Constant.MOBILE] = binding.etPhoneNumber.text.toString().trim()
        params[Constant.PASSWORD] = binding.etPassword.text.toString().trim()
        params[Constant.NAME] = binding.etName.text.toString().trim()
        params[Constant.EMAIL] = binding.etEmail.text.toString().trim()
        params[Constant.AGE] = binding.etAge.text.toString().trim()
        params[Constant.CITY] = binding.etCity.text.toString().trim()
        params[Constant.STATE] = binding.autoCompleteTextView.text.toString().trim()
        params[Constant.REFERRED_BY] = binding.etReferCode.text.toString().trim()
        params[Constant.DEVICE_ID] = Constant.getDeviceId(activity)

        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    Log.d("SIGNUP_RES", response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val jsonArray =
                            jsonObject.getJSONArray(Constant.DATA)

                        session.setBoolean("is_logged_in", true)
                        session.setData(Constant.USER_ID, jsonArray.getJSONObject(0).getString(Constant.ID))
                        session.setData(Constant.NAME, jsonArray.getJSONObject(0).getString(Constant.NAME))
                        session.setData(Constant.MOBILE, jsonArray.getJSONObject(0).getString(Constant.MOBILE))
                        session.setData(Constant.EMAIL, jsonArray.getJSONObject(0).getString(Constant.EMAIL))
                        session.setData(Constant.AGE, jsonArray.getJSONObject(0).getString(Constant.AGE))
                        session.setData(Constant.CITY, jsonArray.getJSONObject(0).getString(Constant.CITY))
                        session.setData(Constant.STATE, jsonArray.getJSONObject(0).getString(Constant.STATE))
                        session.setData(Constant.REFER_CODE,jsonArray.getJSONObject(0).getString(Constant.REFER_CODE))


                        session!!.setData(com.app.fivepl.helper.Constant.USER_ID, jsonArray.getJSONObject(0).getString(com.app.fivepl.helper.Constant.ID))
                        session.setData(com.app.fivepl.helper.Constant.NAME, jsonArray.getJSONObject(0).getString(com.app.fivepl.helper.Constant.NAME))
                        session.setData(com.app.fivepl.helper.Constant.MOBILE, jsonArray.getJSONObject(0).getString(com.app.fivepl.helper.Constant.MOBILE))
                        session.setData(com.app.fivepl.helper.Constant.EMAIL, jsonArray.getJSONObject(0).getString(com.app.fivepl.helper.Constant.EMAIL))
                        session.setData(com.app.fivepl.helper.Constant.AGE, jsonArray.getJSONObject(0).getString(com.app.fivepl.helper.Constant.AGE))
                        session.setData(com.app.fivepl.helper.Constant.CITY, jsonArray.getJSONObject(0).getString(com.app.fivepl.helper.Constant.CITY))
                        session.setData(com.app.fivepl.helper.Constant.STATE, jsonArray.getJSONObject(0).getString(com.app.fivepl.helper.Constant.STATE))
                        session.setData(com.app.fivepl.helper.Constant.REFER_CODE, jsonArray.getJSONObject(0).getString(com.app.fivepl.helper.Constant.REFER_CODE))


                        Toast.makeText(this, "" + jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show()

                        val intent = Intent(activity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {

                        Toast.makeText(this, "" + jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(
                    this,
                    java.lang.String.valueOf(response) + java.lang.String.valueOf(result),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }, this, Constant.REGISTER, params, true)


    }

    override fun onBackPressed() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Exit")
        alertDialogBuilder.setMessage("Are you sure you want to exit?")
        alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.setPositiveButton("Yes") { dialog, which ->
            finishAffinity() // Close the app
        }
        alertDialogBuilder.setNegativeButton("No") { dialog, which -> dialog.dismiss() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()

        // Set text color for positive button
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(resources.getColor(R.color.primary_color))
        // Set text color for negative button
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(resources.getColor(R.color.primary_color))
    }

    private fun login() {
        val params: MutableMap<String, String> = HashMap()
        params[Constant.MOBILE] = session!!.getData(Constant.MOBILE)
        params[Constant.DEVICE_ID] =  Constant.getDeviceId(activity)
        params["otp"] = binding.etOTP.text.toString()
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    Log.d("SIGNUP_RES", response)
                    if (jsonObject.getBoolean(com.app.fivepl.helper.Constant.SUCCESS)) {
                        val jsonArray = jsonObject.getJSONArray(com.app.fivepl.helper.Constant.DATA)
                        //Toast.makeText(activity, jsonObject.getString(com.app.pocketfarm.helper.Constant.MESSAGE), Toast.LENGTH_SHORT).show()
                        session!!.setBoolean("is_logged_in", true)
                        session!!.setData(com.app.fivepl.helper.Constant.USER_ID, jsonArray.getJSONObject(0).getString(com.app.fivepl.helper.Constant.ID))
                        session.setData(com.app.fivepl.helper.Constant.NAME, jsonArray.getJSONObject(0).getString(com.app.fivepl.helper.Constant.NAME))
                        session.setData(com.app.fivepl.helper.Constant.MOBILE, jsonArray.getJSONObject(0).getString(com.app.fivepl.helper.Constant.MOBILE))
                        session.setData(com.app.fivepl.helper.Constant.EMAIL, jsonArray.getJSONObject(0).getString(com.app.fivepl.helper.Constant.EMAIL))
                        session.setData(com.app.fivepl.helper.Constant.AGE, jsonArray.getJSONObject(0).getString(com.app.fivepl.helper.Constant.AGE))
                        session.setData(com.app.fivepl.helper.Constant.CITY, jsonArray.getJSONObject(0).getString(com.app.fivepl.helper.Constant.CITY))
                        session.setData(com.app.fivepl.helper.Constant.STATE, jsonArray.getJSONObject(0).getString(com.app.fivepl.helper.Constant.STATE))
                        session.setData(com.app.fivepl.helper.Constant.REFER_CODE, jsonArray.getJSONObject(0).getString(com.app.fivepl.helper.Constant.REFER_CODE))


                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {
                        val message = jsonObject.getString(Constant.MESSAGE)

                        if (message.equals("Your Mobile Number is not Registered")) {
                            val intent = Intent(this, ProfiledetailsActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        else {
                            DialogUtils.showCustomDialog(this, ""+jsonObject.getString(com.app.fivepl.helper.Constant.MESSAGE))
                        }

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
                // Toast.makeText(this, , Toast.LENGTH_SHORT).show()
                DialogUtils.showCustomDialog(this, ""+java.lang.String.valueOf(response) + java.lang.String.valueOf(result))

            }
        }, this, com.app.fivepl.helper.Constant.LOGIN, params, true)


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


}