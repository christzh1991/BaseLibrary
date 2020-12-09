package com.hh.hlibrary.cache.converter

import com.hh.hlibrary.cache.entity.RealEntity

/**
 * @author zhangchao
 * @since 2020年12月3日
 */
open interface IConverter<T> {
    open fun encode(data: RealEntity<T?>?): ByteArray?

    //type为实际数据的type，只有GSON解析时才需要使用
    open fun decode(cacheData: ByteArray?, type: Type?): RealEntity<T?>?
}