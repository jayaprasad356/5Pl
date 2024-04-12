package com.app.fivepl.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.app.fivepl.adapter.WithdrawalAdapter
import com.app.fivepl.model.Withdrawal
import com.app.fivepl.R
import com.app.fivepl.databinding.ActivityWithdrawalBinding
import com.app.fivepl.helper.ApiConfig
import com.app.fivepl.helper.Constant
import com.app.fivepl.utils.DialogUtils
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.StrictMath.round

class WithdrawalActivity : AppCompatActivity() {

    lateinit var binding: ActivityWithdrawalBinding
    private lateinit var activity: Activity
    private lateinit var session: com.app.fivepl.helper.Session


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWithdrawalBinding.inflate(layoutInflater)
        activity = this
        session = com.app.fivepl.helper.Session(activity)

        val swipeRefreshLayout: SwipeRefreshLayout = binding.swipeRefreshLayout

        binding.ibBack.setOnClickListener {
            onBackPressed()
        }

        binding.tvTotalEarnings.text = "₹ " +session.getData(Constant.BALANCE)


        binding.etAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val enteredText: String = s.toString()
                val enteredAmount = enteredText.toFloatOrNull()

                val fivePercent = calculateFivePercent(enteredText.toFloatOrNull())
                binding.etFee.setText("₹ " +fivePercent)



                val one = enteredText
                val two = fivePercent

                val result = one.toFloatOrNull()?.minus(two)



                if (result== null) {
                    binding.etIncome.setText("₹ 0")
                }
                else {
                    binding.etIncome.setText("₹ " +result)
                }


            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called after the text has changed.
            }
        })


        binding.llbank.setOnClickListener {
            val intent = Intent(activity, com.app.fivepl.activity.UpdatebankActivity::class.java)
            startActivity(intent)
        }

        binding.btnWithdraw.setOnClickListener {
            if (binding.etAmount.text.toString().isEmpty()) {
                binding.etAmount.error = "Please enter amount"
                binding.etAmount.requestFocus()
            } else {
                withdraw()
            }
        }

        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.rvHistory.layoutManager = linearLayoutManager


       // swipeRefreshLayout.setOnRefreshListener { history(swipeRefreshLayout) }

      //  history(swipeRefreshLayout)
        apicall()

        setContentView(binding.root)


    }

    private fun apicall() {
        val params: MutableMap<String, String> = HashMap()
        params[Constant.USER_ID] = session!!.getData(Constant.USER_ID)
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val jsonArray: JSONArray = jsonObject.getJSONArray(Constant.DATA)

                        val withdrawal_ins = jsonArray.getJSONObject(0).getString("withdrawal_ins")

                        val withdrawalHtml = Html.fromHtml(withdrawal_ins, Html.FROM_HTML_MODE_COMPACT)
                        binding.tvWithdraw.text = withdrawalHtml


                    } else {
                        DialogUtils.showCustomDialog(activity, ""+jsonObject.getString(Constant.MESSAGE))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()

                }
            }
        }, activity, Constant.SETTINGS, params, true)

        // Return a dummy intent, as the actual navigation is handled inside the callback

    }


    private fun history(swipeRefreshLayout:SwipeRefreshLayout) {
    val params: MutableMap<String, String> = HashMap()
        params["user_id"] = session.getData(Constant.USER_ID)
        com.app.fivepl.helper.ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(com.app.fivepl.helper.Constant.SUCCESS)) {
                        val jsonArray = jsonObject.getJSONArray(com.app.fivepl.helper.Constant.DATA)

                        val g = Gson()
                        val withdrawal: java.util.ArrayList<Withdrawal> =
                            java.util.ArrayList<Withdrawal>()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject1 = jsonArray.getJSONObject(i)
                            if (jsonObject1 != null) {
                                val group: Withdrawal = g.fromJson(jsonObject1.toString(), Withdrawal::class.java)
                                withdrawal.add(group)
                            } else {
                                break
                            }
                        }

                        val adapter = WithdrawalAdapter(activity, withdrawal)
                        binding.rvHistory.adapter = adapter
                        swipeRefreshLayout.isRefreshing = false


                    } else {
                        swipeRefreshLayout.isRefreshing = false
                        DialogUtils.showCustomDialog(this, ""+jsonObject.getString(Constant.MESSAGE))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(activity, java.lang.String.valueOf(response) + java.lang.String.valueOf(result), Toast.LENGTH_SHORT).show()
            }
        }, activity, com.app.fivepl.helper.Constant.WITHDRAWAL_LIST, params, true)
    }

    private fun withdraw() {

        val params: MutableMap<String, String> = HashMap()
        params["user_id"] = session.getData(com.app.fivepl.helper.Constant.USER_ID)
        params["amount"] = binding.etAmount.text.toString()
        com.app.fivepl.helper.ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(com.app.fivepl.helper.Constant.SUCCESS)) {
                        val jsonArray = jsonObject.getJSONArray(com.app.fivepl.helper.Constant.DATA)
                        session.setData(
                            com.app.fivepl.helper.Constant.BALANCE, jsonArray.getJSONObject(0).getString(
                                com.app.fivepl.helper.Constant.BALANCE))
                        session.setData(
                            com.app.fivepl.helper.Constant.TOTAL_WITHDRAWAL, jsonArray.getJSONObject(0).getString(
                                com.app.fivepl.helper.Constant.TOTAL_WITHDRAWAL))
                        Toast.makeText(activity, jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show()
                        moveToHomeActivity();
                    } else
                    {


                        DialogUtils.showCustomDialog(activity, ""+jsonObject.getString(Constant.MESSAGE))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(this, java.lang.String.valueOf(response) + java.lang.String.valueOf(result), Toast.LENGTH_SHORT).show()
            }
        }, this, com.app.fivepl.helper.Constant.WITHDRAWALS, params, true)


    }

    private fun showCustomDialog() {
        val builder = AlertDialog.Builder(this)
        val dialogView: View = layoutInflater.inflate(R.layout.custom_dialog, null)

        builder.setView(dialogView)
        val dialog = builder.create()
        dialog.setOnDismissListener {
            // This code will be executed when the dialog is dismissed
            moveToHomeActivity()
        }
        dialog.show()
    }

    private fun moveToHomeActivity() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    fun calculateFivePercent(amount: Float?): Int {
        // Check if amount is not null
        val fivePercent = amount?.times(0.05f)?.toDouble() ?: 0.0
        // Round off to the nearest integer
        return round(fivePercent).toInt()
    }
}