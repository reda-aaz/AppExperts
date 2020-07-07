package com.redapp.twittos.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.redapp.twittos.R
import com.google.firebase.auth.FirebaseAuth
import com.redapp.twittos.ui.view.fragment.LogInFragment
import com.redapp.twittos.ui.view.fragment.SignUpFragment

class LoginActivity : AppCompatActivity(), LogInFragment.LoginListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if ((FirebaseAuth.getInstance().currentUser != null) && (FirebaseAuth.getInstance().currentUser?.isEmailVerified == true)) {
            //LoggedIn and verified!
            openHomeActivity()
        } else {
            if (savedInstanceState == null) {
                goLogin()
            }
        }
    }

    private fun goLogin() {
        val loginFragment =LogInFragment()
        loginFragment.setLoginListener(this)
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in,
                    R.anim.slide_out,
                    R.anim.slide_in,
                    R.anim.slide_out
                )
                .add(R.id.fragment_container,loginFragment)
                .addToBackStack(loginFragment.tag)
                .commit()
    }
    override fun goSignUp() {

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in,
                R.anim.slide_out,
                R.anim.slide_in,
                R.anim.slide_out
            )
            .replace(R.id.fragment_container, SignUpFragment())
            .addToBackStack(SignUpFragment().tag)
            .commit()
    }

    override fun openHomeActivity() {
        startActivity(Intent(this, MainActivity::class.java).also {
            it.flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        })
    }

}