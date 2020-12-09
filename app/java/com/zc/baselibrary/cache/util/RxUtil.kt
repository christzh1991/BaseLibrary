package com.hh.hlibrary.cache.util

import io.reactivex.Observable

/**
 * @author zhangchao
 * @date  2020年12月3日
 */
object RxUtil {
    fun <T> io_main(): ObservableTransformer<T?, T?>? {
        return object : ObservableTransformer<T?, T?>() {
            @Override
            fun apply(upstream: Observable<T?>?): ObservableSource<T?>? {
                return upstream.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            }
        }
    }
}