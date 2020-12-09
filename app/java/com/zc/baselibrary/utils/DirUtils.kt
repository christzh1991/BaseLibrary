package com.hh.hlibrary.utils

import android.annotation.TargetApi

/**
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
object DirUtils {
    private val TAG: String? = "DirUtils"
    private val DEBUG: Boolean = false
    fun getDiskCacheDir(context: Context?, uniqueName: String?): File? {
        assertContext(context)
        val cachePath: String? =
            if (!isUserExternal() && isExternalStorageRemovable()) context.getCacheDir()
                .getPath() else getExternalCacheDir(context).getPath()
        val dir: File? = File(cachePath + File.separator + uniqueName)
        if (dir.exists()) {
            if (dir.isDirectory()) {
                return dir
            }
            dir.delete()
        } else {
            dir.mkdirs()
        }
        return dir
    }

    fun getDiskCacheDir(context: Context?): File? {
        assertContext(context)
        var cachePath: String? =
            if (!isUserExternal() && isExternalStorageRemovable()) context.getCacheDir()
                .getPath() else getExternalCacheDir(context).getPath()
        if (StringUtils.isEmpty(cachePath)) {
            cachePath = DeviceUtil.getSDCardPath()
        }
        val dir: File? = File(cachePath)
        if (dir.exists()) {
            if (dir.isDirectory()) {
                return dir
            }
            dir.delete()
        } else {
            dir.mkdirs()
        }
        return dir
    }

    fun getDiskFilesDir(context: Context?, uniqueName: String?): File? {
        assertContext(context)
        val cachePath: String? =
            if (!isUserExternal() && isExternalStorageRemovable()) context.getFilesDir()
                .getPath() else getExternalFilesDir(context).getPath()
        val dir: File? = File(cachePath + File.separator + uniqueName)
        if (dir.exists()) {
            if (dir.isDirectory()) {
                return dir
            }
            dir.delete()
        } else {
            dir.mkdirs()
        }
        return dir
    }

    fun getDiskFilesDir(context: Context?): File? {
        assertContext(context)
        val cachePath: String? =
            if (!isUserExternal() && isExternalStorageRemovable()) context.getFilesDir()
                .getPath() else getExternalFilesDir(context).getPath()
        val dir: File? = File(cachePath)
        if (dir.exists()) {
            if (dir.isDirectory()) {
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
        val isUserExternal: Boolean = "mounted".equals(externalStorageState)
        return isUserExternal
    }

    @TargetApi(9)
    fun isExternalStorageRemovable(): Boolean {
        return if (CompatUtils.hasGingerbread()) Environment.isExternalStorageRemovable() else true
    }

    @TargetApi(8)
    private fun getExternalCacheDir(context: Context?): File? {
        if (CompatUtils.hasFroyo()) {
            val dir: File? = context.getExternalCacheDir()
            if (dir != null) {
                return dir
            }
        }
        val cacheDir: String? = "/Android/data/" + context.getPackageName().toString() + "/cache/"
        return File(Environment.getExternalStorageDirectory().getPath() + cacheDir)
    }

    @TargetApi(8)
    private fun getExternalFilesDir(context: Context?): File? {
        if (CompatUtils.hasFroyo()) {
            val dir: File? = context.getExternalFilesDir(null as String?)
            if (dir != null) {
                return dir
            }
        }
        val cacheDir: String? = "/Android/data/" + context.getPackageName().toString() + "/files/"
        return File(Environment.getExternalStorageDirectory().getPath() + cacheDir)
    }

    @TargetApi(9)
    fun getUsableSpace(path: File?): Long {
        if (CompatUtils.hasGingerbread()) {
            return path.getUsableSpace()
        } else {
            val stats: StatFs? = StatFs(path.getPath())
            return stats.getBlockSize() as Long * stats.getAvailableBlocks() as Long
        }
    }

    fun getExternalStorage(context: Context?, uniqueName: String?): File? {
        val file: File?
        if (isUserExternal()) {
            val path: String? = "/sdcard/" + context.getPackageName()
            file = File(path, uniqueName)
            if (file.exists()) {
                if (!file.isDirectory()) {
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