package com.toys.testimmersionbar.utils;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;


/**
 * <b>description:</b> 状态栏工具  <br>
 * <b>create:</b>  2016-8-5 下午3:58:19
 *
 * @author haitao
 * @version 1.0
 */
@SuppressLint("NewApi")
public class StatusBarCompat {

    /**
     * 只有android:fitsSystemWindows false 才能透过
     * 穿透到状态栏
     *
     * @param activity Activity
     */
    public static void setTranslucentDiff(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);  //需要设置这个才能设置状态栏颜色
            appendSystemUiVisibility(activity.getWindow().getDecorView(), View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明状态栏
            appendSystemUiVisibility(activity.getWindow().getDecorView(), View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }


    /**
     * 穿透虚拟按键 并且透明
     *
     * @param activity
     */
    public static void setNavigationBarTransparent(Activity activity) {
        setNavigationBarColorTranslucent(activity, Color.TRANSPARENT);
    }

    /**
     * @param activity
     * @param color
     */
    public static void setNavigationBarColorTranslucent(Activity activity, int color) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Window window = activity.getWindow();
            //Android 11 以上 将视图延伸至导航栏区域
            window.setDecorFitsSystemWindows(false);
            window.setNavigationBarColor(color);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Android 5.0 以上 全透明
            Window window = activity.getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // 虚拟导航键
            window.setNavigationBarColor(color);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Android 4.4 以上 半透明
            if (color == Color.TRANSPARENT) {
                Window window = activity.getWindow();
                // 虚拟导航键
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }
    }

    /**
     * 设置状态栏颜色
     *
     * @param activity
     * @param statusColor
     */


    public static void compat(Activity activity, int statusColor) {
        addStatusBarView(activity, statusColor, false);
    }

    public static void compatDrawable(Activity activity, int drawable) {
        addStatusBarView(activity, drawable, true);
    }


    private static void appendSystemUiVisibility(View view, int visibility) {
        int uiOption = view.getSystemUiVisibility();
        uiOption = visibility | uiOption;
        view.setSystemUiVisibility(uiOption);
    }

    private static void addStatusBarView(Activity activity, int statusColor, boolean drawable) {
        //View 的fitsSystemWindows 设为true，那么该View的padding属性将由系统设置，用户在布局文件中设置的padding会被忽略。
        //系统会为该View设置一个paddingTop，值为statusbar的高度。fitsSystemWindows默认为false。
        //只有将statusbar设为透明，或者界面设为全屏显示（设置View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN flag)时，fitsSystemWindows才会起作用。

        // 沉浸的方式
        // 1、直接调用系统api 5.0以上才可以 非全屏（fitsSystemWindows true/false 都可以）
        // 2、全屏穿透到状态栏，然后添加一个状态栏高度的自定义view 4.4以上有效（fitsSystemWindows false 不然会多一个padding）
        // adjustResize 生效的条件非全屏 或者 （全屏+fitsSystemWindows = true）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);  //需要设置这个才能设置状态栏颜色
            if (drawable) {
                activity.getWindow().setStatusBarColor(0);
            } else {
                activity.getWindow().setStatusBarColor(statusColor);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏4.4必须设置 不然不能自定义状态栏颜色
            //6.0设置以后状态栏 会出现半透明 不能全白
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && drawable) {
            ViewGroup contentView = activity.findViewById(android.R.id.content);
            appendSystemUiVisibility(activity.getWindow().getDecorView(),View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            contentView.setPadding(0, 0, 0, 0);
            ViewGroup linearLayout = (ViewGroup) contentView.getParent();
            if (linearLayout.getChildCount() > 0) {
                View statusBarView = linearLayout.getChildAt(0);
                if (Color.TRANSPARENT == statusColor) {
                    if (statusBarView != null && "customStatusBar".equals(statusBarView.getTag())) {
                        linearLayout.removeView(statusBarView);
                    }
                } else {
                    if (statusBarView == null || !"customStatusBar".equals(statusBarView.getTag())) {
                        statusBarView = new View(activity);
                        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
                        statusBarView.setTag("customStatusBar");
                        statusBarView.setLayoutParams(lp);
                        linearLayout.addView(statusBarView, 0);
                    }
                    if (drawable) {
                        statusBarView.setBackgroundResource(statusColor);
                    } else {
                        statusBarView.setBackgroundColor(statusColor);
                    }
                }
            }
        }

    }


    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    public static void statusBarLightMode(Activity activity) {
        compat(activity, Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? Color.WHITE : Color.parseColor("#BDBDBD"));
        statusBarMode(activity, true);
    }

    public static void statusBarMode(Activity activity, boolean light) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            appendSystemUiVisibility(activity.getWindow().getDecorView(), (light ? View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR : View.SYSTEM_UI_FLAG_VISIBLE));
        }
    }


    public static void hideSystemUI(Window window) {
        //这样写 android 11 调节亮度会崩溃，暂时无法解决
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
//            window.getAttributes().setFitInsetsTypes(WindowInsets.Type.navigationBars());
//            window.setDecorFitsSystemWindows(false);
//            final WindowInsetsController insetsController = window.getInsetsController();
//            insetsController.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
//            insetsController.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
//
//        } else {
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    // Hide the nav bar and status bar
//                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_FULLSCREEN);
//        }
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }


}

