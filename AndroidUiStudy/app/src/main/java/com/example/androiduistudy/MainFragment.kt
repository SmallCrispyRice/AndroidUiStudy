package com.example.androiduistudy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.androiduistudy.base.BaseFragment
import com.example.androiduistudy.bean.NavItem
import com.example.androiduistudy.databinding.FragmentMainBinding


class MainFragment : BaseFragment() {
    companion object{
        const val TAG = "MainFragment"
    }
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        val navItems = mutableListOf<NavItem>()
        navItems.add(NavItem("索引列表",R.id.action_mainFragment_to_indexListFragment))
        navItems.add(NavItem("小说书架",R.id.action_mainFragment_to_bookshelfFragment))
        navItems.add(NavItem("写字板",R.id.action_mainFragment_to_tabletFragment))
        val adapter = ShowAdapter(navItems,findNavController())
        binding.rvMainShow.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }
}