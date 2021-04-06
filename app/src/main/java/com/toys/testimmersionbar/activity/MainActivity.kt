package com.toys.testimmersionbar.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gyf.barlibrary.ImmersionBar
import com.toys.testimmersionbar.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ImmersionBar.with(this).statusBarColor(R.color.bg_splash).init()
    }
}