package com.zc.baselibrary.utils

import android.annotation.TargetApi
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build.VERSION

/**
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
object CompatUtils {

    private val DEBUG: Boolean = false
    private val TAG: String = "CompatUtils"

    fun hasMarsh(): Boolean {
        return VERSION.SDK_INT >= 23
    }

    fun hasOreo(): Boolean {
        return VERSION.SDK_INT >= 26
    }

    fun hasQ(): Boolean {
        return VERSION.SDK_INT >= 29
    }

    fun hasR(): Boolean {
        return VERSION.SDK_INT >= 30
    }

    fun hasJellyBean(): Boolean {
        return VERSION.SDK_INT >= 16
    }

    fun getBitmapSize(bitmap: Bitmap): Int {
        return bitmap.rowBytes * bitmap.height
    }

    fun getBitmapSize(value: BitmapDrawable): Int {
        val bitmap: Bitmap = value.bitmap
        return bitmap.byteCount
    }
}