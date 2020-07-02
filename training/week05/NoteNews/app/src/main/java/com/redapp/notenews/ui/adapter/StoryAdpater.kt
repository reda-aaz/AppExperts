package com.redapp.notenews.ui.adapter

import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.redapp.notenews.R
import com.redapp.notenews.databinding.StoryItemBinding
import com.redapp.notenews.model.StoryNote

class StoryAdpater: ListAdapter<StoryNote, StoryAdpater.ViewHolder>(StoryDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }



    class ViewHolder private constructor(private val binding: StoryItemBinding ): RecyclerView.ViewHolder(binding.root){


        fun bind(storyItem: StoryNote){
            storyItem.apply {
            binding.titleTextView.text = title
            binding.bodyTextView.text = body
            binding.dateTextView.text = DateFormat.getDateInstance().format(date)

            }

        }

        companion object{
            fun from(parent: ViewGroup):StoryAdpater.ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = StoryItemBinding.inflate(layoutInflater, parent,false)
                return ViewHolder(binding)
            }
        }
    }




}

class StoryDiffCallBack : DiffUtil.ItemCallback<StoryNote>(){
    override fun areItemsTheSame(oldItem: StoryNote, newItem: StoryNote): Boolean {
        return oldItem.storyId == newItem.storyId
    }

    override fun areContentsTheSame(oldItem: StoryNote, newItem: StoryNote): Boolean {
        return oldItem == newItem
    }

}
