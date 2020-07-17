package com.example.simple_clock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_graphical_clock.*

class GraphicalClockActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graphical_clock)

        analogClock.start()
    }

    override fun onDestroy() {
        super.onDestroy()

        analogClock.stop()
    }

}