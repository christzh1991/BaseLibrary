package com.zc.baselibrary.cache.cache

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Environment
import com.jakewharton.disklrucache.DiskLruCache
import com.zc.baselibrary.cache.util.LogUtil
import com.zc.baselibrary.cache.util.Utilities
import java.io.File
import java.io.IOException

/**
 * 磁盘缓存实现类
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
class DiskCache() : ICache {
    private var lruCache: DiskLruCache? = null

    constructor(context: Context, dirName: String, cacheSizeByMb: Int) : this() {
        require(cacheSizeByMb > 0) { "diskCacheSize must > 0." }
        try {
            lruCache = DiskLruCache.open(
                getDiskCacheFile(context, dirName),
                getAppVersion(context),
                1,
                (cacheSizeByMb * 1024 * 1024).toLong()
            )
        } catch (e: IOException) {
            LogUtil.t(e)
        }
    }

    fun isClosed(): Boolean {
        return lruCache?.isClosed ?: true
    }

    /**
     * 获取文件夹地址，如果不存在，则创建
     *
     * @param context 上下文
     * @param dirName 文件名
     * @return File 文件
     */
    private fun getDiskCacheFile(context: Context, dirName: String?): File? {
        val cacheDir: File = try {
            packDiskCacheFile(context, dirName)
        } catch (e: NullPointerException) {
            File(context.cacheDir.path + File.separator + dirName)
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
    private fun packDiskCacheFile(context: Context?, dirName: String?): File {
        val cachePath: String? =
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageRemovable()) {
//            cachePath = context.getExternalCacheDir().getPath();
                Environment.getExternalStorageDirectory().path
            } else {
                Environment.getExternalStorageDirectory().path
            }
        return File(cachePath + File.separator + dirName)
    }

    /**
     * 获取当前应用程序的版本号。
     */
    private fun getAppVersion(context: Context): Int {
        try {
            val info: PackageInfo? =
                context.packageManager.getPackageInfo(context.packageName, 0)
            return info!!.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            LogUtil.t(e)
        }
        return 1
    }

    override fun put(key: String?, data: ByteArray?): Boolean {
        val checkNullOrEmpty = Utilities.checkNullOrEmpty(key, "key is null or empty.")
        val keyTemp: String? = Utilities.Md5(key)
        if (lruCache == null) return false
        try {
            val edit = lruCache?.edit(keyTemp) ?: return false
            val sink = edit.newOutputStream(0)
            if (sink != null) {
                sink.write(data, 0, data!!.size)
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

    override fun get(key: String?, update: Boolean): ByteArray? {
        var key = key
        Utilities.checkNullOrEmpty(key, "key is null or empty.")
        key = Utilities.Md5(key)
        if (update) {
            remove(key)
            return null
        }
        if (lruCache == null) return null
        try {
            val edit = lruCache!!.edit(key) ?: return null
            val source = edit.newInputStream(0)
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

    override fun contains(key: String?): Boolean {
        Utilities.checkNullOrEmpty(key, "key is null or empty.")
        return get(key, false) != null
    }

    override fun remove(key: String?): Boolean {
        var key = key
        Utilities.checkNullOrEmpty(key, "key is null or empty.")
        key = Utilities.Md5(key)
        if (lruCache == null) return false
        try {
            return lruCache!!.remove(key)
        } catch (e: Exception) {
            LogUtil.t(e)
        }
        return false
    }

    override fun clear(): Boolean {
        if (lruCache == null) {
            return false
        }
        try {
            lruCache!!.delete()
            return true
        } catch (e: Exception) {
            LogUtil.t(e)
        }
        return false
    }

}