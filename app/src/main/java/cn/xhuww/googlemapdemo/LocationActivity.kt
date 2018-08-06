package cn.xhuww.googlemapdemo

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import cn.xhuww.googlemapdemo.utils.*

class LocationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.location_activity)
    }

    private fun requestLocationPermission() {
        when {
            hasPermissions(Manifest.permission.ACCESS_FINE_LOCATION) -> {

            }
            shouldShowCustomPermissionRequestHint(Manifest.permission.ACCESS_FINE_LOCATION) ->
                showLocationPermissionHintDialog(onPositiveButtonListener = {
                    startActivityForResult(appSettingIntent(), APP_SETTINGS_REQUEST_CODE)
                })
            else -> requestPermissions(Manifest.permission.ACCESS_FINE_LOCATION, requestCode = 1)
        }
    }

    private fun requestLocationService() {
        if (LocationServiceEnable()) {

        } else {
            showLocationServiceHintDialog(
                onPositiveButtonListener = {
                    startActivityForResult(LocationSettingIntent(), LOCATION_SETTINGS_REQUEST_CODE)
                }
            )
        }
    }

    companion object {
        private const val APP_SETTINGS_REQUEST_CODE = 1
        private const val LOCATION_SETTINGS_REQUEST_CODE = 2
    }
}
