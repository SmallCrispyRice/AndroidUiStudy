package com.example.androiduistudy.base

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity: AppCompatActivity() {
    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        initWindow()
        return super.onCreateView(name, context, attrs)
    }

    //状态栏透明,且组件占据了状态栏
    private fun initWindow() {
        window.statusBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}