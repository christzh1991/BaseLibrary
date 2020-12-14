package com.zc.baselibrary.cache.converter

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.zc.baselibrary.cache.entity.RealEntity
import com.zc.baselibrary.cache.util.Utilities
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * 使用Gson进行数据转换
 * 因频繁转换有一定的性能问题，但是基本可以忽略
 *
 * @author zhangchao
 * @since 2020年12月3日
 */
class GsonConverter : IConverter {
    private var mGson: Gson?

    constructor(gson: Gson?) {
        Utilities.checkNotNull(gson, "gson is null.")
        this.mGson = gson
    }

    constructor() {
        mGson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd hh:mm:ss")
            .create()
    }

    override fun encode(data: RealEntity<Any>): ByteArray {
        val dataStr: String = mGson!!.toJson(data)
        return dataStr.toByteArray()
    }

    override fun decode(cacheData: ByteArray?, type: Type?): RealEntity<Any> {
        Utilities.checkNotNull(type, "type is null.")
        val dataStr = cacheData?.let { String(it) }
        val objType: Type = type(RealEntity::class.java, type)
        return mGson!!.fromJson(dataStr, objType)
    }

    private fun type(raw: Class<*>?, vararg args: Type?): ParameterizedType {
        return object : ParameterizedType {
            override fun getRawType(): Type? {
                return raw
            }

            override fun getActualTypeArguments(): Array<out Type?> {
                return args
            }

            override fun getOwnerType(): Type? {
                return null
            }
        }
    }

}