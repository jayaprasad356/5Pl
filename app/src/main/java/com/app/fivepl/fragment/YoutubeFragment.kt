package com.app.fivepl.fragment

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.app.fivepl.databinding.FragmentYoutubeBinding
import com.app.fivepl.helper.Constant
import com.app.fivepl.helper.Session
import com.app.fivepl.utils.DialogUtils
import com.pierfrancescosoffritti.androidyoutubeplayer.core.customui.DefaultPlayerUiController
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class YoutubeFragment : Fragment() {

    private var binding: FragmentYoutubeBinding? = null
    private var activity: Activity? = null
    private var session: Session? = null
    private var currentSurveyId: Int = 1
    private val surveyId = 1

    var i = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentYoutubeBinding.inflate(inflater, container, false)
        activity = getActivity() as Activity
        session = Session(activity)
        getVideoConfigurationFromVolley(binding)
        getSurveyConfigurationFromVolley(surveyId)
        lifecycle.addObserver(binding!!.youtubePlayerView)
        binding?.youtubePlayerView?.enableAutomaticInitialization = false
        binding?.submitButton?.setOnClickListener {
            checkRadioButtonSelection()
        }


        return binding!!.root
    }

    /**
     * Api call to fetch the survey data.
     */
    private fun getSurveyConfigurationFromVolley(surveyId: Int) {
        val params: MutableMap<String, String> = HashMap()
        params[Constant.USER_ID] = session!!.getData(Constant.USER_ID)
        params[Constant.SURVEY_ID] = surveyId.toString()
        params[Constant.VIDEO_ID] = "1"
        com.app.fivepl.helper.ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(com.app.fivepl.helper.Constant.SUCCESS)) {
                        val `object` = JSONObject(response)
                        val jsonArray: JSONArray =
                            `object`.getJSONArray(com.app.fivepl.helper.Constant.DATA)
                        if (jsonArray.length() > 0) {
                            val jsonObject1 =
                                jsonArray.getJSONObject(0) // Assuming there's only one question for now
                            val question = jsonObject1.getString("question")
                            val option1 = jsonObject1.getString("option_1")
                            val option2 = jsonObject1.getString("option_2")
                            val option3 = jsonObject1.getString("option_3")
                            val correct_option = jsonObject1.getString("correct_option")


                            val id = jsonObject1.getString("id")
                            binding!!.tvQuestionno.text = "Question " + id

                            binding?.textView?.text = question

                            // Set options in radio buttons
                            binding?.radioButton1?.text = option1
                            binding?.radioButton2?.text = option2
                            binding?.radioButton3?.text = option3

                            currentSurveyId = surveyId
                        }
                    } else {
                        activity?.let {
                            DialogUtils.showCustomDialog(
                                it,
                                "" + jsonObject.getString(Constant.MESSAGE)
                            )
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }, activity, com.app.fivepl.helper.Constant.SURVEY, params, true)
    }

    /**
     * Api call to fetch the youtube data.
     * @param binding ViewBinding of the current fragment.
     */
    private fun getVideoConfigurationFromVolley(binding: FragmentYoutubeBinding?) {
        val params: MutableMap<String, String> = HashMap()
        com.app.fivepl.helper.ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(com.app.fivepl.helper.Constant.SUCCESS)) {
                        val `object` = JSONObject(response)
                        val jsonArray: JSONArray =
                            `object`.getJSONArray(com.app.fivepl.helper.Constant.DATA)
                        if (jsonArray.length() > 0) {
                            val jsonObject1 =
                                jsonArray.getJSONObject(0) // Assuming there's only one question for now
                            val id = jsonObject1.getString("id")
                            val url = jsonObject1.getString("url")
                            val status = jsonObject1.getString("status")
                            val duration = jsonObject1.getString("duration")

                            Log
                                .i("Url", url)
                            binding?.youtubePlayerView?.initialize(
                                youTubePlayerListener = object : AbstractYouTubePlayerListener() {
                                    override fun onReady(youTubePlayer: YouTubePlayer) {
                                        val defaultPlayerUiController =
                                            DefaultPlayerUiController(
                                                binding?.youtubePlayerView!!,
                                                youTubePlayer
                                            )
                                        with(defaultPlayerUiController) {
                                            showYouTubeButton(false)
                                            showDuration(false)
                                            showCurrentTime(false)
                                            showUi(false)
                                            showSeekBar(false)
                                            showPlayPauseButton(false)
                                            showVideoTitle(false)
                                            showMenuButton(false)
                                            binding?.youtubePlayerView!!.setCustomPlayerUi(rootView)
                                            youTubePlayer.loadVideo(
                                                url,
                                                0f
                                            )
                                            youTubePlayer.play()
                                        }
                                    }

                                    override fun onError(
                                        youTubePlayer: YouTubePlayer,
                                        error: PlayerConstants.PlayerError
                                    ) {
                                        Log
                                            .e("YT_ERROR", error.name)
                                    }

//                                    override fun onCurrentSecond(
//                                        youTubePlayer: YouTubePlayer,
//                                        second: Float
//                                    ) {
//                                        Log
//                                            .i("Duration From YT", second.toString())
//                                        val filteredDuration = duration.replace(":", ".").also {
//                                            Log
//                                                .i("Duration From API", it)
//                                        }
//                                        val formattedDuration = String.format("%.2f",filteredDuration.toFloat()).toFloat()
//                                        canPlayVideo = (second >= formattedDuration)
//                                    }

                                    override fun onStateChange(
                                        youTubePlayer: YouTubePlayer,
                                        state: PlayerConstants.PlayerState
                                    ) {
                                        when (state) {
                                            PlayerConstants.PlayerState.ENDED -> {
                                                binding.youtubePlayerView.visibility = View.GONE
                                                binding.cardView.visibility = View.VISIBLE
                                                Log
                                                    .i("YT_PLAYER", "Streaming ended.")

                                            }

                                            else -> {

                                            }
                                        }
                                    }
                                },
                                playerOptions = IFramePlayerOptions.Builder()
                                    .controls(0).build()
                            )
                        }
                    } else {
                        activity?.let {
                            DialogUtils.showCustomDialog(
                                it,
                                "" + jsonObject.getString(Constant.MESSAGE)
                            )
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }, activity, Constant.VIDEO_LIST, params, true)
    }


    private fun checkRadioButtonSelection() {
        // Check if any radio button is selected
        val radioButton1 = binding?.radioButton1
        val radioButton2 = binding?.radioButton2
        val radioButton3 = binding?.radioButton3

        if (radioButton1?.isChecked == false && radioButton2?.isChecked == false && radioButton3?.isChecked == false) {
            // None of the radio buttons is selected, display a toast message
            Toast.makeText(activity, "Please select an option", Toast.LENGTH_SHORT).show()
        } else {

            // uncheck all radio buttons
            binding!!.radioGroup.clearCheck()

            val surveyid = currentSurveyId + 1
            getSurveyConfigurationFromVolley(surveyid!!)


        }
    }

}