package com.toys.testimmersionbar.activity

import android.annotation.TargetApi
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity


class SplashActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_splash2)

        if (Build.VERSION.SDK_INT >= 21) {
            val decorView: View = window.decorView
            val option: Int = (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            //                    | View.SYSTEM_UI_FLAG_FULLSCREEN;//隐藏状态栏
            decorView.setSystemUiVisibility(option)
            window.navigationBarColor = Color.TRANSPARENT
            window.statusBarColor = Color.TRANSPARENT
        }
        val actionBar: ActionBar? = supportActionBar
        actionBar?.hide()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val layoutParams = window.attributes
            layoutParams.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = layoutParams
        }

        getCutParams()

        Handler().postDelayed({
            startActivity(Intent(this,
                MainActivity::class.java))
        },3000)
    }

    //获取刘海高度
    fun getDisplayCutoutHeight(height: Int){
        Log.d("TAG", "getDisplayCutoutHeight height =$height")
    }

    @TargetApi(28)
    fun getCutParams() {
        val decorView = window.decorView
        decorView.post(Runnable {
            val displayCutout =
                decorView.rootWindowInsets.displayCutout
            if (displayCutout == null) {
                Log.e("TAG", "不是刘海屏")
                return@Runnable
            }
            Log.e("TAG", "安全区域距离屏幕左边的距离 SafeInsetLeft:" + displayCutout.safeInsetLeft)
            Log.e("TAG", "安全区域距离屏幕右部的距离 SafeInsetRight:" + displayCutout.safeInsetRight)
            Log.e("TAG", "安全区域距离屏幕顶部的距离 SafeInsetTop:" + displayCutout.safeInsetTop)
            getDisplayCutoutHeight(displayCutout.safeInsetTop)
            Log.e("TAG", "安全区域距离屏幕底部的距离 SafeInsetBottom:" + displayCutout.safeInsetBottom)
            //                decorView.setPadding(displayCutout.getSafeInsetLeft(), displayCutout.getSafeInsetTop(), displayCutout.getSafeInsetRight(),
            //                        displayCutout.getSafeInsetBottom());
            val rects: List<Rect> = displayCutout.boundingRects
            if (rects == null || rects.size == 0) {
                Log.e("TAG", "不是刘海屏")
            } else {
                Log.e("TAG", "刘海屏数量:" + rects.size)
                for (rect in rects) {
                    Log.e("TAG", "刘海屏区域：$rect")
                }
            }
        })
    }
}