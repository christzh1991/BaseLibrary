package com.hh.hlibrary

import android.app.Application

/**
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
object HApplication {
    fun init(application: Application?) {
        AppPreferences.init(application)
        ToastUtils.init(application)
        val formatStrategy: PrettyFormatStrategy? =
            PrettyFormatStrategy.newBuilder().tag("zc").build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
    }
}