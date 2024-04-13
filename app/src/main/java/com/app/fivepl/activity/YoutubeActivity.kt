package com.app.fivepl.activity

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.fivepl.R
import com.google.android.material.card.MaterialCardView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.customui.DefaultPlayerUiController
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


class YoutubeActivity : AppCompatActivity(){

    private var timerTextView: TextView? = null
    private var countDownTimer: CountDownTimer? = null
    private var cardView: MaterialCardView? = null
    private var canPlayVideo: Boolean = true
    lateinit var progressBar: ProgressBar
    var i = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube)

        val youTubePlayerView = findViewById<YouTubePlayerView>(R.id.youtube_player_view)
        lifecycle.addObserver(youTubePlayerView)

        supportActionBar?.hide()
        // using pre-made custom ui
        // using pre-made custom ui

        youTubePlayerView.initialize(
            youTubePlayerListener = object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    val defaultPlayerUiController =
                        DefaultPlayerUiController(youTubePlayerView, youTubePlayer)
                    with(defaultPlayerUiController) {
                        showYouTubeButton(false)
                        showDuration(false)
                        showCurrentTime(false)
                        showUi(false)
                        showSeekBar(false)
                        showPlayPauseButton(false)
                        youTubePlayerView.setCustomPlayerUi(rootView)
                        val videoId = "S0Q4gqBUs7c"
                        youTubePlayer.loadVideo(videoId, 0f)
                        if (canPlayVideo) youTubePlayer.play() else youTubePlayer.pause()
                    }
                }

                override fun onStateChange(
                    youTubePlayer: YouTubePlayer,
                    state: PlayerConstants.PlayerState
                ) {
                    when (state) {
                        PlayerConstants.PlayerState.PLAYING -> {
                            if(canPlayVideo) youTubePlayer.play() else youTubePlayer.pause()
                        }

                        else -> {

                        }
                    }
                }

                override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                    if (canPlayVideo) youTubePlayer.play() else youTubePlayer.pause()
                }
            },
            playerOptions = IFramePlayerOptions.Builder()
                .controls(0).build()
        )




        progressBar = findViewById(R.id.progress_bar)


        Handler().postDelayed(object : Runnable {
            override fun run() {
                // set the limitations for the numeric
                // text under the progress bar
                if (i <= 100) {
                    timerTextView = findViewById<TextView>(R.id.timerTextView)

                    // Show the timer text view
                    timerTextView!!.setVisibility(View.VISIBLE)

                    progressBar.progress = i
                    i++
                    Handler().postDelayed(this, 100)
                } else {
                    Handler().removeCallbacks(this)
                }
            }
        }, 100)


        timerTextView = findViewById<TextView>(R.id.timerTextView)


        // Show the timer text view

        // Show the timer text view
        timerTextView!!.setVisibility(View.VISIBLE)


        // Start the countdown timer
        countDownTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Update the timerTextView with the remaining seconds
                timerTextView!!.setText((millisUntilFinished / 1000).toString())
            }

            override fun onFinish() {
                // Do something when the timer finishes
                // For example, hide the timerTextView and start the Quiz format
                timerTextView!!.setVisibility(View.GONE)
                canPlayVideo = false
                Toast.makeText(this@YoutubeActivity,"callback : $canPlayVideo",Toast.LENGTH_SHORT).show()
//                startQuiz()
            }
        }.start()


    }


    private fun startQuiz() {
        // Start your Quiz format here
        cardView = findViewById<MaterialCardView>(R.id.card_view)
        cardView!!.visibility = View.VISIBLE

    }

    override fun onDestroy() {
        super.onDestroy()
        if (countDownTimer != null) {
            countDownTimer!!.cancel()
        }
    }

}
