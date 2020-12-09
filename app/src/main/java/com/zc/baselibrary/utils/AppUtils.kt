package com.zc.baselibrary.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager

/**
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
object AppUtils {

    private val DEBUG: Boolean = true
    private val TAG: String = "SystemUtils"

    fun getVersionName(context: Context?): String? {
        return try {
            context?.packageManager?.getPackageInfo(context.packageName, 0)?.versionName
        } catch (var2: PackageManager.NameNotFoundException) {
            "1.0"
        }
    }

    fun getVersionCode(context: Context?): Int {
        return try {
            context?.packageManager?.getPackageInfo(context.packageName, 0)?.versionCode ?: 0
        } catch (var2: PackageManager.NameNotFoundException) {
            0
        }
    }

    fun getAppName(context: Context): String? {
        return try {
            val applicationInfo: ApplicationInfo =
                context.packageManager.getApplicationInfo(context.packageName, 0)
            val name: String? =
                context.packageManager?.getApplicationLabel(applicationInfo) as String?
            name
        } catch (var3: PackageManager.NameNotFoundException) {
            ""
        }
    }

    @SuppressLint("DefaultLocale")
    fun getIp(context: Context): String {
        val wifiManager: WifiManager =
            context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo: WifiInfo = wifiManager.connectionInfo
        val ipAddress: Int = wifiInfo.ipAddress
        val ip: String = String.format(
            "%d.%d.%d.%d",
            ipAddress and 255,
            ipAddress shr 8 and 255,
            ipAddress shr 16 and 255,
            ipAddress shr 24 and 255
        )
        return ip
    }
}