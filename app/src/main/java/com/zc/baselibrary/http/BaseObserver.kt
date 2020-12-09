package com.zc.baselibrary.http

import android.util.Log
import com.zc.baselibrary.utils.HLogUtils
import com.zc.baselibrary.utils.StringUtils
import com.zc.baselibrary.view.SimpleLoadDialog
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
abstract class BaseObserver<T>(zLoadingDialog: SimpleLoadDialog?) : Observer<BaseEntity<T?>?> {

    private val zLoadingDialog: SimpleLoadDialog?
    private var disposable: Disposable? = null

    override fun onSubscribe(d: Disposable) {
        disposable = d
    }

    override fun onNext(tBaseEntity: BaseEntity<T?>) {
        try {
            if (tBaseEntity.isSuccess()) {
                val t = tBaseEntity.data
                onHandleSuccess(tBaseEntity.code, t)
            } else {
                val msg = if (StringUtils.isEmpty(tBaseEntity.message)) "" else tBaseEntity.message
                HLogUtils.d(tBaseEntity.code.toString() + msg)
                onHandleError(tBaseEntity.code, msg)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("error", "" + e.message)
        }
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        dismiss()
    }

    override fun onComplete() {
        dismiss()
        if (disposable != null && !disposable!!.isDisposed) {
            disposable!!.dispose()
        }
    }

    private fun dismiss() {
        zLoadingDialog?.dismiss()
    }

    protected abstract fun onHandleSuccess(code: Int, t: T?)

    protected fun onHandleError(code: Int, msg: String?) {
//        ToastUtils.toastShow(msg);
    }

    init {
        this.zLoadingDialog = zLoadingDialog
    }
}