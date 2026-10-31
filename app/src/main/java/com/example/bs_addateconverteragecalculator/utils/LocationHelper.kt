package com.example.bs_addateconverteragecalculator.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.provider.Settings
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.bs_addateconverteragecalculator.data.PreferencesManager
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.TimeZone

object LocationHelper {
    fun hasLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
               ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    fun isGpsEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
               locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun promptEnableGps(context: Context) {
        Toast.makeText(context, "Please enable GPS for accurate Astrological calculations.", Toast.LENGTH_LONG).show()
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun fetchAndSaveDeviceLocation(context: Context, preferencesManager: PreferencesManager, coroutineScope: CoroutineScope) {
        if (!hasLocationPermission(context)) return
        if (!isGpsEnabled(context)) {
            promptEnableGps(context)
            return
        }

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val lat = location.latitude
                    val lon = location.longitude
                    
                    // Device's current timezone offset in hours
                    val tzOffset = TimeZone.getDefault().rawOffset / 3600000.0

                    // Reverse geocode to get a readable name (fallback to coordinates if it fails)
                    var locationName = "Current Device Location"
                    try {
                        val geocoder = Geocoder(context, Locale.getDefault())
                        val addresses = geocoder.getFromLocation(lat, lon, 1)
                        if (!addresses.isNullOrEmpty()) {
                            val address = addresses[0]
                            val city = address.locality ?: address.subAdminArea ?: address.adminArea
                            val country = address.countryName
                            if (city != null && country != null) {
                                locationName = "$city, $country"
                            } else if (country != null) {
                                locationName = country
                            }
                        }
                    } catch (e: Exception) {
                        // Geocoder might not be available on all devices/networks
                    }

                    coroutineScope.launch {
                        preferencesManager.setAutoGpsLocation(locationName, lat, lon, tzOffset)
                        Toast.makeText(context, "Location synced: $locationName", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Waiting for GPS signal...", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}
