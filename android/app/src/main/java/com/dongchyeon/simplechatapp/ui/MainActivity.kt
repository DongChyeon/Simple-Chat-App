package com.dongchyeon.simplechatapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.dongchyeon.simplechatapp.SimpleChatApp.Companion.roomName
import com.dongchyeon.simplechatapp.SimpleChatApp.Companion.userName
import com.dongchyeon.simplechatapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        init()
    }

    private fun init() {
        // 다크 모드 비활성화
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding.enterBtn.setOnClickListener {
            val intent = Intent(applicationContext, ChatActivity::class.java)
            userName = binding.usernameEdit.text.toString()
            roomName = binding.roomEdit.text.toString()
            startActivity(intent)
        }
    }
}