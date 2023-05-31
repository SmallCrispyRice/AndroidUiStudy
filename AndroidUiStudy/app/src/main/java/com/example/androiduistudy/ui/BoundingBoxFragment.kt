package com.example.androiduistudy.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.example.androiduistudy.R
import com.example.androiduistudy.base.BaseBindingViewFragment
import com.example.androiduistudy.databinding.FragmentBoundingBoxBinding

class BoundingBoxFragment : BaseBindingViewFragment<FragmentBoundingBoxBinding>(R.layout.fragment_bounding_box) {
    override fun initBinding(layoutInflater: LayoutInflater): FragmentBoundingBoxBinding? {
        return FragmentBoundingBoxBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
    }


    private fun initView() {

        binding.btnGetCoordinates.setOnClickListener {
            val coordinates = binding.imgCat1.coordinates
            binding.tvCoordinates.text ="左上角点坐标: (${coordinates[0]},${coordinates[1]})\n" +
                    "右上角点坐标: (${coordinates[2]},${coordinates[3]})"
        }
        binding.btnClearBox.setOnClickListener {
            binding.imgCat1.clearBox()
        }

        binding.spDecorationType.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, postion: Int, p3: Long) {
                binding.imgCat1.setDecorationType(postion)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }
}