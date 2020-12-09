package com.hh.hlibrary.utils

import android.content.Context

/**
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
class DeviceUuidFactory constructor(context: Context?) {
    private val weakReference: WeakReference<Context?>?
    protected var uuid: UUID? = null
    fun init() {
        if (uuid == null) {
            synchronized(DeviceUuidFactory::class.java, {
                if (uuid == null) {
                    val appPreferences: AppPreferences? = AppPreferences.getInstance()
                    val id: String? = appPreferences.getUUID()
                    if (!StringUtils.isEmpty(id)) {
                        uuid = UUID.fromString(id)
                    } else {
                        val androidId: String? = Secure.getString(
                            weakReference.get().getContentResolver(),
                            Secure.ANDROID_ID
                        )
                        try {
                            if (!"9774d56d682e549c".equals(androidId)) {
                                uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"))
                            } else {
                                val deviceId: String? = (weakReference.get()
                                    .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?).getDeviceId()
                                uuid =
                                    if (deviceId != null) UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")) else UUID.randomUUID()
                            }
                        } catch (e: UnsupportedEncodingException) {
                            var timestr: Long = System.currentTimeMillis()
                            val random: Random? = Random()
                            timestr = timestr - random.nextInt(100000)
                            if (timestr >= 0) {
                                appPreferences.setUUID(String.valueOf(timestr).toString())
                            } else {
                                appPreferences.setUUID(
                                    String.valueOf(random.nextInt(100000)).toString()
                                )
                            }
                        }
                        appPreferences.setUUID(uuid.toString())
                    }
                }
            })
        }
    }

    init {
        weakReference = WeakReference<Context?>(context)
    }
}