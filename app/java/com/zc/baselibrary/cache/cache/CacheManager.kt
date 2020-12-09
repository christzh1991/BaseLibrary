package com.hh.hlibrary.cache.cache

import android.content.Context

/**
 * 管理缓存
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
class CacheManager private constructor() {
    private var context: Context? = null
    private var memoryCacheSizeByMB: Int = 0
    private var diskCacheSizeByMB: Int = 0
    private var converter: IConverter? = null
    private var diskDirName: String? = null
    private var cacheMode: CacheMode? = null

    class Builder constructor(context: Context?) {
        private val context: Context?

        //最大缓存的1/8
        private var memoryCacheSizeByMB: Int =
            (Runtime.getRuntime().maxMemory() / 8 / 1024 / 1024) as Int
        private var diskCacheSizeByMB: Int = 100
        private var converter: IConverter? = GsonConverter()
        private var diskDirName: String? = "RxCache"
        private var cacheMode: CacheMode? = CacheMode.BOTH
        fun setMemoryCacheSizeByMB(memoryCacheSizeByMB: Int): Builder? {
            if (memoryCacheSizeByMB <= 0) throw IllegalArgumentException("MemoryCacheSizeByMB < 0.")
            this.memoryCacheSizeByMB = memoryCacheSizeByMB
            return this
        }

        fun setDiskCacheSizeByMB(diskCacheSizeByMB: Int): Builder? {
            if (diskCacheSizeByMB <= 0) throw IllegalArgumentException("DiskCacheSizeByMB < 0.")
            this.diskCacheSizeByMB = diskCacheSizeByMB
            return this
        }

        fun setDiskDirName(diskDirName: String?): Builder? {
            this.diskDirName =
                Utilities.checkNullOrEmpty(diskDirName, "diskDirName is null or empty.")
            return this
        }

        fun setConverter(converter: IConverter?): Builder? {
            this.converter = Utilities.checkNotNull(converter, "converter is null.")
            return this
        }

        fun setCacheMode(cacheMode: CacheMode?): Builder? {
            this.cacheMode = Utilities.checkNotNull(cacheMode, "cacheMode is null.")
            return this
        }

        fun build(): CacheManager? {
            val cacheManager: CacheManager? = CacheManager()
            cacheManager.setContext(context)
            cacheManager.setConverter(converter)
            cacheManager.setDiskCacheSizeByMB(diskCacheSizeByMB)
            cacheManager.setDiskDirName(diskDirName)
            cacheManager.setMemoryCacheSizeByMB(memoryCacheSizeByMB)
            cacheManager.setCacheMode(cacheMode)
            when (cacheMode) {
                BOTH -> {
                    mDiskCache = DiskCache(context, diskDirName, diskCacheSizeByMB)
                    mMemoryCache = MemoryCache(memoryCacheSizeByMB)
                }
                ONLY_DISK -> {
                    mDiskCache = DiskCache(context, diskDirName, diskCacheSizeByMB)
                    mMemoryCache = null
                }
                ONLY_MEMORY -> {
                    mDiskCache = null
                    mMemoryCache = MemoryCache(memoryCacheSizeByMB)
                }
                NONE -> {
                    mDiskCache = null
                    mMemoryCache = null
                }
            }
            return cacheManager
        }

        init {
            this.context = Utilities.checkNotNull(context, "context is null.")
        }
    }

    private fun setDiskCacheSizeByMB(diskCacheSizeByMB: Int) {
        this.diskCacheSizeByMB = diskCacheSizeByMB
    }

    private fun setContext(context: Context?) {
        this.context = context
    }

    private fun setCacheMode(cacheMode: CacheMode?) {
        this.cacheMode = cacheMode
    }

    private fun setConverter(converter: IConverter?) {
        this.converter = converter
    }

    private fun setDiskDirName(diskDirName: String?) {
        this.diskDirName = diskDirName
    }

    private fun setMemoryCacheSizeByMB(memoryCacheSizeByMB: Int) {
        this.memoryCacheSizeByMB = memoryCacheSizeByMB
    }

    fun getContext(): Context? {
        return context
    }

    fun getConverter(): IConverter? {
        return converter
    }

    fun getCacheMode(): CacheMode? {
        return cacheMode
    }

    fun getDiskCacheSizeByMB(): Int {
        if (cacheMode === CacheMode.BOTH || cacheMode === CacheMode.ONLY_DISK) return diskCacheSizeByMB
        return 0
    }

    fun getMemoryCacheSizeByMB(): Int {
        if (cacheMode === CacheMode.BOTH || cacheMode === CacheMode.ONLY_MEMORY) return memoryCacheSizeByMB
        return 0
    }

    fun getDiskDirName(): String? {
        if (cacheMode === CacheMode.BOTH || cacheMode === CacheMode.ONLY_DISK) return diskDirName
        return ""
    }

    fun getMemoryCache(): MemoryCache? {
        return mMemoryCache
    }

    fun getDiskCache(): DiskCache? {
        if (mDiskCache == null) return mDiskCache
        if (mDiskCache.isClosed()) {
            mDiskCache = DiskCache(getContext(), getDiskDirName(), getDiskCacheSizeByMB())
        }
        return mDiskCache
    }

    /**
     * 将数据缓存到本地的实现
     */
    fun <T> saveLocal(data: T?, key: String?, cacheTime: Long): Observable<Boolean?>? {
        var cacheTime: Long = cacheTime
        Utilities.checkNotNull(data, "data is null.")
        Utilities.checkNullOrEmpty(key, "key is null or empty.")
        if (cacheTime < -1) cacheTime = -1
        val entity: RealEntity<T?>? = RealEntity<T?>(data, cacheTime)
        entity.setUpdateDate(System.currentTimeMillis())
        val cacheData: ByteArray? = getConverter().encode(entity)
        var result: Boolean = false
        if (getMemoryCache() != null) {
            result = result or getMemoryCache().put(key, cacheData)
        }
        if (getDiskCache() != null) {
            result = result or getDiskCache().put(key, cacheData)
        }
        return Observable.just(result or (if (getCacheMode() === CacheMode.NONE) true else false))
    }

    operator fun <T> get(
        key: String?,
        update: Boolean,
        type: Type?
    ): Observable<CacheResponse<T?>?>? {
        Utilities.checkNullOrEmpty(key, "key is null or empty.")
        return Observable.create(object : ObservableOnSubscribe<CacheResponse<T?>?>() {
            @Override
            @Throws(Exception::class)
            fun subscribe(e: ObservableEmitter<CacheResponse<T?>?>?) {
                val response: CacheResponse<T?>? = CacheResponse()
                if (update) {
                    remove(key)
                    e.onComplete()
                }
                var data: T? = getDataFromCache(getMemoryCache(), key, update, type)
                if (data != null) {
                    response.setData(data)
                    LogUtil.i("data from memory.")
                    e.onNext(response)
                    e.onComplete()
                } else {
                    data = getDataFromCache(getDiskCache(), key, update, type)
                    if (data != null) {
                        response.setData(data)
                        LogUtil.i("data from disk")
                        e.onNext(response)
                        e.onComplete()
                    } else {
                        LogUtil.i("data is null.")
                        e.onNext(response)
                        e.onComplete()
                    }
                }
            }
        })

        /*Observable<CacheResponse<T>> memory = Observable.create(new ObservableOnSubscribe<CacheResponse<T>>() {
            @Override
            public void subscribe(ObservableEmitter<CacheResponse<T>> e) throws Exception {
                CacheResponse<T> response = new CacheResponse<>();
                if (update) {
                    remove(key);
                    e.onComplete();
                }

                T data = getDataFromCache(getMemoryCache(), key, update, type);
                if (data != null) {
                    response.setData(data);
                    LogUtil.i("data from memory.");
                    e.onNext(response);
//                    e.onComplete();
                } else {
                    e.onComplete();
                }
            }
        });

        Observable<CacheResponse<T>> disk = Observable.create(new ObservableOnSubscribe<CacheResponse<T>>() {
            @Override
            public void subscribe(ObservableEmitter<CacheResponse<T>> e) throws Exception {
                CacheResponse<T> response = new CacheResponse<>();

                if (update) {
                    remove(key);
                    e.onNext(response);
                    e.onComplete();
                }

                T data = getDataFromCache(getDiskCache(), key, update, type);
                if (data != null) {
                    response.setData(data);
                    LogUtil.i("data from disk");
                    e.onNext(response);
                    e.onComplete();
                } else {
                    LogUtil.i("data is null.");
                    e.onNext(response);
                    e.onComplete();
                }
            }
        });
        return Observable.concat(memory, disk);*/
    }

    private fun <T> save2Memory(key: String?, realEntity: RealEntity<T?>?) {
        if (getMemoryCache() != null) {
            val cacheData: ByteArray? = getConverter().encode(realEntity)
            val save: Boolean = getMemoryCache().put(key, cacheData)
            if (save) LogUtil.i("copy data from disk to memory")
        }
    }

    private fun <T> getDataFromCache(
        cache: ICache?,
        key: String?,
        update: Boolean,
        type: Type?
    ): T? {
        var cacheData: ByteArray? = null
        var result: RealEntity<T?>? = null
        if (cache != null) {
            cacheData = cache.get(key, update)
            if (cacheData != null) {
                result = getConverter().decode(cacheData, type) as RealEntity<T?>?
            }
        }
        var data: T? = null
        if (result != null) {
            //非永久缓存，并且缓存尚未过期，或者是永久缓存
            if (result.getCacheTime() === -1 || (result.getCacheTime() !== -1 && (result.getUpdateDate() + result.getCacheTime() > System.currentTimeMillis()))) {
                data = result.getDatas()
            }
        }
        //判断一下，如果data不为空，且这是从磁盘获取的，将其同步到内存缓存中
        if (data != null && cache is DiskCache) {
            save2Memory<Any?>(key, result)
        }
        return data
    }

    fun remove(key: String?): Observable<Boolean?>? {
        if (getCacheMode() === CacheMode.NONE) return Observable.just(true)
        var result: Boolean = false
        if (getDiskCache() != null) {
            result = result or getDiskCache().remove(key)
            result = result or !getDiskCache().contains(key)
        }
        if (getMemoryCache() != null) {
            result = result or getMemoryCache().remove(key)
            result = result or !getMemoryCache().contains(key)
        }
        return Observable.just(result)
    }

    fun remove(vararg keys: String?): Observable<Boolean?>? {
        if (getCacheMode() === CacheMode.NONE) return Observable.just(true)
        var result: Boolean = false
        for (i in 0 until keys.size) {
            if (getDiskCache() != null) {
                result = result or getDiskCache().remove(keys.get(i))
                result = result or !getDiskCache().contains(keys.get(i))
            }
            if (getMemoryCache() != null) {
                result = result or getMemoryCache().remove(keys.get(i))
                result = result or !getMemoryCache().contains(keys.get(i))
            }
        }
        return Observable.just(result)
    }

    fun clear(): Observable<Boolean?>? {
        if (getCacheMode() === CacheMode.NONE) return Observable.just(true)
        var result: Boolean = false
        if (getDiskCache() != null) result = result or getDiskCache().clear()
        if (getMemoryCache() != null) result = result or getMemoryCache().clear()
        return Observable.just(result)
    }

    companion object {
        private var mDiskCache: DiskCache? = null
        private var mMemoryCache: MemoryCache? = null
    }
}