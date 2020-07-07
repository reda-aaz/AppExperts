package com.redapp.twittos.ui.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.redapp.twittos.R
import kotlinx.android.synthetic.main.fragment_log_in.*

class LogInFragment : Fragment() {

    private lateinit var loginListener: LoginListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        login.setOnClickListener {

            val email = username.text.toString().trim()
            val password = password.text.toString().trim()

            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        if (FirebaseAuth.getInstance().currentUser?.isEmailVerified == true)
                            loginListener.openHomeActivity()
                        else
                            verificationToast()

                    } else {
                        if (task.exception?.message?.contains("no user record") == true) {
                            //user does not exist create account


                            Toast.makeText(context, "No user Record Found, You should sign up first", Toast.LENGTH_SHORT)
                                .show()



                        } else {
//                            wrong password o some other error
                            Toast.makeText(context, "${task.exception?.message}", Toast.LENGTH_SHORT)
                                .show()

                        }


                    }

                }

        }

        signUp_button.setOnClickListener { loginListener.goSignUp()}
    }

    private fun verificationToast() {
        Toast.makeText(context, "Please verify email - link sent", Toast.LENGTH_SHORT).show()
    }



    fun setLoginListener(loginListener: LoginListener){
        this.loginListener = loginListener
    }

    interface LoginListener{
        fun goSignUp()
        fun openHomeActivity()
    }

}