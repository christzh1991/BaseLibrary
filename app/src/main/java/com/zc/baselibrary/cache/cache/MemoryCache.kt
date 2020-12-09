package com.zc.baselibrary.cache.cache

import android.util.LruCache
import com.zc.baselibrary.cache.util.LogUtil
import com.zc.baselibrary.cache.util.Utilities

/**
 * 内存缓存实现类
 *
 * @author zhangchao
 * @since 2020年12月3日
 */
class MemoryCache(cacheSizeByMb: Int) : ICache {
    private val lruCache: LruCache<String?, ByteArray?>?
    override fun put(key: String?, data: ByteArray?): Boolean {
        var key = key
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

    override fun get(key: String?, update: Boolean): ByteArray? {
        var key = key
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
            return lruCache[key]
        } catch (e: Exception) {
            LogUtil.t(e)
        }
        return null
    }

    override fun contains(key: String?): Boolean {
        Utilities.checkNullOrEmpty(key, "key is null or empty.")
        val keyTemp: String? = Utilities.Md5(key)
        return lruCache?.get(keyTemp) != null
    }

    override fun remove(key: String?): Boolean {
        var key = key
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

    override fun clear(): Boolean {
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
        require(cacheSizeByMb > 0) { "memoryCacheSize must > 0." }
        lruCache = LruCache(cacheSizeByMb * 1024 * 1024)
    }
}