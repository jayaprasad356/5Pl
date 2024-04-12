package com.app.fivepl.activity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.app.fivepl.adapter.ActivateplansAdapter
import com.app.fivepl.model.MyPlan
import com.app.fivepl.databinding.ActivityMyProductionBinding
import com.app.fivepl.helper.Session
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MyProductionActivity : AppCompatActivity() {

    lateinit var binding: ActivityMyProductionBinding
    lateinit var activity: Activity
    lateinit var session: Session

    private var adapter: com.app.fivepl.adapter.SliderAdapterExample? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyProductionBinding.inflate(layoutInflater)

        activity = this
        session = Session(activity)

        val swipeRefreshLayout: SwipeRefreshLayout = binding.swipeRefreshLayout
        activateplan(swipeRefreshLayout)

        swipeRefreshLayout.setOnRefreshListener { activateplan(swipeRefreshLayout) }

        binding.ibBack.setOnClickListener{
            onBackPressed()
        }


        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.rvplan.layoutManager = linearLayoutManager
        setContentView(binding.root)


    }


    private fun activateplan(swipeRefreshLayout:SwipeRefreshLayout) {
        val params: MutableMap<String, String> = HashMap()
        params[com.app.fivepl.helper.Constant.USER_ID]= session.getData(com.app.fivepl.helper.Constant.USER_ID)
        com.app.fivepl.helper.ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(com.app.fivepl.helper.Constant.SUCCESS)) {
                        val `object` = JSONObject(response)
                        val jsonArray: JSONArray = `object`.getJSONArray(com.app.fivepl.helper.Constant.DATA)
                        val g = Gson()
                        val myPlans: java.util.ArrayList<MyPlan> =
                            java.util.ArrayList<MyPlan>()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject1 = jsonArray.getJSONObject(i)
                            if (jsonObject1 != null) {
                                val group: MyPlan = g.fromJson(jsonObject1.toString(), MyPlan::class.java)
                                myPlans.add(group)
                            } else {
                                break
                            }
                        }
                        //    Toast.makeText(getActivity(), "1" + jsonObject.getString(Constant.MESSAGE).toString(), Toast.LENGTH_SHORT).show()
                        //important
                        val adapter = ActivateplansAdapter(activity, myPlans)
                        binding.rvplan.adapter = adapter
                        binding.rvplan.visibility = View.VISIBLE
                        binding.animationView.visibility = View.GONE
                        swipeRefreshLayout.isRefreshing = false


                    } else {
                        binding.rvplan.visibility = View.GONE
                        binding.animationView.visibility = View.VISIBLE
                        swipeRefreshLayout.isRefreshing = false
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }, activity, com.app.fivepl.helper.Constant.USER_PLAN_LIST, params, true)



    }

}