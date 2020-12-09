package com.zc.baselibrary

import android.app.Application
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.zc.baselibrary.utils.AppPreferences
import com.zc.baselibrary.utils.ToastUtils

/**
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
object HApplication : Application() {

    fun init() {
        AppPreferences.init(this)
        ToastUtils.init(this)
        val formatStrategy: PrettyFormatStrategy =
            PrettyFormatStrategy.newBuilder().tag("zc").build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
    }

}