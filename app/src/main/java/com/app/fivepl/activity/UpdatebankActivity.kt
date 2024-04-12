package com.app.fivepl.activity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.app.fivepl.databinding.ActivityUpdatebankBinding
import com.app.fivepl.helper.ApiConfig
import com.app.fivepl.helper.Constant
import com.app.fivepl.helper.Session
import com.app.fivepl.utils.DialogUtils
import org.json.JSONException
import org.json.JSONObject

class UpdatebankActivity : AppCompatActivity() {

    lateinit var binding: ActivityUpdatebankBinding
    lateinit var activity: Activity
    lateinit var session: Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdatebankBinding.inflate(layoutInflater)
        activity = this
        session = Session(activity)

        binding.ibBack.setOnClickListener {
            onBackPressed()
        }

        binding.etBankName.setText(session.getData(Constant.BANK))
        binding.etBranchName.setText(session.getData(Constant.BRANCH))
        binding.etHolderName.setText(session.getData(Constant.HOLDER_NAME))
        binding.etIFSCCode.setText(session.getData(Constant.IFSC))
        binding.etAccountNumber.setText(session.getData(Constant.ACCOUNT_NUM))

        binding.btnBankDetails.setOnClickListener {
            if (binding.etHolderName.text.toString().isEmpty()) {
                binding.etHolderName.error = "Please enter account holder name"
                binding.etHolderName.requestFocus()
            }
            else if (binding.etHolderName.text.toString().length < 5){
                binding.etHolderName.error = "please enter 5 characters"
                binding.etHolderName.requestFocus()
            }
            else if (binding.etAccountNumber.text.toString().isEmpty()) {
                binding.etAccountNumber.error = "Please enter account number"
                binding.etAccountNumber.requestFocus()
            } else if (binding.etIFSCCode.text.toString().isEmpty()) {
                binding.etIFSCCode.error = "Please enter IFSC code"
                binding.etIFSCCode.requestFocus()
            }
            else if (binding.etBankName.text.toString().isEmpty()) {
                binding.etBankName.error = "Please enter bank name"
                binding.etBankName.requestFocus()
            }
            else if (binding.etBranchName.text.toString().isEmpty()) {
                binding.etBranchName.error = "Please enter branch name"
                binding.etBranchName.requestFocus()
            }

            else {
                              updateBankDetails()
            }
        }
        setContentView(binding.root)

    }

    private fun updateBankDetails() {
        val params: MutableMap<String, String> = HashMap()
        params[Constant.USER_ID] = session!!.getData(Constant.USER_ID)
        params[Constant.HOLDER_NAME] = binding.etHolderName.text.toString().trim()
        params[Constant.ACCOUNT_NUM] = binding.etAccountNumber.text.toString().trim()
        params[Constant.IFSC] = binding.etIFSCCode.text.toString().trim()
        params[Constant.BANK] = binding.etBankName.text.toString().trim()
        params[Constant.BRANCH] = binding.etBranchName.text.toString().trim()
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {


                        Toast.makeText(activity, jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show()
                        session.setData(Constant.BANK, binding.etBankName.text.toString())
                        session.setData(Constant.BRANCH, binding.etBranchName.text.toString())
                        session.setData(Constant.HOLDER_NAME, binding.etHolderName.text.toString())
                        session.setData(Constant.IFSC, binding.etIFSCCode.text.toString())
                        session.setData(Constant.ACCOUNT_NUM, binding.etAccountNumber.text.toString())
                        onBackPressed()



                    } else {
                        DialogUtils.showCustomDialog(activity, ""+jsonObject.getString(Constant.MESSAGE))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()

                }
            }
        }, activity, Constant.UPDATE_BANK, params, true)

        // Return a dummy intent, as the actual navigation is handled inside the callback

    }

}