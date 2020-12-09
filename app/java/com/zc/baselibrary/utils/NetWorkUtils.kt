package com.hh.hlibrary.utils

import android.app.Application

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
            context.get().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val networkInfo: NetworkInfo? = cm.getActiveNetworkInfo()
        return networkInfo != null && networkInfo.isAvailable()
    }
}