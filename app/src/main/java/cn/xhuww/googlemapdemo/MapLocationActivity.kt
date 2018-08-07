package cn.xhuww.googlemapdemo

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import cn.xhuww.googlemapdemo.utils.*
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class MapLocationActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        requestLocationPermission()
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

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        mMap.isMyLocationEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true

        val locationResult = LocationServices.getFusedLocationProviderClient(this).lastLocation
        locationResult?.addOnCompleteListener {
            if (it.isSuccessful) {
                val location = it.result ?: return@addOnCompleteListener
                val latLng = LatLng(location.latitude, location.longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            } else {
                mMap.uiSettings.isMyLocationButtonEnabled = false
            }
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
