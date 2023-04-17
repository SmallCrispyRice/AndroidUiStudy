package com.example.androiduistudy.util

import android.widget.Toast
import com.example.androiduistudy.CustomApplication

object ToastUtil {
    fun show(msg:String) {
        Toast.makeText(CustomApplication.context,msg,Toast.LENGTH_SHORT).show()
    }
}