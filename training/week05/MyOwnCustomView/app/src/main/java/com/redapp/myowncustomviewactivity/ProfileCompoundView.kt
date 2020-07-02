package com.redapp.myowncustomviewactivity

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class ProfileCompoundView(context: Context, attributeSet: AttributeSet? = null): CardView(context,attributeSet) {

    var emailString: String = ""
    set
    var pseudoString: String = ""
    var imageDrawable: Int = 0

    private var emailTextView: TextView
    private var pseudoTextView: TextView
    private var profileImageView: ImageView

    init {

        val layoutInflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        layoutInflater.inflate(R.layout.profile_view_layout, this, true)

        val tArray = context.obtainStyledAttributes(attributeSet,R.styleable.ProfileCompoundView)
        emailString = tArray.getString(R.styleable.ProfileCompoundView_email) ?: ""
        pseudoString = tArray.getString(R.styleable.ProfileCompoundView_pseudo) ?: ""
        imageDrawable = tArray.getResourceId(
            R.styleable.ProfileCompoundView_personPic,
            R.drawable.ic_launcher_foreground
        )


        emailTextView = this.findViewById(R.id.email_textview)
        pseudoTextView = this.findViewById(R.id.pseudo_textview)
        profileImageView = this.findViewById(R.id.view_imageview)

        setUpView()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setUpView() {
        emailTextView.text = emailString
        pseudoTextView.text = pseudoString
        profileImageView.setImageDrawable(context.getDrawable(imageDrawable))
    }

    fun setProfile(profile: Profile){
        profile.apply {
            emailTextView.text = email
            pseudoTextView.text = pseudo
            profileImageView.setImageDrawable(context.getDrawable(image))
        }
    }
}