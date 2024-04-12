package com.app.fivepl.activity

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.anupkumarpanwar.scratchview.ScratchView
import com.anupkumarpanwar.scratchview.ScratchView.IRevealListener
import com.app.fivepl.databinding.ActivityScartchcardBinding
import com.app.fivepl.helper.ApiConfig
import com.app.fivepl.helper.Constant
import com.app.fivepl.utils.DialogUtils
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class ScartchcardActivity : AppCompatActivity() {

    lateinit var binding: ActivityScartchcardBinding
    lateinit var activity: Activity
    lateinit var session: com.app.fivepl.helper.Session

    var dialog: Dialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScartchcardBinding.inflate(layoutInflater)
        activity = this
        session = com.app.fivepl.helper.Session(activity)

        binding.ibBack.setOnClickListener {
            onBackPressed()
            finish()
        }





        val chance = session.getData(com.app.fivepl.helper.Constant.CHANCES)

        dialog = Dialog(this)


        binding.scratchView.setRevealListener(object : IRevealListener {
            override fun onRevealed(scratchView: ScratchView) {
                Toast.makeText(this@ScartchcardActivity, "Revealed!", Toast.LENGTH_SHORT).show()
                userdetails()
                apicall()
                scratchView.visibility = View.GONE

            }

            override fun onRevealPercentChangedListener(scratchView: ScratchView, percent: Float) {
                Log.d("Revealed", percent.toString())
            }
        })


        if(chance.equals("0")){
            binding.ScarchImg.visibility = View.GONE
            binding.image.visibility = View.VISIBLE
            DialogUtils.showCustomDialog(activity, "No More Chances")
        }
        else{
            binding.ScarchImg.visibility = View.VISIBLE
            binding.image.visibility = View.GONE
            apicall()
        }
        userdetails()


        return setContentView(binding!!.root)
    }


    private fun userdetails() {
        val params: MutableMap<String, String> = HashMap()
        params[Constant.USER_ID] = session!!.getData(Constant.USER_ID)
//        Toast.makeText(this,"" + session!!.getData(Constant.USER_ID),Toast.LENGTH_SHORT).show()
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val jsonArray: JSONArray =
                            jsonObject.getJSONArray(Constant.DATA)



                        session!!.setData(Constant.CHANCES, jsonArray.getJSONObject(0).getString(Constant.CHANCES))




                        binding.tvChance.text =  session.getData(com.app.fivepl.helper.Constant.CHANCES) + " Chances left"





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


    private fun apicall() {
        val params: MutableMap<String, String> = HashMap()
        params[Constant.USER_ID] = session!!.getData(Constant.USER_ID)
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {

                        binding.tvAmount.text = jsonObject.getString("amount")

                    } else {
                        DialogUtils.showCustomDialog(activity, ""+jsonObject.getString(Constant.MESSAGE))

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }, activity, Constant.SCRATCH_CARD, params, true)

        // Return a dummy intent, as the actual navigation is handled inside the callback

    }

}

