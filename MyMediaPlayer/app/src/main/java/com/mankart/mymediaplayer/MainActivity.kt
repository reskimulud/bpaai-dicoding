package com.mankart.mymediaplayer

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mankart.mymediaplayer.databinding.ActivityMainBinding
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var mMediaPlayer: MediaPlayer? = null
    private var isReady: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPlay.setOnClickListener {
            if (!isReady) {
                mMediaPlayer?.prepareAsync()
                binding.btnPlay.text = getString(R.string.btn_text_pause)
            } else {
                if (mMediaPlayer?.isPlaying as Boolean) {
                    binding.btnPlay.text = getString(R.string.btn_text_play)
                    mMediaPlayer?.pause()
                } else {
                    binding.btnPlay.text = getString(R.string.btn_text_pause)
                    mMediaPlayer?.start()
                }
            }
        }

        binding.btnStop.setOnClickListener {
            if (mMediaPlayer?.isPlaying as Boolean) {
                mMediaPlayer?.stop()
                binding.btnPlay.text = getString(R.string.btn_text_play)
                isReady = false
            }
        }

        init()
    }

    private fun init() {
        mMediaPlayer = MediaPlayer()
        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        mMediaPlayer?.setAudioAttributes(attributes)
        val afd = applicationContext.resources.openRawResourceFd(R.raw.guitar_solo)
        try {
            mMediaPlayer?.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        mMediaPlayer?.setOnPreparedListener {
            isReady = true
            mMediaPlayer?.start()
        }

        mMediaPlayer?.setOnCompletionListener {
            binding.btnPlay.text = getString(R.string.btn_text_play)
            isReady = false
        }

        mMediaPlayer?.setOnErrorListener { _, _, _ -> false }
    }
}