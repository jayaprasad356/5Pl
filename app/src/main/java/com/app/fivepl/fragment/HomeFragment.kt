package com.app.fivepl.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.fivepl.activity.ChangepasswordActivity
import com.app.fivepl.activity.HomeActivity
import com.app.fivepl.activity.PaymentActivity
import com.app.fivepl.adapter.WithdrawalAdapter
import com.app.fivepl.model.Withdrawal
import com.app.fivepl.databinding.FragmentHomeBinding
import com.app.fivepl.helper.ApiConfig
import com.app.fivepl.helper.Constant
import com.app.fivepl.utils.DialogUtils
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var activity: Activity
    lateinit var session: com.app.fivepl.helper.Session


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentHomeBinding.inflate(inflater, container, false)





        activity = getActivity() as Activity
        session = com.app.fivepl.helper.Session(activity)




        userdetails()

        (activity as HomeActivity).binding.rlToolbar.visibility = View.VISIBLE

        binding.rlRecharge.setOnClickListener {
            startActivity(activity.intent.setClassName(activity, PaymentActivity::class.java.name))
        }

//
//        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
//        binding.rvtransactionitem.layoutManager = linearLayoutManager
//
//        transaction()

        return binding.root

    }

//    private fun transaction() {
//        val params: MutableMap<String, String> = HashMap()
//        params[com.app.fivepl.helper.Constant.USER_ID] = session.getData(com.app.fivepl.helper.Constant.USER_ID)
//        com.app.fivepl.helper.ApiConfig.RequestToVolley({ result, response ->
//            if (result) {
//                try {
//                    val jsonObject = JSONObject(response)
//                    if (jsonObject.getBoolean(com.app.fivepl.helper.Constant.SUCCESS)) {
//                        val `object` = JSONObject(response)
//                        val jsonArray: JSONArray = `object`.getJSONArray(com.app.fivepl.helper.Constant.DATA)
//                        val g = Gson()
//                        val withdrawal: java.util.ArrayList<Withdrawal> =
//                            java.util.ArrayList<Withdrawal>()
//                        // Limiting to the first five items
//                        val limit = minOf(jsonArray.length(), 5)
//                        for (i in 0 until limit) {
//                            val jsonObject1 = jsonArray.getJSONObject(i)
//                            if (jsonObject1 != null) {
//                                val group: Withdrawal = g.fromJson(jsonObject1.toString(), Withdrawal::class.java)
//                                withdrawal.add(group)
//                            } else {
//                                break
//                            }
//                        }
//                        binding.animationView.visibility = View.GONE
//
//                        val adapter = WithdrawalAdapter(activity, withdrawal)
//                        binding.rvtransactionitem.adapter = adapter
//
//                    } else {
//
//                        //  DialogUtils.showCustomDialog(activity, ""+jsonObject.getString(Constant.MESSAGE))
//
//                        binding.animationView.visibility = View.VISIBLE
//                    }
//                } catch (e: JSONException) {
//                    e.printStackTrace()
//                    Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
//                }
//            }
//        }, activity, com.app.fivepl.helper.Constant.WITHDRAWAL_LIST, params, true)
//    }


    private fun userdetails() {
        val params: MutableMap<String, String> = HashMap()
        params[Constant.USER_ID] = session!!.getData(Constant.USER_ID)
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val jsonArray: JSONArray =
                            jsonObject.getJSONArray(Constant.DATA)



                        session!!.setData(Constant.ACCOUNT_NUM, jsonArray.getJSONObject(0).getString(Constant.ACCOUNT_NUM))
                        session!!.setData(Constant.HOLDER_NAME, jsonArray.getJSONObject(0).getString(Constant.HOLDER_NAME))
                        session!!.setData(Constant.BANK, jsonArray.getJSONObject(0).getString(Constant.BANK))
                        session!!.setData(Constant.BRANCH, jsonArray.getJSONObject(0).getString(Constant.BRANCH))
                        session!!.setData(Constant.IFSC, jsonArray.getJSONObject(0).getString(Constant.IFSC)
                        )
                        session!!.setData(
                            Constant.AGE,
                            jsonArray.getJSONObject(0).getString(Constant.AGE)
                        )
                        session!!.setData(
                            Constant.CITY,
                            jsonArray.getJSONObject(0).getString(Constant.CITY)
                        )
                        session!!.setData(
                            Constant.STATE,
                            jsonArray.getJSONObject(0).getString(Constant.STATE)
                        )
                        session!!.setData(
                            Constant.DEVICE_ID,
                            jsonArray.getJSONObject(0).getString(Constant.DEVICE_ID)
                        )
                        session!!.setData(Constant.RECHARGE, jsonArray.getJSONObject(0).getString(Constant.RECHARGE))
                        session!!.setData(Constant.TODAY_INCOME, jsonArray.getJSONObject(0).getString(Constant.TODAY_INCOME))
                        session!!.setData(Constant.TOTAL_INCOME, jsonArray.getJSONObject(0).getString(Constant.TOTAL_INCOME))
                        session!!.setData(Constant.BALANCE, jsonArray.getJSONObject(0).getString(Constant.BALANCE))
                        session!!.setData(Constant.WITHDRAWAL_STATUS, jsonArray.getJSONObject(0).getString(Constant.WITHDRAWAL_STATUS))
                        session!!.setData(Constant.TEAM_SIZE, jsonArray.getJSONObject(0).getString(Constant.TEAM_SIZE))
                        session!!.setData(Constant.VALID_TEAM, jsonArray.getJSONObject(0).getString(Constant.VALID_TEAM))
                        session!!.setData(Constant.TOTAL_WITHDRAWAL, jsonArray.getJSONObject(0).getString(Constant.TOTAL_WITHDRAWAL))
                        session!!.setData(Constant.TEAM_INCOME, jsonArray.getJSONObject(0).getString(Constant.TEAM_INCOME))
                        session!!.setData(Constant.TOTAL_ASSETS, jsonArray.getJSONObject(0).getString(Constant.TOTAL_ASSETS))
                        session!!.setData(Constant.PROFILE, jsonArray.getJSONObject(0).getString(Constant.PROFILE))
                        session!!.setData(Constant.PASSWORD, jsonArray.getJSONObject(0).getString(Constant.PASSWORD))
                        session!!.setData(Constant.BLOCKED, jsonArray.getJSONObject(0).getString(Constant.BLOCKED))


                        // if paassword is empty or null move change password activity
                        if (session.getData(Constant.PASSWORD).isEmpty() || session.getData(Constant.PASSWORD).equals("null")) {
                            val intent = Intent(activity, ChangepasswordActivity::class.java)
                            startActivity(intent)
                            activity.finish()
                        }


                        val blocked  = jsonArray.getJSONObject(0).getString(Constant.BLOCKED)


                        if (blocked.equals("1")) {
                            session.logoutUser(activity)
                        }



                        binding.tvRecharge.text =  "Recharge Rs." + session.getData(Constant.RECHARGE)
                        binding.tvTotalIncome.text = "₹" + session.getData(Constant.TOTAL_INCOME)
                        binding.tvTodayIncome.text = "₹" + session.getData(Constant.TODAY_INCOME)
                        binding.tvRechargeBalance.text = "₹" + session.getData(Constant.RECHARGE)
                        binding.tvRemainingBalance.text = "₹" + session.getData(Constant.BALANCE)


                        binding.tvName.text = "Hi " + session.getData(com.app.fivepl.helper.Constant.NAME)
                        binding.tvReferralCode.text =  session.getData(com.app.fivepl.helper.Constant.REFER_CODE)


                    } else {
                        DialogUtils.showCustomDialog(activity, ""+jsonObject.getString(Constant.MESSAGE))

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }, activity, Constant.USER_DETAILS, params, true)

        // Return a dummy intent, as the actual navigation is handled inside the callback

    }




}