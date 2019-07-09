package com.maze.healthapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.maze.healthapp.R

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    val TAG = "MapsActivity"
    val ERR_DIALOG_RC = 100

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        isServiceOK()
    }

    fun isServiceOK(): Boolean {

        Log.d(TAG, "isServiceOK: checking google service version")

        val availabe = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this@MapsActivity)

        if (availabe == ConnectionResult.SUCCESS) {
            Log.d(TAG, "isServiceOK: checking google service version")
            return true
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(availabe)) {
            Log.d(TAG, "isServiceOK: an error occurred but you ca fix it")

            GoogleApiAvailability.getInstance().getErrorDialog(this@MapsActivity, availabe, ERR_DIALOG_RC).show()
            return true

        } else {
            Log.d(TAG, "isServiceOK: You can't make maps request")

            return false
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}
