package com.app.fivepl.helper

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.app.ActivityCompat

class LocationHelper(private val context: Context) {

    private val locationManager: LocationManager? = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?

    private val MIN_TIME_BW_UPDATES: Long = 1000 * 10 // Minimum time interval between location updates (milliseconds)
    private val MIN_DISTANCE_CHANGE_FOR_UPDATES: Float = 10f // Minimum distance between location updates (meters)

    private var locationListener: LocationListener? = null

    init {
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                // Do something with the location
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }
    }

    fun requestLocationUpdates() {
        // Check for permission
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        // Request location updates
        locationManager?.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            MIN_TIME_BW_UPDATES,
            MIN_DISTANCE_CHANGE_FOR_UPDATES,
            locationListener!!
        )
    }

    fun stopLocationUpdates() {
        locationManager?.removeUpdates(locationListener!!)
    }

    fun getLastKnownLocation(): Location? {
        // Check for permission
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return null
        }

        // Get last known location
        return locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
    }
}
