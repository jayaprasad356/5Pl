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
import com.app.fivepl.R
import com.app.fivepl.databinding.FragmentYoutubeBinding
import com.app.fivepl.helper.Session
import com.google.android.material.card.MaterialCardView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


class YoutubeFragment : Fragment() {

    private var binding: FragmentYoutubeBinding? = null
    private var activity: Activity? = null
    private var session: Session? = null

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


        binding!!.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val videoId = "S0Q4gqBUs7c"
                youTubePlayer.loadVideo(videoId, 0f)
                youTubePlayer.play()
            }
        })



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

}