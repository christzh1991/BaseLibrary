package com.zc.baselibrary.cache

import android.app.Application
import com.hh.hlibrary.cache.cache.CacheManager
import io.reactivex.Observable
import java.lang.reflect.Type

/**
 * 使用此类进行缓存的增删等操作
 * @author zhangchao
 * @date  2020年12月3日
 */
class RxCache private constructor() {
    private var mCacheManagerBuilder: CacheManager.Builder? = null
    private var mCacheManager: CacheManager? = null

    //高级初始化
    class Builder {
        private val builder: CacheManager.Builder?
        fun setDebug(debug: Boolean): Builder? {
            LogUtil.setDebug(debug)
            return this
        }

        fun setMemoryCacheSizeByMB(memoryCacheSizeByMB: Int): Builder? {
            require(memoryCacheSizeByMB > 0) { "MemoryCacheSizeByMB < 0." }
            builder.setMemoryCacheSizeByMB(memoryCacheSizeByMB)
            return this
        }

        fun setDiskCacheSizeByMB(diskCacheSizeByMB: Int): Builder? {
            require(diskCacheSizeByMB > 0) { "DiskCacheSizeByMB < 0." }
            builder.setDiskCacheSizeByMB(diskCacheSizeByMB)
            return this
        }

        fun setDiskDirName(diskDirName: String?): Builder? {
            builder.setDiskDirName(
                Utilities.checkNullOrEmpty(
                    diskDirName,
                    "diskDirName is null or empty."
                )
            )
            return this
        }

        fun setConverter(converter: IConverter?): Builder? {
            builder.setConverter(Utilities.checkNotNull(converter, "converter is null."))
            return this
        }

        fun setCacheMode(cacheMode: CacheMode?): Builder? {
            builder.setCacheMode(Utilities.checkNotNull(cacheMode, "cacheMode is null."))
            return this
        }

        fun build(): RxCache? {
            getInstance().mCacheManagerBuilder = builder
            return getInstance()
        }

        init {
            assertInit()
            builder = Builder(mContext)
        }
    }
    //sets
    /**
     * 设置缓存的模式
     * 提供四种模式：无缓存，仅内存，仅磁盘，和双缓存
     *
     * @param cacheMode
     * @return
     */
    fun setCacheMode(cacheMode: CacheMode?): RxCache? {
        mCacheManager = getCacheManagerBuilder().setCacheMode(cacheMode).build()
        return this
    }

    /**
     * 可以设置磁盘缓存的文件夹名称
     *
     * @param diskDirName
     * @return
     */
    fun setDiskDirName(diskDirName: String?): RxCache? {
        Utilities.checkNullOrEmpty(diskDirName, "diskDirName is null or empty")
        mCacheManager = getCacheManagerBuilder().setDiskDirName(diskDirName).build()
        return this
    }

    /**
     * 以MB为单位，设置磁盘缓存的大小，一般100M以内就可以
     *
     * @param diskCacheSizeByMB
     * @return
     */
    fun setDiskCacheSizeByMB(diskCacheSizeByMB: Int): RxCache? {
        require(diskCacheSizeByMB >= 0) { "diskCacheSize < 0." }
        mCacheManager = getCacheManagerBuilder().setDiskCacheSizeByMB(diskCacheSizeByMB).build()
        return this
    }

    /**
     * 以MB为单位，设置内存缓存的大小，默认为可用内存大小的1/8
     *
     * @param memoryCacheSizeByMB
     * @return
     */
    fun setMemoryCacheSizeByMB(memoryCacheSizeByMB: Int): RxCache? {
        require(memoryCacheSizeByMB >= 0) { "memoryCacheSize < 0." }
        mCacheManager = getCacheManagerBuilder().setMemoryCacheSizeByMB(memoryCacheSizeByMB).build()
        return this
    }

    /**
     * 设置转换器，默认会使用Gson进行转换，也可以使用序列化以及自定义的转换，只需实现IConverter这个接口
     *
     * @param converter
     * @return
     */
    fun setConverter(converter: IConverter?): RxCache? {
        mCacheManager = getCacheManagerBuilder().setConverter(
            Utilities.checkNotNull(
                converter,
                "converter is null."
            )
        ).build()
        return this
    }

    /**
     * 获取转换器
     *
     * @return
     */
    fun getConverter(): IConverter? {
        return getCacheManager().getConverter()
    }

    /**
     * 获取缓存模式
     *
     * @return
     */
    fun getCacheMode(): CacheMode? {
        return getCacheManager().getCacheMode()
    }

    /**
     * 获取磁盘缓存的大小
     *
     * @return
     */
    fun getDiskCacheSizeByMB(): Int {
        return getCacheManager().getDiskCacheSizeByMB()
    }

    /**
     * 获取内存缓存的大小
     *
     * @return
     */
    fun getMemoryCacheSizeByMB(): Int {
        return getCacheManager().getMemoryCacheSizeByMB()
    }

    /**
     * 获取磁盘缓存的文件夹名称
     *
     * @return
     */
    fun getDiskDirName(): String? {
        return getCacheManager().getDiskDirName()
    }

    private fun getCacheManagerBuilder(): CacheManager.Builder? {
        if (mCacheManagerBuilder == null) mCacheManagerBuilder = Builder(mContext)
        return mCacheManagerBuilder
    }

    private fun getCacheManager(): CacheManager? {
        if (mCacheManager == null) mCacheManager = getCacheManagerBuilder().build()
        return mCacheManager
    }
    //method for use
    /**
     * 写入缓存
     *
     * @param key
     * @param data
     * @param cacheTime
     * @param <T>
     * @return
    </T> */
    fun <T> put(key: String?, data: T?, cacheTime: Long): Observable<Boolean?>? {
        return getCacheManager().saveLocal(data, key, cacheTime)
    }

    /**
     * 读取缓存
     * 若使用Gson转换，需要使用此类， 传递数据的class或者type
     *
     * @param key
     * @param update
     * @param clazz
     * @param <T>
     * @return
    </T> */
    operator fun <T> get(
        key: String?,
        update: Boolean,
        clazz: Class<T?>?
    ): Observable<CacheResponse<T?>?>? {
        return getCacheManager().get(key, update, clazz)
    }

    /**
     * 读取缓存
     * 若使用Gson转换，需要使用此类， 传递数据的class或者type
     *
     * @param key
     * @param update
     * @param type
     * @param <T>
     * @return
    </T> */
    operator fun <T> get(
        key: String?,
        update: Boolean,
        type: Type?
    ): Observable<CacheResponse<T?>?>? {
        return getCacheManager().get(key, update, type)
    }

    /**
     * 如果是Gson方式，clazz传NULL可能会出错，此方法是用于序列化方式的
     *
     * @param key
     * @param update
     * @param <T>
     * @return
    </T> */
    operator fun <T> get(key: String?, update: Boolean): Observable<CacheResponse<T?>?>? {
        return get<Any?>(key, update, null)
    }

    /**
     * 根据键值，删除缓存的数据
     *
     * @param key
     * @return
     */
    fun remove(key: String?): Observable<Boolean?>? {
        return getCacheManager().remove(key)
    }

    fun remove(vararg keys: String?): Observable<Boolean?>? {
        return getCacheManager().remove(keys)
    }

    /**
     * 清空缓存
     *
     * @return
     */
    fun clear(): Observable<Boolean?>? {
        return getCacheManager().clear()
    }

    companion object {
        private var mContext: Application? = null
        fun init(context: Application?) {
            mContext = Utilities.checkNotNull(context, "context is null.")
        }

        private fun assertInit() {
            Utilities.checkNotNull(mContext, "context is null, you need call init() first.")
        }

        //获取实例
        private var instance: RxCache? = null
        fun getInstance(): RxCache? {
            if (instance == null) {
                synchronized(RxCache::class.java) {
                    if (instance == null) {
                        instance = RxCache()
                    }
                }
            }
            return instance
        }
    }
}