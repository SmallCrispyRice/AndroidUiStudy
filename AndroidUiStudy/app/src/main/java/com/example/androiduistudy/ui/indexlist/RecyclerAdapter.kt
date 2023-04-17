package com.example.androiduistudy.ui.indexlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androiduistudy.R
import com.example.androiduistudy.util.CustomLog
import com.example.androiduistudy.util.ToastUtil

class RecyclerAdapter(private val mContext: Context, private val datas:List<String>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mCreateHolder = 0
    companion object{
        const val TAG = "RecyclerAdapter"
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mCreateHolder++
        CustomLog.d(TAG,"onCreateViewHolder num:${mCreateHolder}")
        val inflater = LayoutInflater.from(mContext)
        return NormalHolder(inflater.inflate(R.layout.item_rv_use,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        CustomLog.d(TAG,"onBindViewHolder")
        (holder as NormalHolder).tvMsg.text = datas[position]
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    inner class NormalHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMsg: TextView
        init {
            tvMsg = itemView.findViewById(R.id.tv_rv_use_msg)
            tvMsg.setOnClickListener {
                ToastUtil.show(tvMsg.text.toString())
            }
        }
    }
}