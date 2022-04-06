package com.mankart.myservice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mankart.myservice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStartService.setOnClickListener {
            startService(Intent(this@MainActivity, MyService::class.java))
        }

        binding.btnStartBoundService.setOnClickListener {  }

        binding.btnStopBoundService.setOnClickListener {  }

    }
}