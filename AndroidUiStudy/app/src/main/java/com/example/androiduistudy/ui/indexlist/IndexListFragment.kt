package com.example.androiduistudy.ui.indexlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androiduistudy.R
import com.example.androiduistudy.base.BaseBindingViewFragment
import com.example.androiduistudy.databinding.FragmentIndexListBinding
import com.example.androiduistudy.util.CommonData


class IndexListFragment : BaseBindingViewFragment<FragmentIndexListBinding>(R.layout.fragment_index_list) {
    companion object{
        const val TAG = "IndexListFragment"
    }
    override fun initBinding(layoutInflater: LayoutInflater): FragmentIndexListBinding? {
        return FragmentIndexListBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        val dataList = CommonData.getStrAToZ()
        val showList = mutableListOf<Boolean>()
        if (showList.size==0){
            for (i in dataList.indices){
                showList.add(true)
            }
        }
        val adapter = IndexAdapter(requireContext(), dataList,showList)
        binding.rvIndexList.adapter = adapter

        binding.viewSideIndex.setOnPositionListener {position->
            //接收数据
            if (position >=0){
                val layoutManager = binding.rvIndexList.layoutManager as LinearLayoutManager
                layoutManager.scrollToPositionWithOffset(position, 0)
                binding.rlIndexMsg.visibility = View.VISIBLE
                binding.tvIndexMsg.text = dataList[position]
            }else {
                binding.rlIndexMsg.visibility = View.GONE
            }
        }
    }
}