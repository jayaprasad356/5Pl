package com.app.fivepl.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.fivepl.databinding.ActivitySupportBinding
import com.app.fivepl.helper.ApiConfig
import com.app.fivepl.helper.Constant
import com.app.fivepl.utils.DialogUtils
import com.zoho.commons.InitConfig
import com.zoho.livechat.android.listeners.InitListener
import com.zoho.salesiqembed.ZohoSalesIQ
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class SupportActivity : AppCompatActivity() {

    lateinit var binding: ActivitySupportBinding
    lateinit var activity: Activity
    lateinit var session: com.app.fivepl.helper.Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySupportBinding.inflate(layoutInflater)
        activity = this
        session = com.app.fivepl.helper.Session(activity)

        binding.ibBack.setOnClickListener {
            onBackPressed()
            finish()
        }

        val initConfig = InitConfig()
        // Set your font configurations

        ZohoSalesIQ.init(application, "q%2FetVzn%2B7suBUISHrv%2B4MM5zHsAL4a1dOKYB2tz2WDqUDLMIygjwdSQOZJfKPQMJw0ZesvJqzxqA2OElz%2BHICEfHeNgKxY89MmM70uYQKySEia9eZjTmcA%3D%3D_in", "4%2Fd2z2OovwNUlhzjsAI%2Bk0DfMTZp4hGY%2Fn85U%2Fsq6yplNfH7TtW0rfJXO4V%2BSXjczU5Sqinj5cqzbGKBH2c3KJFrmHUQGflGlc49MR79%2BSFxjRQ8BNLjz86l1rMZtYOW1fJLMC8Kl6DtM%2BJfTJFfroseSg%2BjoxQU", initConfig, object :
            InitListener {
            override fun onInitSuccess() {
                // fit place to show the chat launcher
                ZohoSalesIQ.Launcher.show(ZohoSalesIQ.Launcher.VisibilityMode.ALWAYS)

            }

            override fun onInitError(errorCode: Int, errorMessage: String) {
                // Handle initialization errors
            }
        })





        apicall()

        return setContentView(binding!!.root)
    }

    override fun onResume() {
        super.onResume()
        ZohoSalesIQ.Launcher.show(ZohoSalesIQ.Launcher.VisibilityMode.ALWAYS)
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
                        val whatsapplink = jsonArray.getJSONObject(0).getString("whatsapp_group")
                        val Telegram = jsonArray.getJSONObject(0).getString("telegram_channel")

                        binding.tvJointelegram.setOnClickListener {
                            openTelegram(Telegram)

                        }
                        binding.tvJoinwhatsapp.setOnClickListener {
                            openWhatsApp(whatsapplink)

                        }

                        binding.tvSharetelegram.setOnClickListener {
                            val sendIntent = Intent()
                            sendIntent.action = Intent.ACTION_SEND
                            sendIntent.putExtra(Intent.EXTRA_TEXT, Telegram)
                            sendIntent.type = "text/plain"
                            startActivity(sendIntent)
                        }

                        binding.tvSharewhatsapp.setOnClickListener {
                            val sendIntent = Intent()
                            sendIntent.action = Intent.ACTION_SEND
                            sendIntent.putExtra(Intent.EXTRA_TEXT, whatsapplink)
                            sendIntent.type = "text/plain"
                            startActivity(sendIntent)
                        }

                    } else {
                        DialogUtils.showCustomDialog(
                            activity,
                            "" + jsonObject.getString(Constant.MESSAGE)
                        )
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()

                }
            }
        }, activity, Constant.SETTINGS, params, true)

        // Return a dummy intent, as the actual navigation is handled inside the callback

    }

    private fun openTelegram(telegram: String) {
        val url = telegram;
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    private fun openWhatsApp(whatsapplink: String) {
        val url = whatsapplink
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)

    }

}