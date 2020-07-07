package com.redapp.twittos.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.redapp.twittos.R
import com.redapp.twittos.model.PostMessage
import kotlinx.android.synthetic.main.post_item_layout.view.*

class PostMessageAdapter(var postMessages: List<PostMessage>) :
    RecyclerView.Adapter<PostMessageAdapter.PostMessageViewHolder>() {

    inner class PostMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostMessageViewHolder {

            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.post_item_layout, parent, false)
        return PostMessageViewHolder(view)
    }



    override fun getItemCount(): Int = postMessages.size

    override fun onBindViewHolder(messageViewHolder: PostMessageViewHolder, position: Int) {

        postMessages[position].apply {
            messageViewHolder.itemView.post_body_textview.text = messageText
            messageViewHolder.itemView.post_sender_textview.text = messageSender
        }
    }
}