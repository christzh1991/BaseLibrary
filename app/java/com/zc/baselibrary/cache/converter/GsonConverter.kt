package com.hh.hlibrary.cache.converter

import com.google.gson.Gson

/**
 * 使用Gson进行数据转换
 * 因频繁转换有一定的性能问题，但是基本可以忽略
 *
 * @author zhangchao
 * @since 2020年12月3日
 */
class GsonConverter<T> : IConverter<T?> {
    private var gson: Gson?

    constructor(gson: Gson?) {
        Utilities.checkNotNull(gson, "gson is null.")
        this.gson = gson
    }

    constructor() {
        gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd hh:mm:ss")
            .create()
    }

    @Override
    fun encode(data: RealEntity<T?>?): ByteArray? {
        val dataStr: String? = gson.toJson(data)
        return dataStr.getBytes()
    }

    @Override
    fun decode(cacheData: ByteArray?, type: Type?): RealEntity<T?>? {
        Utilities.checkNotNull(type, "type is null.")
        val dataStr: String? = String(cacheData)
        val objType: Type? = type(RealEntity::class.java, type)
        val data: RealEntity<T?>? = gson.fromJson(dataStr, objType)
        return data
    }

    private fun type(raw: Class?, vararg args: Type?): ParameterizedType? {
        return object : ParameterizedType() {
            fun getRawType(): Type? {
                return raw
            }

            fun getActualTypeArguments(): Array<Type?>? {
                return args
            }

            fun getOwnerType(): Type? {
                return null
            }
        }
    }
}