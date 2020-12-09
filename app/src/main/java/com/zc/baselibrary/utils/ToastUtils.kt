package com.zc.baselibrary.utils

import android.app.Application
import android.view.Gravity
import android.widget.Toast
import java.lang.ref.WeakReference

/**
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
object ToastUtils {
    private var toast: Toast? = null
    private var toast2: Toast? = null
    private var app: WeakReference<Application?>? = null

    fun init(application: Application?) {
        app = WeakReference(application)
    }

    fun toastShow(text: String?) {
        if (toast == null) {
            toast = Toast.makeText(app?.get(), text, Toast.LENGTH_SHORT)
        } else {
            toast?.setText(text)
            toast?.duration = Toast.LENGTH_SHORT
        }
        toast?.show()
    }

    fun longToastShow(text: String?) {
        if (toast == null) {
            toast = Toast.makeText(app?.get(), text, Toast.LENGTH_LONG)
        } else {
            toast?.setText(text)
            toast?.duration = Toast.LENGTH_LONG
        }
        toast?.show()
    }

    fun showToastCenter(tvStr: String?) {
        if (toast2 == null) {
            toast2 = Toast.makeText(app?.get(), tvStr, Toast.LENGTH_SHORT)
        } else {
            toast2?.setText(tvStr)
        }
        toast2?.setGravity(Gravity.CENTER, 0, 0)
        toast2?.show()
    }
}