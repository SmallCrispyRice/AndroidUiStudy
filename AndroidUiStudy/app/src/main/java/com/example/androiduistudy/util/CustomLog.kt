package com.example.androiduistudy.util

import android.util.Log

object CustomLog {
    private var isDebug = false
    private var defaultTag = "测试"

    fun init(isDebug: Boolean){
        CustomLog.isDebug = isDebug
    }

    fun d(tag:String,message:String){
        if (isDebug){
            Log.d(tag,message)
        }
    }

    fun d(message:String){
        if (isDebug){
            Log.d(defaultTag,message)
        }
    }

    fun i(tag:String,message:String){
        if (isDebug){
            Log.i(tag,message)
        }
    }

    fun e(tag:String,message:String){
        if (isDebug){
            Log.e(tag,message)
        }
    }

    fun v(tag:String,message:String){
        if (isDebug){
            Log.v(tag,message)
        }
    }
}