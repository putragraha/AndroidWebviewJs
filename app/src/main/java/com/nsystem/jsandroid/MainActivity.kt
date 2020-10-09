package com.nsystem.jsandroid

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnLocalWeb.setOnClickListener {
            startActivity(Intent(this, LocalWebActivity::class.java))
        }
        btnNetworkWeb.setOnClickListener {
            startActivity(Intent(this, NetworkWebActivity::class.java))
        }
    }
}