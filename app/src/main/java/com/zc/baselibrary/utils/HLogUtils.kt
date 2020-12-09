package com.zc.baselibrary.utils

import com.orhanobut.logger.Logger

/**
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
object HLogUtils {
    private var isDebug: Boolean = false

    fun init(debug: Boolean) {
        isDebug = debug
    }

    fun d(objectLog: Any?) {
        if (isDebug) {
            Logger.d(objectLog)
        }
    }

    fun d(message: String, objectLog: Any?) {
        if (isDebug) {
            Logger.d(message, objectLog)
        }
    }

    fun json(json: String?) {
        if (isDebug) {
            Logger.json(json)
        }
    }

    fun xml(xml: String?) {
        if (isDebug) {
            Logger.xml(xml)
        }
    }
}