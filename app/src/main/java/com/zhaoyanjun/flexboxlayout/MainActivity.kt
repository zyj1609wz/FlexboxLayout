package com.zhaoyanjun.flexboxlayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("ttt--" , "" + System.currentTimeMillis() + "  " + (System.currentTimeMillis()/1000))
    }
}