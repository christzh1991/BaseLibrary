package com.zc.baselibrary.cache.entity

import java.io.Serializable

/**
 * 实际缓存的类，将传入的data包裹在此类下，用以设置缓存时长等
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
class RealEntity<T>(//实际需要缓存的数据
    private var datas: T?, //缓存的时间，以ms为单位
    private var cacheTime: Long
) : Serializable {
    //缓存开始的时间
    private var updateDate: Long = 0
    fun getCacheTime(): Long {
        return cacheTime
    }

    fun setCacheTime(cacheTime: Long) {
        this.cacheTime = cacheTime
    }

    fun getDatas(): T? {
        return datas
    }

    fun setDatas(datas: T?) {
        this.datas = datas
    }

    fun getUpdateDate(): Long {
        return updateDate
    }

    fun setUpdateDate(updateDate: Long) {
        this.updateDate = updateDate
    }
}