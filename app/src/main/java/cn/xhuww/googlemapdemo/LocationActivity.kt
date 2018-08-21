package cn.xhuww.googlemapdemo

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import cn.xhuww.googlemapdemo.utils.*
import com.google.android.gms.location.*

import kotlinx.android.synthetic.main.location_activity.*

class LocationActivity : AppCompatActivity() {
    private val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.location_activity)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest().apply {
            interval = 10000  //请求时间间隔
            fastestInterval = 5000 //最快时间间隔
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                handleLocation(locationResult.lastLocation)
            }
        }
        requestLocationPermission()
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdate() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.myLooper()
        )
    }

    /**
     * 停止获取位置更新
     */
    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun handleLocation(location: Location) {
        longitudeEditText.setText(location.longitude.toString())
        latitudeEditText.setText(location.latitude.toString())

        val url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" +
                "${location.latitude},${location.longitude}&language=zh-CN&sensor=false"
        addressTextView.text = url
        //请求网络即可
    }

    private fun requestLocationPermission() {
        when {
            hasPermissions(locationPermission) -> requestLocationService()
            else -> requestPermissions(locationPermission, requestCode = 1)
        }
    }

    private fun requestLocationService() {
        if (LocationServiceEnable()) requestLocationUpdate()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            requestLocationUpdate()
        }
    }
}
