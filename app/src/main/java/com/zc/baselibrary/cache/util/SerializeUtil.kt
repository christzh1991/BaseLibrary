package com.zc.baselibrary.cache.util

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

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
    fun serialize(`object`: Any?): ByteArray? {
        var oos: ObjectOutputStream? = null
        var baos: ByteArrayOutputStream? = null
        try {
            // 序列化
            baos = ByteArrayOutputStream()
            oos = ObjectOutputStream(baos)
            oos.writeObject(`object`)
            return baos.toByteArray()
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
    fun unserialize(bytes: ByteArray?): Any? {
        var bais: ByteArrayInputStream? = null
        try {
            // 反序列化
            bais = ByteArrayInputStream(bytes)
            val ois = ObjectInputStream(bais)
            return ois.readObject()
        } catch (e: Exception) {
            LogUtil.t(e)
        }
        return null
    }
}