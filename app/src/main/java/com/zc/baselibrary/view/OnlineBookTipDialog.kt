package com.zc.baselibrary.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.hh.hlibrary.R
import com.zc.baselibrary.R
import com.zc.baselibrary.utils.StringUtils

/**
 *
 * @author Zhangchao
 * @date 2017/2/14
 */
class CustomDialog : Dialog, View.OnClickListener {
    private var callback: ClickCallBack? = null
    private var title: TextView? = null
    private var content: TextView? = null
    private var content2: TextView? = null
    private var mContext: Context? = null
    private var isOnTouchOutsideExit = false
    private var mStrConfirm: String? = null
    private var mStrCancel: String? = ""

    constructor(context: Context) : super(context, R.style.loading_dialog) {
        mContext = context
    }

    constructor(context: Context, isOnTouchOutsideExit: Boolean) : super(
        context,
        R.style.loading_dialog
    ) {
        mContext = context
        this.isOnTouchOutsideExit = isOnTouchOutsideExit
    }

    constructor(
        context: Context,
        callback: ClickCallBack?,
        strcancel: String?,
        strconfirm: String?
    ) : super(context, R.style.loading_dialog) {
        this.callback = callback
        mContext = context
        mStrConfirm = strconfirm
        mStrCancel = strcancel
    }

    constructor(context: Context, theme: Int) : super(context, theme) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.dialog_onlinebooktip)
        val dialogConfirm: TextView = findViewById<View?>(R.id.dialog_confirm) as TextView
        val dialogCancel: TextView = findViewById<View?>(R.id.dialog_cancel) as TextView
        title = findViewById<View?>(R.id.title) as TextView
        content = findViewById<View?>(R.id.content) as TextView
        content2 = findViewById<View?>(R.id.content2) as TextView
        val divider = findViewById<View?>(R.id.dialog_divider)
        dialogConfirm.setText(mStrConfirm)
        dialogCancel.setText(mStrCancel)
        if (StringUtils.isEmpty(mStrConfirm)) {
            dialogConfirm.setVisibility(View.GONE)
            divider.visibility = View.GONE
            dialogCancel.setBackgroundResource(R.drawable.bg_recycle_last)
        }
        if (StringUtils.isEmpty(mStrCancel)) {
            dialogCancel.setVisibility(View.GONE)
            divider.visibility = View.GONE
            dialogConfirm.setBackgroundResource(R.drawable.bg_recycle_last)
        }
        dialogCancel.setOnClickListener(this)
        dialogConfirm.setOnClickListener(this)
        setCanceledOnTouchOutside(isOnTouchOutsideExit)
    }

    /**
     * 设置点击按钮的回调
     *
     * @param callback
     */
    fun setClickListen(callback: ClickCallBack?) {
        this.callback = callback
    }

    /**
     * 设置提示文字
     *
     * @param message，str_content，str_content2
     */
    fun setUpdateMessage(message: String?, str_content: String?, str_content2: String?) {
        title.setText(message)
        if (!StringUtils.isEmpty(str_content)) {
            content.setText(str_content)
            content.setVisibility(View.VISIBLE)
        } else {
            content.setVisibility(View.GONE)
        }
        if (!StringUtils.isEmpty(str_content2)) {
            content2.setText(str_content2)
            content2.setVisibility(View.VISIBLE)
        } else {
            content2.setVisibility(View.GONE)
        }
    }

    override fun onClick(v: View?) {
        if (v.getId() == R.id.dialog_confirm) {
            if (callback != null) {
                cancel()
                callback.clickListen(1)
            }
        } else if (v.getId() == R.id.dialog_cancel) {
            callback.clickListen(0)
            cancel()
        }
    }

    /**
     * 点击按钮回调
     * type  1:确定  2：取消
     */
    interface ClickCallBack {
        /**
         * 确定，取消点击事件处理
         */
        open fun clickListen(type: Int)
    }

    override fun onBackPressed() {
        if (isOnTouchOutsideExit) {
            super.onBackPressed()
        } else {
        }
    }
}