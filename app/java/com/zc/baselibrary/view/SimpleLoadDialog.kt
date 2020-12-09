package com.hh.hlibrary.view

import android.app.Activity

/**
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
class SimpleLoadDialog constructor(
    context: Context?,
    cancelable: Boolean
) : Handler() {
    private var load: Dialog? = null
    private var context: Context? = null
    private val cancelable: Boolean
    private val reference: WeakReference<Context?>?
    private fun create() {
        if (load == null) {
            context = reference.get()
            load = Dialog(context, R.style.loadstyle)
            val dialogView: View? = LayoutInflater.from(context).inflate(
                R.layout.custom_sload_layout, null
            )
            load.setCanceledOnTouchOutside(false)
            load.setCancelable(cancelable)
            load.setContentView(dialogView)
            val dialogWindow: Window? = load.getWindow()
            dialogWindow.setGravity(
                (Gravity.CENTER_VERTICAL
                        or Gravity.CENTER_HORIZONTAL)
            )
        }
        if (!load.isShowing() && context != null) {
            load.show()
        }
    }

    fun show() {
        create()
    }

    fun dismiss() {
        context = reference.get()
        if ((load != null) && load.isShowing() && !(context as Activity?).isFinishing()) {
            val name: String? = Thread.currentThread().getName()
            load.dismiss()
            load = null
        }
    }

    @Override
    fun handleMessage(msg: Message?) {
        when (msg.what) {
            SHOW_PROGRESS_DIALOG -> create()
            DISMISS_PROGRESS_DIALOG -> dismiss()
        }
    }

    companion object {
        val SHOW_PROGRESS_DIALOG: Int = 1
        val DISMISS_PROGRESS_DIALOG: Int = 2
    }

    init {
        reference = WeakReference<Context?>(context)
        this.cancelable = cancelable
    }
}