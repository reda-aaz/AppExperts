package com.redapp.twittos.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.redapp.twittos.R
import com.redapp.twittos.model.PostMessage
import kotlinx.android.synthetic.main.post_item_layout.view.*

class PostMessageAdapter(var postMessages: List<PostMessage>) :
    RecyclerView.Adapter<PostMessageAdapter.PostMessageViewHolder>() {

    inner class PostMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(postMessageItem: PostMessage) = with(itemView) {
            postMessageItem.apply {
                itemView.post_body_textview.text = messageText
                itemView.post_sender_textview.text = messageSender
                if (imageUrl.isNotEmpty()) {
                    Glide.with(context)
                        .applyDefaultRequestOptions(RequestOptions().centerCrop())
                        .load(this.imageUrl)
                        .into(itemView.post_imageview)
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostMessageViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_item_layout, parent, false)
        return PostMessageViewHolder(view)
    }


    override fun getItemCount(): Int = postMessages.size

    override fun onBindViewHolder(messageViewHolder: PostMessageViewHolder, position: Int) {

        messageViewHolder.bind(postMessages[position])
    }
}