package com.example.androiduistudy.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.example.androiduistudy.R
import com.example.androiduistudy.base.BaseBindingViewFragment
import com.example.androiduistudy.databinding.FragmentRockerBinding


class RockerFragment : BaseBindingViewFragment<FragmentRockerBinding>(R.layout.fragment_rocker) {
    companion object{
        const val TAG = "RockerFragment"
    }
    override fun initBinding(layoutInflater: LayoutInflater): FragmentRockerBinding? {
        return FragmentRockerBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.viewRocker.getDate { _, pX, pY ->
            binding.viewFly.move(pX, pY)
        }
    }
}