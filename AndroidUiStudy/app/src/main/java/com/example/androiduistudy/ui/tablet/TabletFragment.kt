package com.example.androiduistudy.ui.tablet

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.androiduistudy.R
import com.example.androiduistudy.base.BaseBindingViewFragment
import com.example.androiduistudy.databinding.FragmentTabletBinding
import com.example.androiduistudy.util.CustomLog
import java.io.File

class TabletFragment : BaseBindingViewFragment<FragmentTabletBinding>(R.layout.fragment_tablet) {
    companion object{
        const val TAG = "TabletFragment"
    }
    override fun initBinding(layoutInflater: LayoutInflater): FragmentTabletBinding? {
        return FragmentTabletBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.btnClearTablet.setOnClickListener {
            binding.viewTablet.clearTablet()
        }

        binding.btnSaveTablet.setOnClickListener {
            val file = File(requireContext().filesDir,"savePath.png")
            val filePath = file.absolutePath
            CustomLog.d(TAG,"filePath:${filePath}")
            binding.viewTablet.saveToFile(file.absolutePath)
            binding.imgSaveTablet
            if (file.exists()){
                val bitmap = BitmapFactory.decodeFile(filePath)
                binding.imgSaveTablet.setImageBitmap(bitmap)
            }
        }

    }
}