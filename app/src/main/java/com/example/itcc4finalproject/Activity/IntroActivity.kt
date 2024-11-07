package com.example.itcc4finalproject.Activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.example.itcc4finalproject.databinding.ActivityIntroBinding

class IntroActivity : BaseActivity() {
    private lateinit var binding: ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout first
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Enable edge-to-edge layout after setting the content view
        enableEdgeToEdge()

        binding.apply {
            startBtn.setOnClickListener {
                startActivity(Intent(this@IntroActivity, MainActivity::class.java))
            }
        }
    }
}
