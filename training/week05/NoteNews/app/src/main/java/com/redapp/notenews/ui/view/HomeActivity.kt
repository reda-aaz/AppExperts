
package com.redapp.notenews.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.redapp.notenews.R


class HomeActivity : AppCompatActivity() , StoryNoteTrackerFragment.OnUpsertNoteListner{



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null)
            addFragment()




    }

    fun addFragment(){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = StoryNoteTrackerFragment()
        fragment.setOnUpsertListner(this)
        fragmentTransaction.add(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }

    override fun onUpsertNote(storyNoteId: Int?) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = UpsertStoryNoteFragment.newInstance(storyNoteId)
        fragmentTransaction.replace(R.id.fragment_container, fragment).isAddToBackStackAllowed
        fragmentTransaction.commit()
    }
}