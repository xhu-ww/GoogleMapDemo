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

/**
 * 判断是否有权限
 */
fun Context.hasPermissions(@Size(min = 1) vararg permission: String): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true
    return permission.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }
}

/**
 * 申请权限（如果是Fragment 则可新增方法 Fragment.requestPermissions）
 */
fun Activity.requestPermissions(@Size(min = 1) vararg permissions: String, requestCode: Int) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
    this.requestPermissions(permissions, requestCode)
}

/**
 * 位置权限申请框 勾选不再提醒后 是否弹出自己的提示框
 */
fun Activity.shouldShowCustomPermissionRequestHint(permission: String): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return false
    return !shouldShowRequestPermissionRationale(permission)
}

/**
 * 判断是否打开了定位服务
 */
fun Context.LocationServiceEnable(): Boolean {
    val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

fun Context.showLocationPermissionHintDialog(
    onPositiveButtonListener: () -> Unit,
    onNegativeButtonListener: () -> Unit = {}
): AlertDialog {
    return AlertDialog.Builder(this)
        .setMessage("当前应用需要打开位置权限，请点击设置进入设置界面打开位置权限")
        .setNegativeButton("取消") { _, _ ->
            onNegativeButtonListener()
        }
        .setPositiveButton("设置") { _, _ ->
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
        .setMessage("当前应用需要打开位置服务，请点击设置进入设置界面打开位置服务")
        .setNegativeButton("取消") { _, _ ->
            onNegativeButtonListener()
        }
        .setPositiveButton("设置") { _, _ ->
            onPositiveButtonListener()
        }
        .setCancelable(false)
        .show()
}

/**
 * 跳转App设置界面
 */
fun Context.appSettingIntent(): Intent {
    return Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.parse("package:$packageName")
    }
}

/**
 * 跳转系统 位置服务 设置界面
 */
fun LocationSettingIntent(): Intent {
    return Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
}
