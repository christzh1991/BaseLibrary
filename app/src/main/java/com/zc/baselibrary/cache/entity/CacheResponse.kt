package com.zc.baselibrary.cache.entity

/**
 * Rx2.x不支持NULL，所以使用此类对返回数据包装
 * @author zhangchao
 * @date  2020年12月3日
 */
class CacheResponse<T> {
    private var data: T? = null
    fun getData(): T? {
        return data
    }

    fun setData(data: T?) {
        this.data = data
    }
}