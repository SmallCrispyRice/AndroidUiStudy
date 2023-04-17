package com.example.androiduistudy.ui.indexlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androiduistudy.R
import com.example.androiduistudy.util.CommonData

class IndexAdapter(
    private val mContext: Context,
    private val datas: Array<String>,
    private val showList: MutableList<Boolean>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object{
        const val TAG = "IndexAdapter"
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(mContext)
        return NormalHolder(inflater.inflate(R.layout.item_index,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //初始化view
        (holder as NormalHolder).tvIndexName.text = datas[position]
        (holder as NormalHolder).rvIndexMsg.apply {
            val indexMsgS = CommonData.getStrList(30)
            val indexMsgAdapter = RecyclerAdapter(mContext, indexMsgS)
            adapter = indexMsgAdapter
        }
        if (showList[position]){
            (holder as NormalHolder).rvIndexMsg.visibility = View.VISIBLE
            (holder as NormalHolder).imgIndexListShow.setImageResource(R.mipmap.msg_show)
        }else{
            (holder as NormalHolder).rvIndexMsg.visibility = View.GONE
            (holder as NormalHolder).imgIndexListShow.setImageResource(R.mipmap.right_2)
        }
        //view点击事件处理
        (holder as NormalHolder).imgIndexListShow.setOnClickListener {
            if (showList[position]){
                (holder as NormalHolder).rvIndexMsg.visibility = View.GONE
                (holder as NormalHolder).imgIndexListShow.setImageResource(R.mipmap.right_2)
            }else{
                (holder as NormalHolder).rvIndexMsg.visibility = View.VISIBLE
                (holder as NormalHolder).imgIndexListShow.setImageResource(R.mipmap.msg_show)
            }
            showList[position] = !showList[position]
        }
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    inner class NormalHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvIndexName: TextView
        val rvIndexMsg: RecyclerView
        val imgIndexListShow: ImageView
        init {
            tvIndexName = itemView.findViewById(R.id.tv_index_name)
            rvIndexMsg = itemView.findViewById(R.id.rv_index_msg)
            imgIndexListShow = itemView.findViewById(R.id.img_index_list_show)
        }
    }
}