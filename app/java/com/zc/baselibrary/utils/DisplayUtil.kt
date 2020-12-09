package com.hh.hlibrary.utils

import android.content.Context

/**
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
class DisplayUtil constructor() {
    private val weakReference: WeakReference<Context?>? = null

    companion object {
        fun getDisplayMetrics(context: Context?): DisplayMetrics? {
            val manager: WindowManager? =
                context.getSystemService(Context.WINDOW_SERVICE) as WindowManager?
            val metrics: DisplayMetrics? = DisplayMetrics()
            manager.getDefaultDisplay().getMetrics(metrics)
            return metrics
        }

        fun getScreenWidth(context: Context?): Int {
            return getDisplayMetrics(context).widthPixels
        }

        fun getScreenHegiht(context: Context?): Int {
            return getDisplayMetrics(context).heightPixels
        }

        fun dp(value: Float, res: Resources?): Float {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                value,
                res.getDisplayMetrics()
            )
        }

        fun dip2px(context: Context?, dipValue: Float): Int {
            val scale: Float = context.getResources().getDisplayMetrics().density
            return (dipValue * scale + 0.5f) as Int
        }

        /**
         * 将sp值转换为px值，保证文字大小不变
         *
         * @param spValue
         * @return
         */
        fun sp2px(spValue: Float, context: Context?): Int {
            val fontScale: Float = context.getResources().getDisplayMetrics().scaledDensity
            return (spValue * fontScale + 0.5f) as Int
        }
    }
}