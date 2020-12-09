package com.hh.hlibrary.utils

import android.annotation.SuppressLint

/**
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
object AppUtils {
    private val DEBUG: Boolean = true
    private val TAG: String? = "SystemUtils"
    fun getVersionName(context: Context?): String? {
        try {
            val versionName: String? =
                context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName
            return versionName
        } catch (var2: NameNotFoundException) {
            return "1.0"
        }
    }

    fun getVersionCode(context: Context?): Int {
        try {
            val versionCode: Int =
                context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode
            return versionCode
        } catch (var2: NameNotFoundException) {
            return 0
        }
    }

    fun getAppName(context: Context?): String? {
        try {
            val applicationInfo: ApplicationInfo? =
                context.getPackageManager().getApplicationInfo(context.getPackageName(), 0)
            val name: String? =
                context.getPackageManager().getApplicationLabel(applicationInfo) as String?
            return name
        } catch (var3: NameNotFoundException) {
            return ""
        }
    }

    @SuppressLint(["DefaultLocale"])
    fun getIp(context: Context?): String? {
        val wifiManager: WifiManager? =
            context.getSystemService(Context.WIFI_SERVICE) as WifiManager?
        val wifiInfo: WifiInfo? = wifiManager.getConnectionInfo()
        val ipAddress: Int = wifiInfo.getIpAddress()
        val ip: String? = String.format(
            "%d.%d.%d.%d",
            ipAddress and 255,
            ipAddress shr 8 and 255,
            ipAddress shr 16 and 255,
            ipAddress shr 24 and 255
        )
        return ip
    }
}