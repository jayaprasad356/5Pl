package com.app.fivepl.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.app.fivepl.databinding.ActivityChangepasswordBinding
import com.app.fivepl.helper.ApiConfig
import com.app.fivepl.helper.Constant
import com.app.fivepl.helper.Session
import org.json.JSONException
import org.json.JSONObject

class ChangepasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangepasswordBinding
    private lateinit var activity: Activity
    private lateinit var session:Session


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangepasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        activity = this
        session = Session(activity)

        binding.ibBack.setOnClickListener {
            onBackPressed()
        }


        binding.btnChangePassword.setOnClickListener {

            val cp = binding.etPassword.text.toString()
            if (binding.etPassword.text.toString().isEmpty()) {
                binding.etPassword.error = "Please enter old password"
                binding.etPassword.requestFocus()
            }
            else if (binding.etPassword.text.toString().length <= 3){
                binding.etPassword.error = "Password minimum 4 letters"
                binding.etPassword.requestFocus()
            }
            else if (binding.etConfirmPassword.text.toString() != cp) {
                binding.etConfirmPassword.error = "Password mismatch"
                binding.etConfirmPassword.requestFocus()
            }

            else {
               apicall()
            }
        }



    }


    private fun apicall() {
        val params = HashMap<String, String>()
        params[Constant.USER_ID] = session.getData(Constant.USER_ID)
        params[Constant.PASSWORD] = binding.etPassword.text.toString().trim()


        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    Log.d("SIGNUP_RES", response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {


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
        }, this, Constant.CHANGE_PASSWORD, params, true)


    }

}