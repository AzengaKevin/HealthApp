package com.maze.healthapp.ui.frags


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

import com.maze.healthapp.R
import com.maze.healthapp.models.User
import com.maze.healthapp.utils.toast
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.ByteArrayOutputStream


class ProfileFragment : Fragment() {

    private val DEFAULT_IMAGE_URL = "https://picsum.photos/200"
    private val CAMERA_RC: Int = 999
    private lateinit var imageUri: Uri
    private lateinit var db: FirebaseFirestore

    private val currUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        db = FirebaseFirestore.getInstance()

        currUser?.let { user ->
            user.photoUrl?.let {
                Glide.with(this)
                    .load(user.photoUrl)
                    .into(userAvatar)
            }

            username.setText(user.displayName)

            emailTV.text = user.email

            phoneTV.text = if (user.phoneNumber.isNullOrEmpty()) "Add Number" else user.phoneNumber

            if (user.isEmailVerified) emaiNotVerifiedTV.visibility = View.GONE
            else emaiNotVerifiedTV.visibility = View.VISIBLE

        }

        userAvatar.setOnClickListener {
            takePictureIntent()
        }

        saveBtn.setOnClickListener {
            val avatarUri = when {
                ::imageUri.isInitialized -> imageUri
                currUser?.photoUrl == null -> Uri.parse(DEFAULT_IMAGE_URL)
                else -> currUser.photoUrl

            }

            val name = username.text.toString().trim()

            if (name.isEmpty()) {
                username.error = "Name is required"
                username.requestFocus()
                return@setOnClickListener
            }

            val updates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(avatarUri)
                .build()

            saveProgress.visibility = View.VISIBLE

            currUser?.updateProfile(updates)!!
                .addOnCompleteListener { task ->
                    saveProgress.visibility = View.GONE

                    if (task.isSuccessful) {
                        activity?.toast("Profile Updated")
                    } else {
                        task.exception?.message?.let {
                            activity?.toast(it)
                        }
                    }

                }

            //Add User Details to Firestore
            val updatedUser = FirebaseAuth.getInstance().currentUser

            updatedUser?.let {
                val user =
                    User(it.uid, "Regular", it.displayName ?: "", it.photoUrl.toString(), it.phoneNumber ?: "", "", "")

                db.document("users/${it.uid}")
                    .set(user)
                    .addOnCompleteListener { task ->

                        if (task.isSuccessful) {
                            activity?.toast("Profile Updated In Firestore")
                        } else {
                            activity?.toast("Firestore Update Failed")
                        }
                    }
            }


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

        val upload = imageRef.putBytes(imageBytes)

        upload.addOnCompleteListener { uploadTask ->
            imageProgress.visibility = View.GONE

            if (uploadTask.isSuccessful) {
                imageRef.downloadUrl.addOnCompleteListener { urlTask ->
                    urlTask.result?.let {
                        imageUri = it
                        userAvatar.setImageBitmap(imageBitmap)
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
