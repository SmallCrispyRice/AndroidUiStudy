package com.example.androiduistudy


import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.androiduistudy.base.BaseActivity
import com.example.androiduistudy.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {
    companion object {
        const val TAG = "MainActivity"
    }
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }
}