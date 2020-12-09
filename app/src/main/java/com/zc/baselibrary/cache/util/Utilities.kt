package com.zc.baselibrary.cache.util

import java.io.ByteArrayOutputStream
import java.io.Closeable
import java.io.IOException
import java.io.InputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * @author zhangchao
 * @date  2020年12月3日
 */
object Utilities {
    fun <T> checkNotNull(t: T?, throwStr: String?): T {
        if (t == null) {
            throw NullPointerException(throwStr)
        }
        return t
    }

    fun checkNullOrEmpty(s: String?, throwStr: String?): String {
        if (s == null || s.isEmpty()) {
            throw IllegalArgumentException(throwStr)
        }
        return s
    }

    @Throws(IOException::class)
    fun input2byte(inStream: InputStream?): ByteArray? {
        val swapStream = ByteArrayOutputStream()
        val buff = ByteArray(100)
        var rc = 0
        while (inStream!!.read(buff, 0, 100).also { rc = it } > 0) {
            swapStream.write(buff, 0, rc)
        }
        return swapStream.toByteArray()
    }

    fun closeQuietly(closeable: Closeable?) {
        if (closeable == null) return
        try {
            closeable.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 使用MD5算法对传入的key进行加密并返回。
     */
    fun Md5(key: String?): String? {
        val cacheKey: String?
        cacheKey = try {
            val mDigest = MessageDigest.getInstance("MD5")
            mDigest.update(key!!.toByteArray())
            bytesToHexString(mDigest.digest())
        } catch (e: NoSuchAlgorithmException) {
            key.hashCode().toString()
        }
        return cacheKey
    }

    private fun bytesToHexString(bytes: ByteArray?): String? {
        val sb = StringBuilder()
        for (i in bytes!!.indices) {
            val hex = Integer.toHexString(0xFF and bytes[i].toInt())
            if (hex.length == 1) {
                sb.append('0')
            }
            sb.append(hex)
        }
        return sb.toString()
    }
}