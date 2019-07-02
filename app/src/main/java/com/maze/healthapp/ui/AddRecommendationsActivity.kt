package com.maze.healthapp.ui

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import com.maze.healthapp.R
import com.maze.healthapp.utils.toast
import kotlinx.android.synthetic.main.activity_add_recommendations.*
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*


class AddRecommendationsActivity : AppCompatActivity() {

    private val TAG = "AddRecommendationsAct"

    private val STORAGE_RC = 999
    private val IMAGE_RC = 100
    private var section = "bmi"
    private var imagePath: Uri? = null

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recommendations)

        db = FirebaseFirestore.getInstance()

        setupSinner()

        recImage.setOnClickListener {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                toast("Permissions Granted")
                openChooseImageActivity()
            } else {

                ActivityCompat.requestPermissions(
                    this@AddRecommendationsActivity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    STORAGE_RC
                )

            }
        }

        addButton.setOnClickListener {
            validate()
        }

    }

    private fun validate() {

        when (section) {
            "bmi" -> {
                if (bmiET.text.toString().trim().isNullOrEmpty()) {
                    bmiET.error = "BMI is required"
                    bmiET.requestFocus()
                    return
                }
            }

            "bsl" -> {
                if (bslET.text.toString().trim().isNullOrEmpty()) {
                    bslET.error = "BSL is required"
                    bslET.requestFocus()
                    return
                }
            }

            "bp" -> {
                if (systoleET.text.toString().trim().isNullOrEmpty()) {
                    systoleET.error = "Systole is required"
                    systoleET.requestFocus()
                    return
                }

                if (diastoleET.text.toString().trim().isNullOrEmpty()) {
                    diastoleET.error = "Diastole is required"
                    diastoleET.requestFocus()
                    return
                }

            }
        }

        if (recContent.text.toString().trim().isNullOrEmpty()) {
            recContent.error = "Recommendation is required"
            recContent.requestFocus()
        }

        if (imagePath == null) {
            toast("Please Select An Image")
            return
        }

        uploadDetails()
    }

    private fun uploadDetails() {
        toast("We are good to go")

        val fileRef = FirebaseStorage.getInstance().getReference("recommendations/${UUID.randomUUID()}")

        val uploadTask = fileRef.putFile(imagePath!!)

        uploadTask
            .addOnSuccessListener {
                fileRef.downloadUrl.addOnSuccessListener { uri ->

                    toFirestore(uri.toString())

                }

            }
            .addOnFailureListener { e ->
                Log.e(TAG, "uploadDetails", e)
            }


    }

    private fun toFirestore(imageUrl: String) {

        val content = recContent.text.toString().trim()

        when (section) {
            "bmi" -> {
                val bmi = bmiET.text.toString().trim()
                val data = hashMapOf(
                    "photoUrl" to imageUrl,
                    "bmi" to bmi,
                    "content" to content
                )

                db.collection(section)
                    .add(data)
                    .addOnSuccessListener {
                        toast("Recommendation Added")
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "toFirestore", e)
                    }

            }
            "bsl" -> {
                val bsl = bslET.text.toString().trim()

                val data = hashMapOf(
                    "photoUrl" to imageUrl,
                    "bsl" to bsl,
                    "content" to content
                )

                db.collection(section)
                    .add(data)
                    .addOnSuccessListener {
                        toast("Recommendation Added")
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "toFirestore", e)
                    }

            }


            "bp" -> {
                val systole = systoleET.text.toString().trim()
                val diastole = diastoleET.text.toString().trim()

                val data = hashMapOf(
                    "photoUrl" to imageUrl,
                    "systole" to systole,
                    "diastole" to diastole,
                    "content" to content
                )

                db.collection(section)
                    .add(data)
                    .addOnSuccessListener {
                        toast("Recommendation Added")
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "toFirestore", e)
                    }

            }
        }

    }

    private fun openChooseImageActivity() {

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Image"), IMAGE_RC)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            when (requestCode) {
                STORAGE_RC -> openChooseImageActivity()

                else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        } else {
            toast("Permission Denied")
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == Activity.RESULT_OK) {

            if (data != null) {
                when (requestCode) {

                    IMAGE_RC -> {

                        imagePath = data.data

                        imagePath?.let {
                            recImage.setImageURI(it)
                        }

                    }
                    else -> super.onActivityResult(requestCode, resultCode, data)
                }
            }


        } else {
            toast("Action Cancelled")
        }

    }

    private fun setupSinner() {

        val sectionAdapter = ArrayAdapter<String>(
            this@AddRecommendationsActivity,
            android.R.layout.simple_list_item_1,
            resources.getStringArray(R.array.categories)
        )
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        sectionSpinner.adapter = sectionAdapter

        sectionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {

                when (pos) {
                    0 -> {
                        section = "bmi"
                        bmiET.visibility = View.VISIBLE
                        bslET.visibility = View.GONE
                        systoleET.visibility = View.GONE
                        diastoleET.visibility = View.GONE

                    }

                    1 -> {
                        section = "bsl"
                        bmiET.visibility = View.GONE
                        bslET.visibility = View.VISIBLE
                        systoleET.visibility = View.GONE
                        diastoleET.visibility = View.GONE
                    }

                    2 -> {
                        section = "bp"

                        bmiET.visibility = View.GONE
                        bslET.visibility = View.GONE
                        systoleET.visibility = View.VISIBLE
                        diastoleET.visibility = View.VISIBLE
                    }
                }

                toast(section)

            }

        }
    }
}
