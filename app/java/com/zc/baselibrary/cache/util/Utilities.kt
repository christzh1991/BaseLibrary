package com.hh.hlibrary.cache.util

import java.io.ByteArrayOutputStream

/**
 * @author zhangchao
 * @date  2020年12月3日
 */
object Utilities {
    fun <T> checkNotNull(t: T?, throwStr: String?): T? {
        if (t == null) {
            throw NullPointerException(throwStr)
        }
        return t
    }

    fun checkNullOrEmpty(s: String?, throwStr: String?): String? {
        if (s == null || s.isEmpty()) {
            throw IllegalArgumentException(throwStr)
        }
        return s
    }

    @Throws(IOException::class)
    fun input2byte(inStream: InputStream?): ByteArray? {
        val swapStream: ByteArrayOutputStream? = ByteArrayOutputStream()
        val buff: ByteArray? = ByteArray(100)
        var rc: Int = 0
        while ((inStream.read(buff, 0, 100).also({ rc = it })) > 0) {
            swapStream.write(buff, 0, rc)
        }
        val in2b: ByteArray? = swapStream.toByteArray()
        return in2b
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
        var cacheKey: String?
        try {
            val mDigest: MessageDigest? = MessageDigest.getInstance("MD5")
            mDigest.update(key.getBytes())
            cacheKey = bytesToHexString(mDigest.digest())
        } catch (e: NoSuchAlgorithmException) {
            cacheKey = String.valueOf(key.hashCode())
        }
        return cacheKey
    }

    private fun bytesToHexString(bytes: ByteArray?): String? {
        val sb: StringBuilder? = StringBuilder()
        for (i in bytes.indices) {
            val hex: String? = Integer.toHexString(0xFF and bytes.get(i))
            if (hex.length() === 1) {
                sb.append('0')
            }
            sb.append(hex)
        }
        return sb.toString()
    }
}