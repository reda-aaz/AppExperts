package com.redapp.twittos.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.redapp.twittos.ui.view.fragment.AddPostFragment
import com.redapp.twittos.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.redapp.twittos.model.PostMessage
import com.redapp.twittos.utils.AppSingleton
import com.redapp.twittos.ui.adapter.PostMessageAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val addPostFragment: AddPostFragment =
        AddPostFragment()

    private val postAdapter: PostMessageAdapter = PostMessageAdapter(mutableListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        content_recyclerview.layoutManager = LinearLayoutManager(this)
        content_recyclerview.adapter = postAdapter

        AppSingleton.my_ID = FirebaseAuth.getInstance().currentUser?.email?:"1010"
        sign_out_imageView.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            openLoginActivity()
        }

        FirebaseDatabase.getInstance().reference.child("POST_MESSAGES")
            .addValueEventListener(object : ValueEventListener {

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d("TAG_X", "${databaseError.message}")
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val list: MutableList<PostMessage> = mutableListOf()

                    dataSnapshot.children.forEach { data ->
                        data.getValue(PostMessage::class.java)?.let { postMessage ->
                            list.add(postMessage)
                        }
                    }

                    updateMessages(list)

                }
            })

        new_post_button.setOnClickListener {

            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in,
                    R.anim.slide_out,
                    R.anim.slide_in,
                    R.anim.slide_out
                )
                .add(R.id.main_frame, addPostFragment)
                .addToBackStack(addPostFragment.tag)
                .commit()
        }


    }

    private fun updateMessages(list: MutableList<PostMessage>) {
        postAdapter.postMessages = list
        postAdapter.notifyDataSetChanged()
    }

    fun openLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java).also {
            it.flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        })
    }
}