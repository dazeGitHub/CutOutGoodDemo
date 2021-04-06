package com.toys.testimmersionbar.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_splash)

//        requestWindowFeature(Window.FEATURE_NO_TITLE)
//        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setGoogleNotch()

        Handler().postDelayed({
            startActivity(Intent(this,
                MainActivity::class.java))
        },3000)
    }

    /**
     * Android P Google API 使用刘海屏区域
     */
    private fun setGoogleNotch(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val lp : WindowManager.LayoutParams  = window.attributes;
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            window.attributes = lp;
        }
    }

    /**
     * 设置使用小米手机刘海屏区域
     */
    private fun setMIUINotch(){
//        var flag = 0x00000100 or  0x00000200 or  0x00000400
//        try {
//            var method = Window.class?.getMethod("addExtraFlags", Int.class)
//            method.invoke(getWindow(), flag);
//        } catch ( e : Exception) {
//            TLog.i("launch", "addExtraFlags not found.");
//        }
    }
}