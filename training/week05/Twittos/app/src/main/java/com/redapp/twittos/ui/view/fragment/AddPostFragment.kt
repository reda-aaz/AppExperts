package com.redapp.twittos.ui.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.redapp.twittos.R
import com.google.firebase.database.FirebaseDatabase
import com.redapp.twittos.model.PostMessage
import com.redapp.twittos.utils.AppSingleton
import kotlinx.android.synthetic.main.add_post_fragment_layout.*

class
AddPostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.add_post_fragment_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AppSingleton.my_ID = FirebaseAuth.getInstance().currentUser?.email?:"1010"

        upload_post_button.setOnClickListener {
            //Upload to firebase

            val message = PostMessage().also {
                it.messageSender = AppSingleton.my_ID
                it.messageText = body_edittext.text.toString().trim()
                body_edittext.text.clear()
            }

            FirebaseDatabase.getInstance().reference.child("POST_MESSAGES")
                .push().setValue(message)

            activity?.supportFragmentManager?.popBackStack()
        }
    }
}
