package com.app.fivepl.fragment

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.app.fivepl.R
import com.app.fivepl.adapter.TransactionAdapter
import com.app.fivepl.databinding.FragmentHomeBinding
import com.app.fivepl.databinding.FragmentTransactionBinding
import com.app.fivepl.helper.Constant
import com.app.fivepl.model.Transaction
import com.app.fivepl.utils.DialogUtils
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class TransactionFragment : Fragment() {


    lateinit var binding: FragmentTransactionBinding
    lateinit var activity: Activity
    lateinit var session: com.app.fivepl.helper.Session
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTransactionBinding.inflate(inflater, container, false)

        activity = getActivity() as Activity
        session = com.app.fivepl.helper.Session(activity)


        session = com.app.fivepl.helper.Session(activity)
        val swipeRefreshLayout: SwipeRefreshLayout = binding.swipeRefreshLayout
        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.rvTransactions.layoutManager = linearLayoutManager

        swipeRefreshLayout.setOnRefreshListener { transaction(swipeRefreshLayout) }
        transaction(swipeRefreshLayout)



        return binding.root




    }


    private fun transaction(swipeRefreshLayout:SwipeRefreshLayout) {
        val params: MutableMap<String, String> = HashMap()
        params[com.app.fivepl.helper.Constant.USER_ID] = session.getData(com.app.fivepl.helper.Constant.USER_ID)
        com.app.fivepl.helper.ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(com.app.fivepl.helper.Constant.SUCCESS)) {
                        val `object` = JSONObject(response)
                        val jsonArray: JSONArray = `object`.getJSONArray(com.app.fivepl.helper.Constant.DATA)
                        val g = Gson()
                        val transaction: java.util.ArrayList<Transaction> = java.util.ArrayList<Transaction>()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject1 = jsonArray.getJSONObject(i)
                            if (jsonObject1 != null) {
                                val group: Transaction = g.fromJson(jsonObject1.toString(), Transaction::class.java)
                                transaction.add(group)
                            } else {
                                break
                            }
                        }

                        val adapter = TransactionAdapter(activity, transaction)
                        binding.rvTransactions.adapter = adapter
                        swipeRefreshLayout.isRefreshing = false

                    } else {
                        swipeRefreshLayout.isRefreshing = false
                        DialogUtils.showCustomDialog(requireActivity(), ""+jsonObject.getString(Constant.MESSAGE))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }, activity, com.app.fivepl.helper.Constant.TRANSACTIONS_LIST, params, true)


    }


}