package com.app.fivepl.fragment

import android.app.Activity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.app.fivepl.R
import com.app.fivepl.databinding.FragmentYoutubeBinding
import com.app.fivepl.helper.Session
import com.google.android.material.card.MaterialCardView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.customui.DefaultPlayerUiController
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


class YoutubeFragment : Fragment() {

    private var binding: FragmentYoutubeBinding? = null
    private var activity: Activity? = null
    private var session: Session? = null
    private var canPlayVideo: Boolean = true

    var i = 0
    private var countDownTimer: CountDownTimer? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentYoutubeBinding.inflate(inflater, container, false)
        activity = getActivity() as Activity
        session = Session(activity)

        lifecycle.addObserver(binding!!.youtubePlayerView)

//        requireActivity().supportActionBar?.hide()
        // using pre-made custom ui
        // using pre-made custom ui


        binding?.youtubePlayerView?.enableAutomaticInitialization = false
        binding?.youtubePlayerView?.initialize(
            youTubePlayerListener = object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    val defaultPlayerUiController =
                        DefaultPlayerUiController(binding?.youtubePlayerView!!, youTubePlayer)
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
                        val videoId = "S0Q4gqBUs7c"
                        youTubePlayer.loadVideo(videoId, 0f)
                        if (canPlayVideo) youTubePlayer.play() else youTubePlayer.pause()
                    }
                }

                override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                    super.onCurrentSecond(youTubePlayer, second)
                    if (canPlayVideo) youTubePlayer.play() else youTubePlayer.pause()
                    binding?.youtubePlayerView?.visibility = if (canPlayVideo) View.VISIBLE else View.GONE
                }
            },
            playerOptions = IFramePlayerOptions.Builder()
                .controls(0).build()
        )



        Handler().postDelayed(object : Runnable {
            override fun run() {
                // set the limitations for the numeric
                // text under the progress bar
                if (i <= 100) {


                    // Show the timer text view
                    binding!!.timerTextView!!.setVisibility(View.VISIBLE)

                    binding!!.progressBar.progress = i
                    i++
                    Handler().postDelayed(this, 100)
                } else {
                    Handler().removeCallbacks(this)
                }
            }
        }, 100)


        // Show the timer text view

        // Show the timer text view
        binding!!.timerTextView!!.setVisibility(View.VISIBLE)

        // Start the countdown timer

        // Start the countdown timer
        countDownTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Update the timerTextView with the remaining seconds
                binding!!.timerTextView!!.setText((millisUntilFinished / 1000).toString())
            }

            override fun onFinish() {
                // Do something when the timer finishes
                // For example, hide the timerTextView and start the Quiz format
                binding!!.timerTextView!!.setVisibility(View.GONE)
                canPlayVideo = false
                startQuiz()
            }
        }.start()


        return binding!!.root
    }

    private fun startQuiz() {
        // Start your Quiz format here
        binding!!.cardView.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        if (countDownTimer != null) {
            countDownTimer!!.cancel()
        }
    }

    override fun onPause() {
        super.onPause()
        if (countDownTimer != null) {
            countDownTimer!!.cancel()
        }
    }

}