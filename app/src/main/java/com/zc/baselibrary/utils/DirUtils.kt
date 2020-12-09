package com.zc.baselibrary.utils

import android.content.Context
import android.os.Environment
import android.os.StatFs
import java.io.File

/**
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
object DirUtils {
    private val TAG: String = "DirUtils"
    private val DEBUG: Boolean = false
    fun getDiskCacheDir(context: Context, uniqueName: String?): File {
        assertContext(context)
        val cachePath: String? =
            if (!isUserExternal() && isExternalStorageRemovable()) context.cacheDir
                .path else getExternalCacheDir(context).path
        val dir: File = File(cachePath + File.separator + uniqueName)
        if (dir.exists()) {
            if (dir.isDirectory) {
                return dir
            }
            dir.delete()
        } else {
            dir.mkdirs()
        }
        return dir
    }

    fun getDiskCacheDir(context: Context): File {
        assertContext(context)
        var cachePath: String =
            if (!isUserExternal() && isExternalStorageRemovable()) context.cacheDir
                .path else getExternalCacheDir(context).path
        if (StringUtils.isEmpty(cachePath)) {
            cachePath = DeviceUtil.getSDCardPath()
        }
        val dir: File = File(cachePath)
        if (dir.exists()) {
            if (dir.isDirectory) {
                return dir
            }
            dir.delete()
        } else {
            dir.mkdirs()
        }
        return dir
    }

    private fun getDiskFilesDir(context: Context, uniqueName: String?): File {
        assertContext(context)
        val cachePath: String =
            if (!isUserExternal() && isExternalStorageRemovable()) context.filesDir
                .path else getExternalFilesDir(context).path
        val dir: File = File(cachePath + File.separator + uniqueName)
        if (dir.exists()) {
            if (dir.isDirectory) {
                return dir
            }
            dir.delete()
        } else {
            dir.mkdirs()
        }
        return dir
    }

    fun getDiskFilesDir(context: Context): File {
        assertContext(context)
        val cachePath: String =
            if (!isUserExternal() && isExternalStorageRemovable()) context.filesDir
                .path else getExternalFilesDir(context).path
        val dir: File = File(cachePath)
        if (dir.exists()) {
            if (dir.isDirectory) {
                return dir
            }
            dir.delete()
        } else {
            dir.mkdirs()
        }
        return dir
    }

    private fun assertContext(context: Context?) {
        if (context == null) {
            throw NullPointerException("context can't be null")
        }
    }

    private fun isUserExternal(): Boolean {
        val externalStorageState: String? = Environment.getExternalStorageState()
        val isUserExternal: Boolean = ("mounted" == externalStorageState)
        return isUserExternal
    }

    fun isExternalStorageRemovable(): Boolean {
        return Environment.isExternalStorageRemovable()
    }

    private fun getExternalCacheDir(context: Context): File {
        val dir: File? = context.externalCacheDir
        if (dir != null) {
            return dir
        }
        val cacheDir: String = "/Android/data/" + context.packageName + "/cache/"
        return File(Environment.getExternalStorageDirectory().path + cacheDir)
    }

    private fun getExternalFilesDir(context: Context): File {
        val dir: File? = context.getExternalFilesDir(null as String?)
        if (dir != null) {
            return dir
        }
        val cacheDir: String = "/Android/data/" + context.packageName + "/files/"
        return File(Environment.getExternalStorageDirectory().path + cacheDir)
    }

    fun getUsableSpace(path: File): Long {
        val stats: StatFs = StatFs(path.path)
        return stats.blockSize.toLong() * stats.availableBlocks.toLong()
    }

    fun getExternalStorage(context: Context, uniqueName: String): File {
        val file: File?
        if (isUserExternal()) {
            val path: String = "/sdcard/" + context.packageName
            file = File(path, uniqueName)
            if (file.exists()) {
                if (!file.isDirectory) {
                    file.delete()
                }
            } else {
                file.mkdirs()
            }
        } else {
            file = getDiskFilesDir(context, uniqueName)
        }
        return file
    }
}