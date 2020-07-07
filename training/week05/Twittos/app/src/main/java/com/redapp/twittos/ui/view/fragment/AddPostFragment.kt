package com.redapp.twittos.ui.view.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.redapp.twittos.R
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.redapp.twittos.model.PostMessage
import com.redapp.twittos.utils.AppSingleton
import kotlinx.android.synthetic.main.add_post_fragment_layout.*
import kotlinx.android.synthetic.main.post_item_layout.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class
AddPostFragment : Fragment() {
    companion object {
        private val fileProvider = "com.redapp.twittos.provider"
    }

    val REQ_CODE = 777
    private var imageBitmap: Bitmap? = null
    private var fileDirectoryPath: String = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.add_post_fragment_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AppSingleton.my_ID = FirebaseAuth.getInstance().currentUser?.email ?: "1010"

        upload_post_button.setOnClickListener {

            imageBitmap?.let {image->
                val bAOS: ByteArrayOutputStream = ByteArrayOutputStream()
                image.compress(Bitmap.CompressFormat.JPEG, 100,bAOS)
                val imageByteArray = bAOS.toByteArray()
                val storageReference = FirebaseStorage.getInstance().reference.child("/UPLOADS/${FirebaseAuth.getInstance().currentUser?.uid}")
                val imageUploadTask = storageReference.putBytes(imageByteArray)

                imageUploadTask.addOnCompleteListener{ task ->
                    if(task.isSuccessful){
                        storageReference.downloadUrl.addOnCompleteListener {imageUriTask->
                            val message = PostMessage().also {
                                it.messageSender = AppSingleton.my_ID
                                it.messageText = body_edittext.text.toString().trim()
                                it.imageUrl=imageUriTask.result?.toString() ?: ""
                            }

                            FirebaseDatabase.getInstance().reference.child("POST_MESSAGES")
                                .push().setValue(message)

                            activity?.supportFragmentManager?.popBackStack()

                        }
                    }

                }

            }
        }

        picture_imageView.setOnClickListener {
            context?.let { ctx ->
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (cameraIntent.resolveActivity(ctx.packageManager) != null) {
                    try {


                        val file = temporaryImage()
                        file?.let {
                            val imageUri = FileProvider.getUriForFile(ctx, fileProvider, it)
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                            startActivityForResult(cameraIntent,REQ_CODE)
                        }
                    }catch (e: IOException){
                        Log.d("TAG_X", "${e.localizedMessage}")
                    }
                }
            }
        }
    }

    private fun temporaryImage(): File? {
        val dateStamp = SimpleDateFormat("yyyy_MM_dd_mm_ss", Locale.FRANCE).format(Date())
        val fileName = "$dateStamp${FirebaseAuth.getInstance().currentUser?.uid}"
        var fileDirectory: File? = null

        context?.let {
            fileDirectory = it.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        }
        fileDirectory?.let {
            val imageFile = File.createTempFile(
                fileName,
                ".jpg",
                it
            )
            fileDirectoryPath = imageFile.absolutePath ?: ""
            return imageFile
        }

        return null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CODE) {
            val bitmap = BitmapFactory.decodeFile(fileDirectoryPath)
            imageBitmap = bitmap
            context?.let {
                Glide.with(it)
                    .applyDefaultRequestOptions(RequestOptions().centerCrop())
                    .load(bitmap)
                    .into(picture_imageView)
            }
        }
    }
}
