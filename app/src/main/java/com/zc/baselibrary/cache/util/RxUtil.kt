package com.zc.baselibrary.cache.util

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author zhangchao
 * @date  2020年12月3日
 */
object RxUtil {
    fun <T> ioMain(): ObservableTransformer<T?, T?> {
        return ObservableTransformer<T?, T?> { upstream ->
            upstream.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }
}