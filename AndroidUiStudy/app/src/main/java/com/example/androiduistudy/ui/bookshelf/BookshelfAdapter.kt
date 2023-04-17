package com.example.androiduistudy.ui.bookshelf

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androiduistudy.R
import com.example.androiduistudy.databinding.ItemBookshelfBinding
import com.example.androiduistudy.util.CustomLog
import java.util.*

class BookshelfAdapter(var datas: MutableList<Book>, val mContext: Context) :
    RecyclerView.Adapter<BookshelfAdapter.ViewHolder>() {
    companion object{
        const val TAG = "BookshelfAdapter"
    }
    private var deletDatas = mutableListOf<Book>()
    class ViewHolder(val binding: ItemBookshelfBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        val binding = ItemBookshelfBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            tvBookName.text = datas[position].bookName
            imgBookSelected.setImageResource(if (datas[position].bookSelected) R.mipmap.circle_selected else R.mipmap.circle_unselected)
            imgBookSelected.setOnClickListener {
                datas[position].bookSelected = !datas[position].bookSelected
                imgBookSelected.setImageResource(if (datas[position].bookSelected)R.mipmap.circle_selected else R.mipmap.circle_unselected)
                if (datas[position].bookSelected){
                    deletDatas.add(datas[position])
                }else{
                    deletDatas.remove(datas[position])
                }
            }
            tvBookName.setOnClickListener{
                var bookList = mutableListOf<String>()
                for (data in datas){
                    bookList.add(data.bookName)
                }
                CustomLog.d(TAG,"all list:${bookList}")

                var deleteBookList = mutableListOf<String>()
                for (data in deletDatas){
                    deleteBookList.add(data.bookName)
                }
                CustomLog.d(TAG,"delet list:${deleteBookList}")
            }
        }
    }

    override fun getItemCount(): Int = datas.size

    /** 删除选中的数据 */
    fun deleted(){
        if (deletDatas.size>0){
            for (data in deletDatas){
                datas.remove(data)
            }
            deletDatas.clear()//删除数据后，deletDatas清空防止有影响。
            notifyDataSetChanged()
        }
    }
}