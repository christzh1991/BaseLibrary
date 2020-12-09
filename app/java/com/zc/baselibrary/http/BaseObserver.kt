package com.hh.hlibrary.http

import android.util.Log

/**
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
abstract class BaseObserver<T> constructor(zLoadingDialog: SimpleLoadDialog?) :
    Observer<BaseEntity<T?>?> {
    private val zLoadingDialog: SimpleLoadDialog?
    private var disposable: Disposable? = null
    @Override
    fun onSubscribe(@NonNull d: Disposable?) {
        disposable = d
    }

    @Override
    fun onNext(@NonNull tBaseEntity: BaseEntity<T?>?) {
        try {
            if (tBaseEntity.isSuccess()) {
                val t: T? = tBaseEntity.getData()
                onHandleSuccess(tBaseEntity.getCode(), t)
            } else {
                val msg: String? =
                    if (StringUtils.isEmpty(tBaseEntity.getMsg())) "" else tBaseEntity.getMsg()
                HLogUtils.d(tBaseEntity.getCode() + msg)
                onHandleError(tBaseEntity.getCode(), msg)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("error", "" + e.getMessage())
        }
    }

    @Override
    fun onError(@NonNull e: Throwable?) {
        e.printStackTrace()
        dismiss()
    }

    @Override
    fun onComplete() {
        dismiss()
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose()
        }
    }

    private fun dismiss() {
        if (zLoadingDialog != null) {
            zLoadingDialog.dismiss()
        }
    }

    protected abstract fun onHandleSuccess(code: Int, t: T?)
    protected fun onHandleError(code: Int, msg: String?) {
//        ToastUtils.toastShow(msg);
    }

    init {
        this.zLoadingDialog = zLoadingDialog
    }
}