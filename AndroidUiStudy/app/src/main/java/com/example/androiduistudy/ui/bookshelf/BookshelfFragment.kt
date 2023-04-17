package com.example.androiduistudy.ui.bookshelf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.androiduistudy.R
import com.example.androiduistudy.base.BaseBindingViewFragment
import com.example.androiduistudy.databinding.FragmentBookshelfBinding


class BookshelfFragment : BaseBindingViewFragment<FragmentBookshelfBinding>(R.layout.fragment_bookshelf)  {
    override fun initBinding(layoutInflater: LayoutInflater): FragmentBookshelfBinding? {
        return FragmentBookshelfBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        val books = mutableListOf<Book>()
        for (i in 0..48){
            books.add(Book("书名${i}",false))
        }
        val adapter = BookshelfAdapter(books,requireContext())
        binding.rlBookshelf.adapter = adapter
        //装饰的部分在拖动交换时有问题，可注释掉
//        val bookShelfItemDecoration = BookShelfItemDecoration(requireContext())
//        binding.rlBookshelf.addItemDecoration(bookShelfItemDecoration)
        val itemTouchHelper = ItemTouchHelper(BookShelfItemTouchHelper(books,adapter))
        itemTouchHelper.attachToRecyclerView(binding.rlBookshelf)
        binding.btnDeleteBooks.setOnClickListener {
            adapter.deleted()
        }
    }

}