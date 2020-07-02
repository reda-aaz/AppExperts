package com.redapp.myowncustomviewactivity

import android.os.Build
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity(){
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        profile1.setProfile(Profile("Redox","redred.com"))
        profile2.setProfile(Profile("Donkey KONG", "donkey@jungle.com",R.drawable.donkey_kong))
        val animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_anim)
        val animation2 = AnimationUtils.loadAnimation(this, R.anim.slide_in_anim_from_left)
        profile1.startAnimation(animation)
        profile2.startAnimation(animation2)
    }
}