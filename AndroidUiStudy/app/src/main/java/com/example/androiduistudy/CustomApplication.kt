package com.example.androiduistudy

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.example.androiduistudy.util.CustomLog

class CustomApplication: Application() {
    companion object{
        @SuppressLint("CustomContext")
        lateinit var context: Context //可以在任意地方使用context CustomApplication.context
    }
    override fun onCreate() {
        super.onCreate()
        CustomLog.init(true)
        context = applicationContext
    }
}