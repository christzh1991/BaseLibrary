package com.hh.hlibrary.cache.cache

import android.util.LruCache

/**
 * 内存缓存实现类
 *
 * @author zhangchao
 * @since 2020年12月3日
 */
class MemoryCache constructor(cacheSizeByMb: Int) : ICache {
    private val lruCache: LruCache<String?, ByteArray?>?
    @Override
    fun put(key: String?, data: ByteArray?): Boolean {
        var key: String? = key
        Utilities.checkNullOrEmpty(key, "key is null or empty.")
        key = Utilities.Md5(key)
        if (lruCache == null) return false
        try {
            lruCache.put(key, data)
            LogUtil.i("MemoryCache save success!")
            return true
        } catch (e: Exception) {
            LogUtil.t(e)
        }
        return false
    }

    @Override
    operator fun get(key: String?, update: Boolean): ByteArray? {
        var key: String? = key
        Utilities.checkNullOrEmpty(key, "key is null or empty.")
        key = Utilities.Md5(key)
        if (update) {
            remove(key)
            return null
        }
        if (lruCache == null) {
            return null
        }
        try {
            LogUtil.i("MemoryCache get success!")
            return lruCache.get(key)
        } catch (e: Exception) {
            LogUtil.t(e)
        }
        return null
    }

    @Override
    operator fun contains(key: String?): Boolean {
        var key: String? = key
        Utilities.checkNullOrEmpty(key, "key is null or empty.")
        key = Utilities.Md5(key)
        return lruCache.get(key) != null
    }

    @Override
    fun remove(key: String?): Boolean {
        var key: String? = key
        Utilities.checkNullOrEmpty(key, "key is null or empty.")
        key = Utilities.Md5(key)
        if (lruCache == null) return false
        try {
            return lruCache.remove(key) != null
        } catch (e: Exception) {
            LogUtil.t(e)
        }
        return false
    }

    @Override
    fun clear(): Boolean {
        if (lruCache == null) return false
        try {
            lruCache.evictAll()
            return true
        } catch (e: Exception) {
            LogUtil.t(e)
        }
        return false
    }

    init {
        if (cacheSizeByMb <= 0) throw IllegalArgumentException("memoryCacheSize must > 0.")
        lruCache = LruCache(cacheSizeByMb * 1024 * 1024)
    }
}