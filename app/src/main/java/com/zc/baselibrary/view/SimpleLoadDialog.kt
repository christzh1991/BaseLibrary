package com.zc.baselibrary.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.os.Message
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import com.zc.baselibrary.R
import java.lang.ref.WeakReference

/**
 * @author zhangchao
 * @date  2020年12月3日
 */
class SimpleLoadDialog(
    context: Context?,
    cancelable: Boolean
) : Handler() {

    companion object {
        const val SHOW_PROGRESS_DIALOG = 1
        const val DISMISS_PROGRESS_DIALOG = 2
    }

    private var load: Dialog? = null
    private var context: Context? = null
    private val cancelable: Boolean
    private val reference: WeakReference<Context?>?
    private fun create() {
        if (load == null) {
            context = reference?.get()
            if(context != null){
                load = Dialog(context!!, R.style.loadstyle)
                val dialogView: View = LayoutInflater.from(context).inflate(
                    R.layout.custom_sload_layout, null
                )
                load!!.setCanceledOnTouchOutside(false)
                load!!.setCancelable(cancelable)
                load!!.setContentView(dialogView)
                val dialogWindow = load!!.window
                dialogWindow?.setGravity(
                    Gravity.CENTER_VERTICAL
                            or Gravity.CENTER_HORIZONTAL
                )
            }

        }
        if (load?.isShowing != true && context != null) {
            load?.show()
        }
    }

    fun show() {
        create()
    }

    fun dismiss() {
        context = reference?.get()
        if (load != null && load!!.isShowing && !(context as Activity?)!!.isFinishing) {
            val name = Thread.currentThread().name
            load!!.dismiss()
            load = null
        }
    }

    override fun handleMessage(msg: Message) {
        when (msg.what) {
            SHOW_PROGRESS_DIALOG -> create()
            DISMISS_PROGRESS_DIALOG -> dismiss()
        }
    }

    init {
        reference = WeakReference(context)
        this.cancelable = cancelable
    }
}