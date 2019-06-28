package com.maze.healthapp.ui.frags


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage

import com.maze.healthapp.R
import com.maze.healthapp.utils.toast
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.ByteArrayOutputStream
import java.util.jar.Manifest


class ProfileFragment : Fragment() {

    val CAMERA_RC: Int = 999
    private lateinit var imageUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        avatar.setOnClickListener {
            takePictureIntent()
        }
    }

    private fun takePictureIntent() {



        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { picIntent ->
            picIntent.resolveActivity(activity?.packageManager!!)?.also {
                startActivityForResult(picIntent, CAMERA_RC)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {

            if (requestCode == CAMERA_RC) {
                val imageBitmap = data?.extras?.get("data") as Bitmap

                uploadAndSaveURI(imageBitmap)
            }
        }
    }

    private fun uploadAndSaveURI(imageBitmap: Bitmap) {
        val baos = ByteArrayOutputStream()

        val imageRef =
            FirebaseStorage.getInstance()
                .reference
                .child("avatars/${FirebaseAuth.getInstance().currentUser?.uid}")

        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

        val imageBytes = baos.toByteArray()

        imageProgress.visibility = View.VISIBLE

        val uploadTask = imageRef.putBytes(imageBytes)

        uploadTask.addOnCompleteListener { uploadTask ->
            imageProgress.visibility = View.GONE

            if (uploadTask.isSuccessful) {
                imageRef.downloadUrl.addOnCompleteListener { urlTask ->

                    urlTask.result?.let {
                        imageUri = it
                    }

                }
            } else {
                uploadTask.exception?.message?.let {
                    activity?.toast(it)
                }
            }

        }

    }


}
