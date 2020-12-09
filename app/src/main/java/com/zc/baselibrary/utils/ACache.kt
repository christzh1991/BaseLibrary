package com.zc.baselibrary.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Process
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import kotlin.math.min

/**
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
class ACache private constructor(ctx: Context?, cacheDir: File?, max_size: Long, max_count: Int) {
    private val mCache: ACacheManager

    fun put(key: String, value: String) {
        val file = mCache.newFile(key)
        var out: BufferedWriter? = null
        try {
            out = BufferedWriter(FileWriter(file), 1024)
            out.write(value)
        } catch (var14: IOException) {
            var14.printStackTrace()
        } finally {
            if (out != null) {
                try {
                    out.flush()
                    out.close()
                } catch (var13: IOException) {
                    var13.printStackTrace()
                }
            }
            mCache.put(file)
        }
    }

    fun put(key: String, value: String, saveTime: Int) {
        this.put(key, Utils.newStringWithDateInfo(saveTime, value))
    }

    private fun getAsString(key: String): String? {
        val file = mCache[key]
        return if (!file.exists()) {
            null
        } else {
            var removeFile = false
            var `in`: BufferedReader? = null
            var currentLine: String?
            try {
                `in` = BufferedReader(FileReader(file))
                var readString: String
                readString = ""
                while (`in`.readLine().also { currentLine = it } != null) {
                    readString = readString + currentLine
                }
                val var7: String?
                if (Utils.isDue(readString)) {
                    removeFile = true
                    var7 = null
                    return var7
                }
                var7 = Utils.clearDateInfo(readString)
                return var7
            } catch (var18: IOException) {
                var18.printStackTrace()
                currentLine = null
            } finally {
                if (`in` != null) {
                    try {
                        `in`.close()
                    } catch (var17: IOException) {
                        var17.printStackTrace()
                    }
                }
                if (removeFile) {
                    this.remove(key)
                }
            }
            currentLine
        }
    }

    fun put(key: String, value: JSONObject?) {
        this.put(key, value.toString())
    }

    fun put(key: String, value: JSONObject?, saveTime: Int) {
        this.put(key, value.toString(), saveTime)
    }

    fun getAsJSONObject(key: String): JSONObject? {
        val JSONString = getAsString(key)
        return try {
            JSONObject(JSONString)
        } catch (var4: Exception) {
            var4.printStackTrace()
            null
        }
    }

    fun put(key: String, value: JSONArray) {
        this.put(key, value.toString())
    }

    fun put(key: String, value: JSONArray?, saveTime: Int) {
        this.put(key, value.toString(), saveTime)
    }

    fun getAsJSONArray(key: String): JSONArray? {
        val JSONString = getAsString(key)
        return try {
            JSONArray(JSONString)
        } catch (var4: Exception) {
            var4.printStackTrace()
            null
        }
    }

    fun put(key: String, value: ByteArray) {
        val file = mCache.newFile(key)
        var out: FileOutputStream? = null
        try {
            if (!file.exists()) {
                file.createNewFile()
            }
            out = FileOutputStream(file)
            out.write(value)
        } catch (var14: Exception) {
            var14.printStackTrace()
        } finally {
            if (out != null) {
                try {
                    out.flush()
                    out.close()
                } catch (var13: IOException) {
                    var13.printStackTrace()
                }
            }
            mCache.put(file)
        }
    }

    @Throws(FileNotFoundException::class)
    fun put(key: String?): OutputStream {
        return xFileOutputStream(mCache.newFile(key))
    }

    @Throws(FileNotFoundException::class)
    operator fun get(key: String?): InputStream? {
        val file = mCache[key]
        return if (!file.exists()) null else FileInputStream(file)
    }

    fun put(key: String, value: ByteArray, saveTime: Int) {
        this.put(key, Utils.newByteArrayWithDateInfo(saveTime, value))
    }

    fun getAsBinary(key: String): ByteArray? {
        var RAFile: RandomAccessFile? = null
        var removeFile = false
        val var6: Any?
        try {
            var var5: Any?
            try {
                val file = mCache[key]
                if (!file.exists()) {
                    var5 = null
                    return var5
                }
                RAFile = RandomAccessFile(file, "r")
                val byteArray = ByteArray(RAFile.length().toInt())
                RAFile.read(byteArray)
                if (!Utils.isDue(byteArray)) {
                    return Utils.clearDateInfo(byteArray)
                }
                removeFile = true
                var6 = null
            } catch (var18: Exception) {
                var18.printStackTrace()
                var5 = null
                return var5
            }
        } finally {
            if (RAFile != null) {
                try {
                    RAFile.close()
                } catch (var17: IOException) {
                    var17.printStackTrace()
                }
            }
            if (removeFile) {
                this.remove(key)
            }
        }
        return var6 as ByteArray?
    }

    fun put(key: String, value: Serializable) {
        this.put(key, value, -1)
    }

    fun put(key: String, value: Serializable, saveTime: Int) {
        var baos: ByteArrayOutputStream? = null
        var oos: ObjectOutputStream? = null
        try {
            baos = ByteArrayOutputStream()
            oos = ObjectOutputStream(baos)
            oos.writeObject(value)
            val data = baos.toByteArray()
            if (saveTime != -1) {
                this.put(key, data, saveTime)
            } else {
                this.put(key, data)
            }
        } catch (var15: Exception) {
            var15.printStackTrace()
        } finally {
            try {
                oos?.close()
            } catch (var14: IOException) {
            }
        }
    }

    fun getAsObject(key: String): Any? {
        val data = getAsBinary(key)
        return if (data != null) {
            var bais: ByteArrayInputStream? = null
            var ois: ObjectInputStream? = null
            var var6: Any?
            try {
                bais = ByteArrayInputStream(data)
                ois = ObjectInputStream(bais)
                val reObject = ois.readObject()
                var6 = reObject
                return var6
            } catch (var20: Exception) {
                var20.printStackTrace()
                var6 = null
            } finally {
                try {
                    bais?.close()
                } catch (var19: IOException) {
                    var19.printStackTrace()
                }
                try {
                    ois?.close()
                } catch (var18: IOException) {
                    var18.printStackTrace()
                }
            }
            var6
        } else {
            null
        }
    }

    fun put(key: String, value: Bitmap) {
        this.put(key, Utils.bitmap2Bytes(value))
    }

    fun put(key: String, value: Bitmap, saveTime: Int) {
        this.put(key, Utils.bitmap2Bytes(value), saveTime)
    }

    fun getAsBitmap(key: String): Bitmap? {
        return if (getAsBinary(key) == null) null else Utils.bytes2Bitmap(
            getAsBinary(key)
        )
    }

    fun put(key: String, value: Drawable) {
        this.put(key, Utils.drawable2Bitmap(value))
    }

    fun put(key: String, value: Drawable, saveTime: Int) {
        this.put(key, Utils.drawable2Bitmap(value), saveTime)
    }

    fun getAsDrawable(key: String): Drawable {
        return Utils.bitmap2Drawable(
            Utils.bytes2Bitmap(
                getAsBinary(key)
            )
        )
    }

    fun file(key: String?): File? {
        val f = mCache.newFile(key)
        return if (f.exists()) f else null
    }

    fun remove(key: String): Boolean {
        return mCache.remove(key)
    }

    fun clear() {
        mCache.clear()
    }

    private object Utils {
        private const val mSeparator = ' '
        fun isDue(str: String): Boolean {
            return isDue(str.toByteArray())
        }

        fun isDue(data: ByteArray): Boolean {
            val strs = getDateInfoFromDate(data)
            if (strs != null && strs.size == 2) {
                var saveTimeStr: String?
                saveTimeStr = strs[0]
                while (saveTimeStr?.startsWith("0") == true) {
                    saveTimeStr = saveTimeStr.substring(1, saveTimeStr.length)
                }
                val saveTime = saveTimeStr?.toLong()
                val deleteAfter = strs[1]?.toLong()
                if (System.currentTimeMillis() > deleteAfter?.times(1000L)
                        ?.let { saveTime?.plus(it) } ?: 0
                ) {
                    return true
                }
            }
            return false
        }

        fun newStringWithDateInfo(second: Int, strInfo: String?): String {
            return createDateInfo(second) + strInfo
        }

        fun newByteArrayWithDateInfo(second: Int, data2: ByteArray): ByteArray {
            val data1 = createDateInfo(second).toByteArray()
            val retdata = ByteArray(data1.size + data2.size)
            System.arraycopy(data1, 0, retdata, 0, data1.size)
            System.arraycopy(data2, 0, retdata, data1.size, data2.size)
            return retdata
        }

        fun clearDateInfo(strInfo: String?): String? {
            var strInfo = strInfo
            if (strInfo != null && hasDateInfo(strInfo.toByteArray())) {
                strInfo = strInfo.substring(strInfo.indexOf(32.toChar()) + 1, strInfo.length)
            }
            return strInfo
        }

        fun clearDateInfo(data: ByteArray): ByteArray {
            return if (hasDateInfo(data)) copyOfRange(
                data,
                indexOf(data, ' ') + 1,
                data.size
            ) else data
        }

        private fun hasDateInfo(data: ByteArray?): Boolean {
            return data != null && data.size > 15 && data[13].toInt() == 45 && indexOf(
                data,
                ' '
            ) > 14
        }

        private fun getDateInfoFromDate(data: ByteArray): Array<String?>? {
            return if (hasDateInfo(data)) {
                val saveDate =
                    String(copyOfRange(data, 0, 13))
                val deleteAfter = String(
                    copyOfRange(
                        data,
                        14,
                        indexOf(data, ' ')
                    )
                )
                arrayOf(saveDate, deleteAfter)
            } else {
                null
            }
        }

        private fun indexOf(data: ByteArray, c: Char): Int {
            for (i in data.indices) {
                if (data[i].toChar() == c) {
                    return i
                }
            }
            return -1
        }

        private fun copyOfRange(original: ByteArray, from: Int, to: Int): ByteArray {
            val newLength = to - from
            return if (newLength < 0) {
                throw IllegalArgumentException("$from > $to")
            } else {
                val copy = ByteArray(newLength)
                System.arraycopy(
                    original,
                    from,
                    copy,
                    0,
                    min(original.size - from, newLength)
                )
                copy
            }
        }

        private fun createDateInfo(second: Int): String {
            var currentTime: String
            currentTime = System.currentTimeMillis().toString() + ""
            while (currentTime.length < 13) {
                currentTime = "0$currentTime"
            }
            return "$currentTime-$second "
        }

        fun bitmap2Bytes(bm: Bitmap): ByteArray {
            val baos = ByteArrayOutputStream()
            bm.compress(CompressFormat.PNG, 100, baos)
            return baos.toByteArray()
        }

        fun bytes2Bitmap(b: ByteArray?): Bitmap? {
            return if (b?.isEmpty() == true) null else b?.size?.let {
                BitmapFactory.decodeByteArray(b, 0, it)
            }
        }

        fun drawable2Bitmap(drawable: Drawable): Bitmap {
            val w = drawable.intrinsicWidth
            val h = drawable.intrinsicHeight
            val config =
                if (drawable.opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
            val bitmap = Bitmap.createBitmap(w, h, config)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, w, h)
            drawable.draw(canvas)
            return bitmap
        }

        fun bitmap2Drawable(bm: Bitmap?): Drawable {
            val bd = BitmapDrawable(bm)
            bm?.density?.let { bd.setTargetDensity(it) }
            return BitmapDrawable(bm)
        }
    }

    inner class ACacheManager(
        cacheDir: File?,
        sizeLimit: Long,
        countLimit: Int
    ) {
        private val cacheSize: AtomicLong?
        private val cacheCount: AtomicInteger?
        private val sizeLimit: Long
        private val countLimit: Int
        private val lastUsageDates: MutableMap<File?, Long?>? =
            Collections.synchronizedMap<File?, Long?>(HashMap())
        protected var cacheDir: File?

        private fun calculateCacheSizeAndCacheCount() {
            Thread {
                var size = 0
                var count = 0
                val cachedFiles = cacheDir?.listFiles()
                if (cachedFiles != null) {
                    val var5 = cachedFiles.size
                    for (var6 in 0 until var5) {
                        val cachedFile = cachedFiles[var6]
                        size = (size.toLong() + calculateSize(cachedFile)).toInt()
                        ++count
                        lastUsageDates?.set(cachedFile, cachedFile.lastModified())
                    }
                    cacheSize?.set(size.toLong())
                    cacheCount?.set(count)
                }
            }.start()
        }

        fun put(file: File?) {
            var valueSize: Long
            var curCacheCount = cacheCount?.get()
            while (curCacheCount?.plus(1) ?: 1 > countLimit) {
                valueSize = removeNext()
                cacheSize?.addAndGet(-valueSize)
                curCacheCount = cacheCount?.addAndGet(-1)
            }
            cacheCount?.addAndGet(1)
            valueSize = calculateSize(file)
            var freedSize: Long
            var curCacheSize = cacheSize?.get()
            while (curCacheSize?.plus(valueSize) ?: 1 > sizeLimit) {
                freedSize = removeNext()
                curCacheSize = cacheSize?.addAndGet(-freedSize)
            }
            cacheSize?.addAndGet(valueSize)
            val currentTime = System.currentTimeMillis()
            file?.setLastModified(currentTime)
            lastUsageDates?.set(file, currentTime)
        }

        operator fun get(key: String?): File {
            val file = newFile(key)
            val currentTime = System.currentTimeMillis()
            file.setLastModified(currentTime)
            lastUsageDates?.set(file, currentTime)
            return file
        }

        fun newFile(key: String?): File {
            if (!cacheDir?.exists()!!) {
                cacheDir!!.mkdirs()
            }
            return File(cacheDir, key.hashCode().toString() + "")
        }

        fun remove(key: String): Boolean {
            val image = this[key]
            return image.delete()
        }

        fun clear() {
            lastUsageDates?.clear()
            cacheSize?.set(0L)
            val files = cacheDir?.listFiles()
            if (files != null) {
                val var3 = files.size
                for (var4 in 0 until var3) {
                    val f = files[var4]
                    f.delete()
                }
            }
        }

        private fun removeNext(): Long {
            return if (lastUsageDates?.isEmpty() == true) {
                0L
            } else {
                var oldestUsage: Long? = null
                var mostLongUsedFile: File? = null
                val entries: MutableSet<MutableMap.MutableEntry<File?, Long?>>? =
                    lastUsageDates?.entries
                synchronized(lastUsageDates!!) {
                    val var5: MutableIterator<*> = entries?.iterator()!!
                    while (true) {
                        if (!var5.hasNext()) {
                            break
                        }
                        val entry: MutableMap.MutableEntry<File?, Long?>? =
                            var5.next() as MutableMap.MutableEntry<File?, Long?>?
                        if (mostLongUsedFile == null) {
                            mostLongUsedFile = entry?.key as File?
                            oldestUsage = entry?.value as Long?
                        } else {
                            val lastValueUsage = entry?.value as Long?
                            if (lastValueUsage!! < oldestUsage!!) {
                                oldestUsage = lastValueUsage
                                mostLongUsedFile = entry?.key as File?
                            }
                        }
                    }
                }
                val fileSize = calculateSize(mostLongUsedFile)
                if (mostLongUsedFile?.delete() == true) {
                    lastUsageDates.remove(mostLongUsedFile)
                }
                fileSize
            }
        }

        private fun calculateSize(file: File?): Long {
            return file?.length()!!
        }

        init {
            this.cacheDir = cacheDir
            this.sizeLimit = sizeLimit
            this.countLimit = countLimit
            cacheSize = AtomicLong()
            cacheCount = AtomicInteger()
            calculateCacheSizeAndCacheCount()
        }
    }

    internal inner class xFileOutputStream(private var file: File?) : FileOutputStream(file) {
        @Throws(IOException::class)
        override fun close() {
            super.close()
            mCache.put(file)
        }
    }

    companion object {
        const val TIME_HOUR = 3600
        const val TIME_DAY = 86400
        private const val MAX_SIZE = 50000000
        private const val MAX_COUNT = 2147483647
        private val mInstanceMap: MutableMap<String?, ACache?> = HashMap<String?, ACache?>()

        @JvmOverloads
        operator fun get(ctx: Context, cacheName: String? = "ACache"): ACache {
            val f = DirUtils.getDiskCacheDir(ctx, cacheName)
            return Companion[ctx, f, 50000000L, 2147483647]
        }

        operator fun get(ctx: Context?, max_size: Long, max_count: Int): ACache {
            val f = File(ctx?.cacheDir, "ACache")
            return Companion[ctx, f, max_size, max_count]
        }

        operator fun get(ctx: Context?, cacheDir: File?, max_size: Long, max_count: Int): ACache {
            var manager =
                mInstanceMap[cacheDir?.absoluteFile.toString() + myPid()] as ACache?
            if (manager == null) {
                manager = ACache(ctx, cacheDir, max_size, max_count)
                mInstanceMap[cacheDir?.absolutePath + myPid()] = manager
            }
            return manager
        }

        private fun myPid(): String {
            return "_" + Process.myPid()
        }
    }

    init {
        var cacheDir = cacheDir
        if (!cacheDir?.exists()!! && !cacheDir.mkdirs()) {
            cacheDir = File(ctx?.cacheDir, "ACache")
        }
        mCache = ACacheManager(cacheDir, max_size, max_count)
    }
}