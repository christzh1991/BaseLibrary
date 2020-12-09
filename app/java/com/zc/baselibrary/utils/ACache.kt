import android.content.Context

/**
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
class ACache private constructor(ctx: Context?, cacheDir: File?, max_size: Long, max_count: Int) {
    private val mCache: ACacheManager?
    fun put(@NonNull key: String?, @NonNull value: String?) {
        if (key != null && value != null) {
            val file: File? = mCache.newFile(key)
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
    }

    fun put(key: String?, value: String?, saveTime: Int) {
        this.put(key, Utils.newStringWithDateInfo(saveTime, value))
    }

    fun getAsString(key: String?): String? {
        val file: File? = mCache.get(key)
        if (!file.exists()) {
            return null
        } else {
            var removeFile: Boolean = false
            var `in`: BufferedReader? = null
            var currentLine: String?
            try {
                `in` = BufferedReader(FileReader(file))
                var readString: String?
                readString = ""
                while ((`in`.readLine().also({ currentLine = it })) != null) {
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
                    remove(key)
                }
            }
            return currentLine
        }
    }

    fun put(key: String?, value: JSONObject?) {
        this.put(key, value.toString())
    }

    fun put(key: String?, value: JSONObject?, saveTime: Int) {
        this.put(key, value.toString(), saveTime)
    }

    fun getAsJSONObject(key: String?): JSONObject? {
        val JSONString: String? = getAsString(key)
        try {
            val obj: JSONObject? = JSONObject(JSONString)
            return obj
        } catch (var4: Exception) {
            var4.printStackTrace()
            return null
        }
    }

    fun put(key: String?, value: JSONArray?) {
        this.put(key, value.toString())
    }

    fun put(key: String?, value: JSONArray?, saveTime: Int) {
        this.put(key, value.toString(), saveTime)
    }

    fun getAsJSONArray(key: String?): JSONArray? {
        val JSONString: String? = getAsString(key)
        try {
            val obj: JSONArray? = JSONArray(JSONString)
            return obj
        } catch (var4: Exception) {
            var4.printStackTrace()
            return null
        }
    }

    fun put(key: String?, value: ByteArray?) {
        val file: File? = mCache.newFile(key)
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
    fun put(key: String?): OutputStream? {
        return xFileOutputStream(mCache.newFile(key))
    }

    @Throws(FileNotFoundException::class)
    operator fun get(key: String?): InputStream? {
        val file: File? = mCache.get(key)
        return if (!file.exists()) null else FileInputStream(file)
    }

    fun put(key: String?, value: ByteArray?, saveTime: Int) {
        this.put(key, Utils.newByteArrayWithDateInfo(saveTime, value))
    }

    fun getAsBinary(key: String?): ByteArray? {
        var RAFile: RandomAccessFile? = null
        var removeFile: Boolean = false
        val var6: Object?
        try {
            var var5: Object?
            try {
                val file: File? = mCache.get(key)
                if (!file.exists()) {
                    var5 = null
                    return var5 as ByteArray?
                }
                RAFile = RandomAccessFile(file, "r")
                val byteArray: ByteArray? = ByteArray(RAFile.length() as Int)
                RAFile.read(byteArray)
                if (!Utils.isDue(byteArray)) {
                    val var21: ByteArray? = Utils.clearDateInfo(byteArray)
                    return var21
                }
                removeFile = true
                var6 = null
            } catch (var18: Exception) {
                var18.printStackTrace()
                var5 = null
                return var5 as ByteArray?
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
                remove(key)
            }
        }
        return var6 as ByteArray?
    }

    fun put(key: String?, value: Serializable?) {
        this.put(key, value as Serializable?, -1)
    }

    fun put(key: String?, value: Serializable?, saveTime: Int) {
        var baos: ByteArrayOutputStream? = null
        var oos: ObjectOutputStream? = null
        try {
            baos = ByteArrayOutputStream()
            oos = ObjectOutputStream(baos)
            oos.writeObject(value)
            val data: ByteArray? = baos.toByteArray()
            if (saveTime != -1) {
                this.put(key, data, saveTime)
            } else {
                this.put(key, data)
            }
        } catch (var15: Exception) {
            var15.printStackTrace()
        } finally {
            try {
                oos.close()
            } catch (var14: IOException) {
            }
        }
    }

    fun getAsObject(key: String?): Object? {
        val data: ByteArray? = getAsBinary(key)
        if (data != null) {
            var bais: ByteArrayInputStream? = null
            var ois: ObjectInputStream? = null
            var var6: Object?
            try {
                bais = ByteArrayInputStream(data)
                ois = ObjectInputStream(bais)
                val reObject: Object? = ois.readObject()
                var6 = reObject
                return var6
            } catch (var20: Exception) {
                var20.printStackTrace()
                var6 = null
            } finally {
                try {
                    if (bais != null) {
                        bais.close()
                    }
                } catch (var19: IOException) {
                    var19.printStackTrace()
                }
                try {
                    if (ois != null) {
                        ois.close()
                    }
                } catch (var18: IOException) {
                    var18.printStackTrace()
                }
            }
            return var6
        } else {
            return null
        }
    }

    fun put(key: String?, value: Bitmap?) {
        this.put(key, Utils.Bitmap2Bytes(value))
    }

    fun put(key: String?, value: Bitmap?, saveTime: Int) {
        this.put(key, Utils.Bitmap2Bytes(value), saveTime)
    }

    fun getAsBitmap(key: String?): Bitmap? {
        return if (getAsBinary(key) == null) null else Utils.Bytes2Bimap(getAsBinary(key))
    }

    fun put(key: String?, value: Drawable?) {
        this.put(key, Utils.drawable2Bitmap(value))
    }

    fun put(key: String?, value: Drawable?, saveTime: Int) {
        this.put(key, Utils.drawable2Bitmap(value), saveTime)
    }

    fun getAsDrawable(key: String?): Drawable? {
        return if (getAsBinary(key) == null) null else Utils.bitmap2Drawable(
            Utils.Bytes2Bimap(
                getAsBinary(key)
            )
        )
    }

    fun file(key: String?): File? {
        val f: File? = mCache.newFile(key)
        return if (f.exists()) f else null
    }

    fun remove(key: String?): Boolean {
        return mCache.remove(key)
    }

    fun clear() {
        mCache.clear()
    }

    private object Utils {
        private val mSeparator: Char = ' '
        private fun isDue(str: String?): Boolean {
            return isDue(str.getBytes())
        }

        private fun isDue(data: ByteArray?): Boolean {
            val strs: Array<String?>? = getDateInfoFromDate(data)
            if (strs != null && strs.size == 2) {
                var saveTimeStr: String?
                saveTimeStr = strs.get(0)
                while (saveTimeStr.startsWith("0")) {
                    saveTimeStr = saveTimeStr.substring(1, saveTimeStr.length())
                }
                val saveTime: Long = Long.valueOf(saveTimeStr)
                val deleteAfter: Long = Long.valueOf(strs.get(1))
                if (System.currentTimeMillis() > saveTime + deleteAfter * 1000L) {
                    return true
                }
            }
            return false
        }

        private fun newStringWithDateInfo(second: Int, strInfo: String?): String? {
            return createDateInfo(second) + strInfo
        }

        private fun newByteArrayWithDateInfo(second: Int, data2: ByteArray?): ByteArray? {
            val data1: ByteArray? = createDateInfo(second).getBytes()
            val retdata: ByteArray? = ByteArray(data1.size + data2.size)
            System.arraycopy(data1, 0, retdata, 0, data1.size)
            System.arraycopy(data2, 0, retdata, data1.size, data2.size)
            return retdata
        }

        private fun clearDateInfo(strInfo: String?): String? {
            var strInfo: String? = strInfo
            if (strInfo != null && hasDateInfo(strInfo.getBytes())) {
                strInfo = strInfo.substring(strInfo.indexOf(32) + 1, strInfo.length())
            }
            return strInfo
        }

        private fun clearDateInfo(data: ByteArray?): ByteArray? {
            return if (hasDateInfo(data)) copyOfRange(
                data,
                indexOf(data, ' ') + 1,
                data.size
            ) else data
        }

        private fun hasDateInfo(data: ByteArray?): Boolean {
            return (data != null) && (data.size > 15) && (data.get(13) == 45) && (indexOf(
                data,
                ' '
            ) > 14)
        }

        private fun getDateInfoFromDate(data: ByteArray?): Array<String?>? {
            if (hasDateInfo(data)) {
                val saveDate: String? = String(copyOfRange(data, 0, 13))
                val deleteAfter: String? = String(copyOfRange(data, 14, indexOf(data, ' ')))
                return arrayOf(saveDate, deleteAfter)
            } else {
                return null
            }
        }

        private fun indexOf(data: ByteArray?, c: Char): Int {
            for (i in data.indices) {
                if (data.get(i) == c) {
                    return i
                }
            }
            return -1
        }

        private fun copyOfRange(original: ByteArray?, from: Int, to: Int): ByteArray? {
            val newLength: Int = to - from
            if (newLength < 0) {
                throw IllegalArgumentException(from.toString() + " > " + to)
            } else {
                val copy: ByteArray? = ByteArray(newLength)
                System.arraycopy(original, from, copy, 0, Math.min(original.size - from, newLength))
                return copy
            }
        }

        private fun createDateInfo(second: Int): String? {
            var currentTime: String?
            currentTime = System.currentTimeMillis().toString() + ""
            while (currentTime.length() < 13) {
                currentTime = "0" + currentTime
            }
            return currentTime.toString() + "-" + second + ' '
        }

        private fun Bitmap2Bytes(bm: Bitmap?): ByteArray? {
            if (bm == null) {
                return null
            } else {
                val baos: ByteArrayOutputStream? = ByteArrayOutputStream()
                bm.compress(CompressFormat.PNG, 100, baos)
                return baos.toByteArray()
            }
        }

        private fun Bytes2Bimap(b: ByteArray?): Bitmap? {
            return if (b.size == 0) null else BitmapFactory.decodeByteArray(b, 0, b.size)
        }

        private fun drawable2Bitmap(drawable: Drawable?): Bitmap? {
            if (drawable == null) {
                return null
            } else {
                val w: Int = drawable.getIntrinsicWidth()
                val h: Int = drawable.getIntrinsicHeight()
                val config: Config? =
                    if (drawable.getOpacity() !== PixelFormat.OPAQUE) Config.ARGB_8888 else Config.RGB_565
                val bitmap: Bitmap? = Bitmap.createBitmap(w, h, config)
                val canvas: Canvas? = Canvas(bitmap)
                drawable.setBounds(0, 0, w, h)
                drawable.draw(canvas)
                return bitmap
            }
        }

        private fun bitmap2Drawable(bm: Bitmap?): Drawable? {
            if (bm == null) {
                return null
            } else {
                val bd: BitmapDrawable? = BitmapDrawable(bm)
                bd.setTargetDensity(bm.getDensity())
                return BitmapDrawable(bm)
            }
        }
    }

    inner class ACacheManager private constructor(
        cacheDir: File?,
        sizeLimit: Long,
        countLimit: Int
    ) {
        private val cacheSize: AtomicLong?
        private val cacheCount: AtomicInteger?
        private val sizeLimit: Long
        private val countLimit: Int
        private val lastUsageDates: Map<File?, Long?>?
        protected var cacheDir: File?
        private fun calculateCacheSizeAndCacheCount() {
            (Thread(object : Runnable() {
                fun run() {
                    var size: Int = 0
                    var count: Int = 0
                    val cachedFiles: Array<File?>? = cacheDir.listFiles()
                    if (cachedFiles != null) {
                        val var4: Array<File?>? = cachedFiles
                        val var5: Int = cachedFiles.size
                        for (var6 in 0 until var5) {
                            val cachedFile: File? = var4.get(var6)
                            size = (size as Long + calculateSize(cachedFile)) as Int
                            ++count
                            lastUsageDates.put(cachedFile, cachedFile.lastModified())
                        }
                        cacheSize.set(size as Long)
                        cacheCount.set(count)
                    }
                }
            })).start()
        }

        private fun put(file: File?) {
            var valueSize: Long
            var curCacheCount: Int = cacheCount.get()
            while (curCacheCount + 1 > countLimit) {
                valueSize = removeNext()
                cacheSize.addAndGet(-valueSize)
                curCacheCount = cacheCount.addAndGet(-1)
            }
            cacheCount.addAndGet(1)
            valueSize = calculateSize(file)
            var freedSize: Long
            var curCacheSize: Long = cacheSize.get()
            while (curCacheSize + valueSize > sizeLimit) {
                freedSize = removeNext()
                curCacheSize = cacheSize.addAndGet(-freedSize)
            }
            cacheSize.addAndGet(valueSize)
            val currentTime: Long? = System.currentTimeMillis()
            file.setLastModified(currentTime)
            lastUsageDates.put(file, currentTime)
        }

        private operator fun get(key: String?): File? {
            val file: File? = newFile(key)
            val currentTime: Long? = System.currentTimeMillis()
            file.setLastModified(currentTime)
            lastUsageDates.put(file, currentTime)
            return file
        }

        private fun newFile(key: String?): File? {
            if (!cacheDir.exists()) {
                cacheDir.mkdirs()
            }
            return File(cacheDir, key.hashCode().toString() + "")
        }

        private fun remove(key: String?): Boolean {
            val image: File? = this.get(key)
            return image.delete()
        }

        private fun clear() {
            lastUsageDates.clear()
            cacheSize.set(0L)
            val files: Array<File?>? = cacheDir.listFiles()
            if (files != null) {
                val var2: Array<File?>? = files
                val var3: Int = files.size
                for (var4 in 0 until var3) {
                    val f: File? = var2.get(var4)
                    f.delete()
                }
            }
        }

        private fun removeNext(): Long {
            if (lastUsageDates.isEmpty()) {
                return 0L
            } else {
                var oldestUsage: Long? = null
                var mostLongUsedFile: File? = null
                val entries: Set<Entry<File?, Long?>?>? = lastUsageDates.entrySet()
                synchronized(lastUsageDates, {
                    val var5: Iterator? = entries.iterator()
                    while (true) {
                        if (!var5.hasNext()) {
                            break
                        }
                        val entry: Entry<File?, Long?>? = var5.next() as Entry?
                        if (mostLongUsedFile == null) {
                            mostLongUsedFile = entry.getKey() as File?
                            oldestUsage = entry.getValue() as Long?
                        } else {
                            val lastValueUsage: Long? = entry.getValue() as Long?
                            if (lastValueUsage < oldestUsage) {
                                oldestUsage = lastValueUsage
                                mostLongUsedFile = entry.getKey() as File?
                            }
                        }
                    }
                })
                val fileSize: Long = calculateSize(mostLongUsedFile)
                if (mostLongUsedFile.delete()) {
                    lastUsageDates.remove(mostLongUsedFile)
                }
                return fileSize
            }
        }

        private fun calculateSize(file: File?): Long {
            return file.length()
        }

        init {
            lastUsageDates = Collections.synchronizedMap(HashMap())
            this.cacheDir = cacheDir
            this.sizeLimit = sizeLimit
            this.countLimit = countLimit
            cacheSize = AtomicLong()
            cacheCount = AtomicInteger()
            calculateCacheSizeAndCacheCount()
        }
    }

    internal inner class xFileOutputStream constructor(file: File?) : FileOutputStream(file) {
        var file: File?
        @Override
        @Throws(IOException::class)
        fun close() {
            super.close()
            mCache.put(file)
        }

        init {
            this.file = file
        }
    }

    companion object {
        val TIME_HOUR: Int = 3600
        val TIME_DAY: Int = 86400
        private val MAX_SIZE: Int = 50000000
        private val MAX_COUNT: Int = 2147483647
        private val mInstanceMap: Map<String?, ACache?>? = HashMap()
        operator fun get(ctx: Context?): ACache? {
            return get(ctx, "ACache")
        }

        operator fun get(ctx: Context?, cacheName: String?): ACache? {
            val f: File? = DirUtils.getDiskCacheDir(ctx, cacheName)
            return get(ctx, f, 50000000L, 2147483647)
        }

        operator fun get(ctx: Context?, max_zise: Long, max_count: Int): ACache? {
            val f: File? = File(ctx.getCacheDir(), "ACache")
            return get(ctx, f, max_zise, max_count)
        }

        operator fun get(ctx: Context?, cacheDir: File?, max_zise: Long, max_count: Int): ACache? {
            var manager: ACache? = mInstanceMap.get(cacheDir.getAbsoluteFile() + myPid()) as ACache?
            if (manager == null) {
                manager = ACache(ctx, cacheDir, max_zise, max_count)
                mInstanceMap.put(cacheDir.getAbsolutePath() + myPid(), manager)
            }
            return manager
        }

        private fun myPid(): String? {
            return "_" + Process.myPid()
        }
    }

    init {
        var cacheDir: File? = cacheDir
        if (!cacheDir.exists() && !cacheDir.mkdirs()) {
            cacheDir = File(ctx.getCacheDir(), "ACache")
        }
        mCache = ACacheManager(cacheDir, max_size, max_count)
    }
}