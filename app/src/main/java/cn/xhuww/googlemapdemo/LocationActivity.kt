package cn.xhuww.googlemapdemo

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import cn.xhuww.googlemapdemo.utils.*
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.location_activity.*

class LocationActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.location_activity)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                val location = locationResult.lastLocation
                longitudeEditText.setText(location.longitude.toString())
                latitudeEditText.setText(location.latitude.toString())
            }
        }

        requestLocationPermission()
        transformAddress.setOnClickListener { requestLocationPermission() }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
//        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
//            if (location == null) return@addOnSuccessListener
//
//            longitudeEditText.setText(location.longitude.toString())
//            latitudeEditText.setText(location.latitude.toString())
//        }

//        val builder = LocationSettingsRequest.Builder()
//
//        val client: SettingsClient = LocationServices.getSettingsClient(this)
//        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
//
//        task.addOnSuccessListener { locationSettingsResponse ->
//            if (locationSettingsResponse == null) return@addOnSuccessListener
//
//            longitudeEditText.setText(location.longitude.toString())
//            latitudeEditText.setText(location.latitude.toString())
//        }
//        task.addOnFailureListener { exception ->
//            exception.printStackTrace()
//        }

        fusedLocationClient.requestLocationUpdates(LocationRequest(), locationCallback, null)
    }

    private fun requestLocationPermission() {
        when {
            hasPermissions(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                requestLocationService()
            }
            else -> requestPermissions(Manifest.permission.ACCESS_FINE_LOCATION, requestCode = 1)
        }
    }

    private fun requestLocationService() {
        if (LocationServiceEnable()) {
            getLastLocation()
        } else {
            showLocationServiceHintDialog(
                onPositiveButtonListener = {
                    startActivityForResult(LocationSettingIntent(), LOCATION_SETTINGS_REQUEST_CODE)
                }
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLastLocation()
        } else if (shouldShowCustomPermissionRequestHint(Manifest.permission.ACCESS_FINE_LOCATION)) {
            showLocationPermissionHintDialog(onPositiveButtonListener = {
                startActivityForResult(appSettingIntent(), APP_SETTINGS_REQUEST_CODE)
            })
        } else {
            requestLocationPermission()
        }
    }

    companion object {
        private const val APP_SETTINGS_REQUEST_CODE = 1
        private const val LOCATION_SETTINGS_REQUEST_CODE = 2
    }
}
