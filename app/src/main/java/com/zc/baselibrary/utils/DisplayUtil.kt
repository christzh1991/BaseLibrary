package com.zc.baselibrary.utils

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager
import java.lang.ref.WeakReference

/**
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
object DisplayUtil {

    private val weakReference: WeakReference<Context?>? = null

    private fun getDisplayMetrics(context: Context): DisplayMetrics {
        val manager: WindowManager =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val metrics: DisplayMetrics = DisplayMetrics()
        manager.defaultDisplay.getMetrics(metrics)
        return metrics
    }

    fun getScreenWidth(context: Context): Int {
        return getDisplayMetrics(context).widthPixels ?: 0
    }

    fun getScreenHeight(context: Context): Int {
        return getDisplayMetrics(context).heightPixels ?: 0
    }

    fun dp(value: Float, res: Resources): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value,
            res.displayMetrics
        )
    }

    fun dip2px(context: Context, dipValue: Float): Int {
        val scale: Float = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    fun sp2px(spValue: Float, context: Context): Int {
        val fontScale: Float = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }
}