package com.hh.hlibrary.cache.cache

import android.content.Context

/**
 * 磁盘缓存实现类
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
class DiskCache constructor(context: Context?, dirName: String?, cacheSizeByMb: Int) : ICache {
    private val lruCache: DiskLruCache? = null
    fun isClosed(): Boolean {
        if (lruCache == null) return true
        return lruCache.isClosed()
    }

    /**
     * 获取文件夹地址，如果不存在，则创建
     *
     * @param context 上下文
     * @param dirName 文件名
     * @return File 文件
     */
    private fun getDiskCacheFile(context: Context?, dirName: String?): File? {
        var cacheDir: File?
        try {
            cacheDir = packDiskCacheFile(context, dirName)
        } catch (e: NullPointerException) {
            cacheDir = File(context.getCacheDir().getPath() + File.separator + dirName)
        }
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }
        return cacheDir
    }

    /**
     * 获取文件夹地址
     *
     * @param context 上下文
     * @param dirName 文件名
     * @return File 文件
     */
    @Throws(NullPointerException::class)
    private fun packDiskCacheFile(context: Context?, dirName: String?): File? {
        val cachePath: String?
        if ((Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                    || !Environment.isExternalStorageRemovable())
        ) {
//            cachePath = context.getExternalCacheDir().getPath();
            cachePath = Environment.getExternalStorageDirectory().getPath()
        } else {
            cachePath = Environment.getExternalStorageDirectory().getPath()
        }
        return File(cachePath + File.separator + dirName)
    }

    /**
     * 获取当前应用程序的版本号。
     */
    private fun getAppVersion(context: Context?): Int {
        try {
            val info: PackageInfo? =
                context.getPackageManager().getPackageInfo(context.getPackageName(), 0)
            return info.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            LogUtil.t(e)
        }
        return 1
    }

    @Override
    fun put(key: String?, data: ByteArray?): Boolean {
        var key: String? = key
        Utilities.checkNullOrEmpty(key, "key is null or empty.")
        key = Utilities.Md5(key)
        if (lruCache == null) return false
        try {
            val edit: DiskLruCache.Editor? = lruCache.edit(key)
            if (edit == null) {
                return false
            }
            val sink: OutputStream? = edit.newOutputStream(0)
            if (sink != null) {
                sink.write(data, 0, data.size)
                sink.flush()
                Utilities.closeQuietly(sink)
                edit.commit()
                LogUtil.i("DiskCache save success!")
                return true
            }
            edit.abort()
        } catch (e: IOException) {
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
        if (lruCache == null) return null
        try {
            val edit: DiskLruCache.Editor? = lruCache.edit(key)
            if (edit == null) {
                return null
            }
            val source: InputStream? = edit.newInputStream(0)
            val value: ByteArray?
            if (source != null) {
                value = Utilities.input2byte(source)
                Utilities.closeQuietly(source)
                edit.commit()
                LogUtil.i("DiskCache get success!")
                return value
            }
            edit.abort()
        } catch (e: IOException) {
            LogUtil.t(e)
        }
        return null
    }

    @Override
    operator fun contains(key: String?): Boolean {
        Utilities.checkNullOrEmpty(key, "key is null or empty.")
        return get(key, false) != null
    }

    @Override
    fun remove(key: String?): Boolean {
        var key: String? = key
        Utilities.checkNullOrEmpty(key, "key is null or empty.")
        key = Utilities.Md5(key)
        if (lruCache == null) return false
        try {
            return lruCache.remove(key)
        } catch (e: Exception) {
            LogUtil.t(e)
        }
        return false
    }

    @Override
    fun clear(): Boolean {
        if (lruCache == null) {
            return false
        }
        try {
            lruCache.delete()
            return true
        } catch (e: Exception) {
            LogUtil.t(e)
        }
        return false
    }

    init {
        if (cacheSizeByMb <= 0) throw IllegalArgumentException("diskCacheSize must > 0.")
        try {
            lruCache = DiskLruCache.open(
                getDiskCacheFile(context, dirName),
                getAppVersion(context),
                1,
                cacheSizeByMb * 1024 * 1024
            )
        } catch (e: IOException) {
            LogUtil.t(e)
        }
    }
}