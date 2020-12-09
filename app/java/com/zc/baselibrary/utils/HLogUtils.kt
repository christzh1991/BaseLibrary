package com.hh.hlibrary.utils

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

    fun d(`object`: Object?) {
        if (isDebug) {
            Logger.d(`object`)
        }
    }

    fun d(message: String?, `object`: Object?) {
        if (isDebug) {
            Logger.d(message, `object`)
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