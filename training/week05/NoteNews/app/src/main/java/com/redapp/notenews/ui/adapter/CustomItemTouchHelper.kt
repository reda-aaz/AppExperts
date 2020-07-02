package com.redapp.notenews.ui.adapter

import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.redapp.notenews.model.StoryNote

class CustomItemTouchHelper(private val storyAdapter: StoryAdpater, private val listener: CustomItemTouchHelper.CustomSwipListner) :
    ItemTouchHelper.SimpleCallback(0, (ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT)) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        Log.d("TAG_X", "onMove...")

        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        Log.d("TAG_X", "swiped... outside")
        if (direction == (ItemTouchHelper.LEFT) || direction == (ItemTouchHelper.RIGHT)) {
            val itemPosition = viewHolder.adapterPosition
            listener.onSwipedNote(storyAdapter.currentList.get(itemPosition))
        }
    }

    override fun onMoved(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        fromPos: Int,
        target: RecyclerView.ViewHolder,
        toPos: Int,
        x: Int,
        y: Int
    ) {
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y)


    }


    override fun getSwipeDirs(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {

        Log.d("TAG_X", "Swipe DIR")
        return super.getSwipeDirs(recyclerView, viewHolder)
    }
    interface CustomSwipListner {
        fun onSwipedNote(storyNote : StoryNote)
    }
}
