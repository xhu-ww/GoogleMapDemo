package cn.xhuww.googlemapdemo.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.annotation.Size
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import cn.xhuww.googlemapdemo.R

fun Context.hasPermissions(@Size(min = 1) vararg permission: String): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true
    return permission.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }
}

fun Activity.shouldShowCustomPermissionRequestHint(permission: String): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return false
    return !shouldShowRequestPermissionRationale(permission)
}

fun Activity.requestPermissions(@Size(min = 1) vararg permissions: String, requestCode: Int) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
    this.requestPermissions(permissions, requestCode)
}

fun Context.LocationServiceEnable(): Boolean {
    val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

fun Context.showLocationPermissionHintDialog(
    onPositiveButtonListener: () -> Unit,
    onNegativeButtonListener: () -> Unit = {}
): AlertDialog {
    return AlertDialog.Builder(this)
        .setTitle(R.string.dialog_permission_title)
        .setMessage(R.string.dialog_permission_content)
        .setNegativeButton(R.string.dialog_permission_negative) { _, _ ->
            onNegativeButtonListener()
        }
        .setPositiveButton(R.string.dialog_permission_positive) { _, _ ->
            onPositiveButtonListener()
        }
        .setCancelable(false)
        .show()
}

fun Context.showLocationServiceHintDialog(
    onPositiveButtonListener: () -> Unit,
    onNegativeButtonListener: () -> Unit = {}
): AlertDialog {
    return AlertDialog.Builder(this)
        .setMessage(R.string.dialog_location_content)
        .setNegativeButton(R.string.dialog_location_negative) { _, _ ->
            onNegativeButtonListener()
        }
        .setPositiveButton(R.string.dialog_location_positive) { _, _ ->
            onPositiveButtonListener()
        }
        .setCancelable(false)
        .show()
}

fun Context.appSettingIntent(): Intent {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.data = Uri.parse("package:$packageName")
    return intent
}

fun LocationSettingIntent(): Intent {
    return Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
}
