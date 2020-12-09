package com.zc.baselibrary.cache.util

import android.util.Log

/**
 * @author zhangchao
 * @date  2020年12月3日
 */
object LogUtil {

    private var debug = true
    private var tag: String = "RxCache"

    fun setDebug(debug: Boolean) {
        if (debug) LogUtil.debug = debug
    }

    fun setTag(tag: String) {
        if (debug) LogUtil.tag = tag
    }

    fun d(content: String) {
        if (debug) Log.d(tag, content)
    }

    fun i(content: String) {
        if (debug) Log.i(tag, content)
    }

    fun e(content: String) {
        if (debug) Log.e(tag, content)
    }

    fun v(content: String) {
        if (debug) Log.v(tag, content)
    }

    fun w(content: String) {
        if (debug) Log.w(tag, content)
    }

    fun t(t: Throwable) {
        if (debug) t.printStackTrace()
    }
}