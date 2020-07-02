package com.redapp.myplacecompass.util

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.util.Log


private const val TAG = "PlaceLocationListener"

class PlaceLocationListener(private val locationDelegate: LocationDelegate) : LocationListener {
    lateinit var locationString: String
    lateinit var locationLatLng: Location


    override fun onLocationChanged(location: Location) {
        Log.i(TAG, "onLocationChanged: ")
        locationDelegate.setLocation(location)
        locationString = "${location.latitude},${location.longitude}"
        locationLatLng = location
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        // Do nothing
    }

    override fun onProviderEnabled(provider: String?) {
        // Do nothing
    }

    override fun onProviderDisabled(provider: String?) {
        // Do nothing
    }

    interface LocationDelegate {
        fun setLocation(location: Location)
    }
}