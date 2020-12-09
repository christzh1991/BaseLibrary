package com.zc.baselibrary.cache.converter

import com.zc.baselibrary.cache.entity.RealEntity
import java.lang.reflect.Type

/**
 * @author zhangchao
 * @since 2020年12月3日
 */
interface IConverter {
    fun encode(data: RealEntity<Any>): ByteArray?

    //type为实际数据的type，只有GSON解析时才需要使用
    fun decode(cacheData: ByteArray?, type: Type?): RealEntity<Any>
}