package com.redapp.twittos.ui.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.redapp.twittos.R
import kotlinx.android.synthetic.main.fragment_sign_up.*


class SignUpFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signUpFragment_button.setOnClickListener {
            val email = signUp_username.text.toString().trim()
            val password = signUp_password.text.toString().trim()
            val confirmPassword = confirm_password_editText.text.toString().trim()
            if(email.isNotEmpty() && password.isNotEmpty()) {

                if (password == confirmPassword) {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        email, password
                    ).addOnCompleteListener { signUpTask ->
                        if (signUpTask.isSuccessful) {
                            //first time user - send a verification email and let the user know
                            FirebaseAuth.getInstance().currentUser?.sendEmailVerification()
                            verificationToast()
                        } else {
                            Toast.makeText(
                                context,
                                "${signUpTask.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()

                        }


                    }
                } else {
                    Toast.makeText(context, "Please confirm your password", Toast.LENGTH_LONG)
                        .show()
                }
            }
            else {
                Toast.makeText(context, "Please enter your credentials details", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun verificationToast() {
        Toast.makeText(context, "Please verify email - link sent", Toast.LENGTH_SHORT).show()
    }
}