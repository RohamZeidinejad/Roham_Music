package com.example.rohammusic

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rohammusic.databinding.ActivityMainBinding
import com.google.android.material.slider.Slider
import java.util.Locale
import java.util.Timer
import java.util.TimerTask

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var timer: Timer
    var isPlaying = false
    var isMute = false
    var isUserChanging = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mediaPlayer = MediaPlayer.create(this, R.raw.dark_knight)

        playMusic()

        backwardForward()

        muteUnmute()

        slider()

    }

    private fun playMusic() {

        binding.icPlayMusic.setOnClickListener {

            if (!isPlaying) {

                mediaPlayer.start()
                binding.icPlayMusic.setImageResource(R.drawable.ic_pause)
                isPlaying = true

            } else {

                mediaPlayer.pause()
                binding.icPlayMusic.setImageResource(R.drawable.ic_play)
                isPlaying = false
            }
        }

    }

    private fun backwardForward() {

        binding.icBackward.setOnClickListener {

            val position = mediaPlayer.currentPosition - 10000
            mediaPlayer.seekTo(position)

        }

        binding.icForward.setOnClickListener {

            val position = mediaPlayer.currentPosition + 10000
            mediaPlayer.seekTo(position)

        }

    }

    private fun muteUnmute() {

        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager

        binding.icMute.setOnClickListener {

            if (!isMute) {

                audioManager.adjustVolume(AudioManager.ADJUST_MUTE, 0)
                binding.icMute.setImageResource(R.drawable.ic_mute)
                isMute = true

            } else {
                audioManager.adjustVolume(AudioManager.ADJUST_UNMUTE, 0)
                binding.icMute.setImageResource(R.drawable.ic_unmute)
                isMute = false
            }

        }


    }

    private fun slider() {

        binding.slider.valueTo = mediaPlayer.duration.toFloat()

        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    if (!isUserChanging) {
                        binding.slider.value = mediaPlayer.currentPosition.toFloat()
                    }
                }
            }

        }, 0, 1000)

        binding.end.text = secondsToString(mediaPlayer.duration.toLong())

        binding.slider.addOnChangeListener { slider, value, fromUser ->

            isUserChanging = fromUser
            binding.start.text = secondsToString(value.toLong())

        }

        binding.slider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {


            override fun onStartTrackingTouch(slider: Slider) {

            }

            override fun onStopTrackingTouch(slider: Slider) {

                mediaPlayer.seekTo(slider.value.toInt())
                isUserChanging = false

            }

        })

    }

    private fun secondsToString(duration: Long): String {

        val second = duration / 1000 % 60
        val minute = duration / (1000 * 60) % 60

        return java.lang.String.format(Locale.US, "%02d:%02d", minute, second)

    }

}