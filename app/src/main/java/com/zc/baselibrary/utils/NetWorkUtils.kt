package com.zc.baselibrary.utils

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import java.lang.ref.WeakReference

/**
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
object NetWorkUtils {
    private var context: WeakReference<Application?>? = null
    fun init(application: Application?) {
        context = WeakReference(application)
    }

    /**
     * 判断网络是否可用
     */
    fun isNetworkAvailable(): Boolean {
        val cm: ConnectivityManager? =
            context?.get()?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val networkInfo: NetworkInfo? = cm?.activeNetworkInfo
        return networkInfo != null && networkInfo.isAvailable
    }
}