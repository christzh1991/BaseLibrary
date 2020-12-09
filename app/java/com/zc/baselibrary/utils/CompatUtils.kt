package com.hh.hlibrary.utils

import android.annotation.TargetApi

/**
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
object CompatUtils {
    private val DEBUG: Boolean = false
    private val TAG: String? = "CompatUtils"
    fun hasFroyo(): Boolean {
        return VERSION.SDK_INT >= 8
    }

    fun hasGingerbread(): Boolean {
        return VERSION.SDK_INT >= 9
    }

    fun hasHoneycomb(): Boolean {
        return VERSION.SDK_INT >= 11
    }

    fun hasHoneycombMR1(): Boolean {
        return VERSION.SDK_INT >= 12
    }

    fun hasJellyBean(): Boolean {
        return VERSION.SDK_INT >= 16
    }

    fun disableConnectionReuseIfNecessary() {
        if (hasHttpConnectionBug()) {
            System.setProperty("http.keepAlive", "false")
        }
    }

    fun hasHttpConnectionBug(): Boolean {
        return VERSION.SDK_INT < 8
    }

    @TargetApi(12)
    fun getBitmapSize(bitmap: Bitmap?): Int {
        return bitmap.getRowBytes() * bitmap.getHeight()
    }

    @TargetApi(12)
    fun getBitmapSize(value: BitmapDrawable?): Int {
        val bitmap: Bitmap? = value.getBitmap()
        return if (hasHoneycombMR1()) bitmap.getByteCount() else bitmap.getRowBytes() * bitmap.getHeight()
    }
}