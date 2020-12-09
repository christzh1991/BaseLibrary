package com.hh.hlibrary.cache.converter

import com.hh.hlibrary.cache.entity.RealEntity

/**
 *
 * 描述：序列化对象的转换器
 * 1.使用改转换器，对象&对象中的其它所有对象都必须是要实现Serializable接口（序列化）<br></br>
 * 优点：<br></br>
 * 速度快<br></br>
 * @author zhangchao
 * @date  2020年12月3日
 */
class SerializableConverter constructor() : IConverter {
    @Override
    fun encode(data: RealEntity?): ByteArray? {
        return SerializeUtil.serialize(data)
    }

    @Override
    fun decode(cacheData: ByteArray?, type: Type?): RealEntity? {
        return SerializeUtil.unserialize(cacheData) as RealEntity?
    }
}