package com.example.androiduistudy.ui.bookshelf;

import android.graphics.Canvas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;


import com.example.androiduistudy.R;

import java.util.Collections;
import java.util.List;

public class BookShelfItemTouchHelper extends ItemTouchHelper.Callback {
    private final String TAG = "BookShelfItemTouchHelper";
    private List<Book> books;
    private BookshelfAdapter bookshelfAdapter;

    public BookShelfItemTouchHelper(List<Book> books, BookshelfAdapter bookshelfAdapter) {
        this.books = books;
        this.bookshelfAdapter = bookshelfAdapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        int swipeFlags = 0;//不响应滑动方向
        int flags = makeMovementFlags(dragFlags,swipeFlags);
        return flags;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        if (fromPosition < toPosition){
            for (int i = fromPosition;i<toPosition;i++){
                Collections.swap(books,i,i+1);
            }
        }else {
            for (int i = fromPosition; i > toPosition;i--){
                Collections.swap(books,i,i-1);
            }
        }
        bookshelfAdapter.notifyItemMoved(fromPosition,toPosition);
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }


    @Override
    public void onMoved(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, int fromPos, @NonNull RecyclerView.ViewHolder target, int toPos, int x, int y) {
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);

        bookshelfAdapter.notifyItemRangeChanged(Math.min(fromPos, toPos), Math.abs(fromPos - toPos) + 1);
    }

    /** 选中状态改变通知 */
    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG){
            int bgColor = viewHolder.itemView.getContext().getResources().getColor(R.color.text_blue);
            viewHolder.itemView.setScaleX(1.2f);
            viewHolder.itemView.setScaleY(1.2f);
            viewHolder.itemView.setBackgroundColor(bgColor);
        }
    }

    /** 手指释放item或者交互动画结束时调用 viewHolder是释放的item的ViewHolder对象*/
    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setScaleX(1.0f);
        viewHolder.itemView.setScaleY(1.0f);
        int color = viewHolder.itemView.getContext().getResources().getColor(R.color.white);
        viewHolder.itemView.setBackgroundColor(color);
    }

    /** 用于item的绘制，手指拖动，松手复位均调用 */
    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    /** 修改滚动速度 下面是固定了划动速度*/
    @Override
    public int interpolateOutOfBoundsScroll(@NonNull RecyclerView recyclerView, int viewSize, int viewSizeOutOfBounds, int totalSize, long msSinceStartScroll) {
        final int direction = (int) Math.signum(viewSizeOutOfBounds);
        return 30 * direction;
    }
}
