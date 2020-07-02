package com.redapp.myowncustomviewactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rotatedImageView: RotatedImageView = RotatedImageView(this)
        setContentView(rotatedImageView)

        Handler().postDelayed({
            startActivity(Intent(this, ProfileActivity::class.java).also {
                it.flags = (Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        }, 3000)
    }
}