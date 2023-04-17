package com.example.androiduistudy.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.androiduistudy.util.CustomLog

open class BaseFragment: Fragment() {
    companion object{
        const val TAG = "BaseFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CustomLog.d(TAG,"onCreate 当前类:${javaClass.simpleName}")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CustomLog.d(TAG,"onViewCreated 当前类:${javaClass.simpleName}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        CustomLog.d(TAG,"onCreateView 当前类:${javaClass.simpleName}")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        CustomLog.d(TAG,"onDestroy 当前类:${javaClass.simpleName}")
    }
}