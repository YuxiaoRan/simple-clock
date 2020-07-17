package com.example.simple_clock

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        graphical.setOnClickListener {
            val i = Intent(this, GraphicalClockActivity::class.java)
            startActivity(i)
        }

        numerical.setOnClickListener {
            val j = Intent(this, NumericalClockActivity::class.java)
            startActivity(j)
        }
    }

}