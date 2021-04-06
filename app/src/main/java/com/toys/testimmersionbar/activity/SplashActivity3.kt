package com.toys.testimmersionbar.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import com.toys.testimmersionbar.utils.StatusBarCompat

class SplashActivity3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_splash3)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)

        StatusBarCompat.setTranslucentDiff(this)
        StatusBarCompat.setNavigationBarTransparent(this)

        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
        }, 3000)
    }
}