package com.app.fivepl.fragment

import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.fivepl.activity.HomeActivity
import com.app.fivepl.adapter.MyTeamAdapter
import com.app.fivepl.model.MyTeam
import com.app.fivepl.databinding.FragmentMyteamBinding
import com.app.fivepl.helper.Constant
import com.bumptech.glide.Glide
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class MyteamFragment : Fragment() {

    lateinit var binding: FragmentMyteamBinding
    lateinit var activity: Activity
    lateinit var session: com.app.fivepl.helper.Session

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMyteamBinding.inflate(inflater, container, false)

        activity = getActivity() as Activity
        session = com.app.fivepl.helper.Session(activity)

//        // Home Activity binding.rlHeader.visibility = View.GONE
        (activity as HomeActivity).binding.rlToolbar.visibility = View.GONE


        binding.tvMobile.text = session.getData(Constant.MOBILE)


        Glide.with(activity).load(session.getData(Constant.PROFILE)).placeholder(com.app.fivepl.R.drawable.profile).into(binding.ivProfile)


        binding.rlBonus1.setOnClickListener {
            binding.rlBonus1.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#6b59e1"))
            binding.rlBonus2.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#BBB1FF"))
            binding.rlBonus3.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#BBB1FF"))
            binding.rvMyTeamB.visibility = View.GONE
            binding.tvBonus1.setTextColor(Color.parseColor("#ffffff"))
            binding.tvBonus2.setTextColor(Color.parseColor("#000000"))
            binding.tvBonus3.setTextColor(Color.parseColor("#000000"))
            referlist("b")

        }

        binding.rlBonus2.setOnClickListener {
            binding.rlBonus1.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#BBB1FF"))
            binding.rlBonus2.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#6b59e1"))
            binding.rlBonus3.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#BBB1FF"))
            binding.rvMyTeamB.visibility = View.GONE

            binding.tvBonus1.setTextColor(Color.parseColor("#000000"))
            binding.tvBonus2.setTextColor(Color.parseColor("#ffffff"))
            binding.tvBonus3.setTextColor(Color.parseColor("#000000"))
            referlist("c")
        }

        binding.rlBonus3.setOnClickListener {
            binding.rlBonus1.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#BBB1FF"))
            binding.rlBonus2.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#BBB1FF"))
            binding.rlBonus3.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#6b59e1"))
            binding.rvMyTeamB.visibility = View.GONE

            binding.tvBonus1.setTextColor(Color.parseColor("#000000"))
            binding.tvBonus2.setTextColor(Color.parseColor("#000000"))
            binding.tvBonus3.setTextColor(Color.parseColor("#ffffff"))
            referlist("d")
        }



        binding.tvTeamsize.text =  session.getData(com.app.fivepl.helper.Constant.TEAM_SIZE)
        binding.tvValidTeam.text =  session.getData(com.app.fivepl.helper.Constant.VALID_TEAM)


        binding.tvCopy.text = session.getData(com.app.fivepl.helper.Constant.REFER_CODE)

        binding.rlCopy.setOnClickListener {
            val text = binding.tvCopy.text.toString()
            val clipboard = activity.getSystemService(Activity.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = android.content.ClipData.newPlainText("Copied Text", text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(activity, "Copied", Toast.LENGTH_SHORT).show()
        }

        binding.btnInvite.setOnClickListener {
            val shareBody = "Hey, I am using WeAgri App. It's a great app to earn money. Use my refer code " + session.getData(
                com.app.fivepl.helper.Constant.REFER_CODE) + " to get 100 coins on signup. Download the app from the link below.\n" + ""
            val sharingIntent = android.content.Intent(android.content.Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "WeAgri")
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
            startActivity(android.content.Intent.createChooser(sharingIntent, "Share using"))
        }





        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.rvMyTeamB.layoutManager = linearLayoutManager






        referlist("b")


        return binding.root



    }

    private fun referlist(level: String) {
        binding.rvMyTeamB.visibility = View.GONE
        binding.tvMember.text = "Team - " + 0;
        val params: MutableMap<String, String> = HashMap()
        params[com.app.fivepl.helper.Constant.USER_ID] = session.getData(com.app.fivepl.helper.Constant.USER_ID)
        params[com.app.fivepl.helper.Constant.LEVEL] = level
        com.app.fivepl.helper.ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(com.app.fivepl.helper.Constant.SUCCESS)) {
                        val `object` = JSONObject(response)
                        val jsonArray: JSONArray = `object`.getJSONArray(com.app.fivepl.helper.Constant.DATA)
                        val g = Gson()
                        val myteam: java.util.ArrayList<MyTeam> =
                            java.util.ArrayList<MyTeam>()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject1 = jsonArray.getJSONObject(i)
                            if (jsonObject1 != null) {
                                val group: MyTeam = g.fromJson(jsonObject1.toString(), MyTeam::class.java)
                                myteam.add(group)
                            } else {
                                break
                            }
                        }
                        val count = jsonArray.length()
                        binding.tvMember.text = "Team - " + count.toString()
                   //  Toast.makeText(getActivity(), "1" + jsonObject.getString(Constant.MESSAGE).toString(), Toast.LENGTH_SHORT).show()
                        //important
                        val adapter = MyTeamAdapter(activity, myteam)
                        binding.rvMyTeamB.visibility = View.VISIBLE
                        binding.rvMyTeamB.adapter = adapter
                        binding.animationView.visibility = View.GONE


                    } else {
                      //  DialogUtils.showCustomDialog(activity, ""+jsonObject.getString(Constant.MESSAGE))
                        binding.animationView.visibility = View.VISIBLE
                        binding.rvMyTeamB.visibility = View.GONE
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(getActivity(), "1"+e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }, activity, com.app.fivepl.helper.Constant.TEAM_LIST, params, true)




    }


}