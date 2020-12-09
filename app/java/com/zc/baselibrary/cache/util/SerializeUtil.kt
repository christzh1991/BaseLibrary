package com.hh.hlibrary.cache.util

import java.io.ByteArrayInputStream

/**
 * @author zhangchao
 * @date  2020年12月3日
 */
object SerializeUtil {
    /**
     * 序列化
     *
     * @param object
     * @return
     */
    fun serialize(`object`: Object?): ByteArray? {
        var oos: ObjectOutputStream? = null
        var baos: ByteArrayOutputStream? = null
        try {
            // 序列化
            baos = ByteArrayOutputStream()
            oos = ObjectOutputStream(baos)
            oos.writeObject(`object`)
            val bytes: ByteArray? = baos.toByteArray()
            return bytes
        } catch (e: Exception) {
            LogUtil.t(e)
        }
        return null
    }

    /**
     * 反序列化
     *
     * @param bytes
     * @return
     */
    fun unserialize(bytes: ByteArray?): Object? {
        var bais: ByteArrayInputStream? = null
        try {
            // 反序列化
            bais = ByteArrayInputStream(bytes)
            val ois: ObjectInputStream? = ObjectInputStream(bais)
            return ois.readObject()
        } catch (e: Exception) {
            LogUtil.t(e)
        }
        return null
    }
}