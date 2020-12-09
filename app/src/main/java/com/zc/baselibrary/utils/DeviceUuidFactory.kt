package com.zc.baselibrary.utils

import android.content.Context
import android.provider.Settings.Secure
import android.telephony.TelephonyManager
import java.io.UnsupportedEncodingException
import java.lang.ref.WeakReference
import java.util.*

/**
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
class DeviceUuidFactory constructor(context: Context?) {

    private val weakReference: WeakReference<Context?>?
    private var uuid: UUID? = null

    init {
        weakReference = WeakReference(context)
    }

    fun init() {
        if (uuid == null) {
            synchronized(DeviceUuidFactory::class.java) {
                if (uuid == null) {
                    val appPreferences: AppPreferences ? = AppPreferences.Companion.getInstance()
                    val id: String? = appPreferences?.getUUID()
                    if (!StringUtils.isEmpty(id)) {
                        uuid = UUID.fromString(id)
                    } else {
                        val androidId: String? = Secure.getString(
                            weakReference?.get()?.getContentResolver(),
                            Secure.ANDROID_ID
                        )
                        try {
                            uuid = if ("9774d56d682e549c" != androidId) {
                                UUID.nameUUIDFromBytes(androidId?.toByteArray(charset("utf8")))
                            } else {
                                val deviceId: String? = (weakReference?.get()
                                    ?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?)?.getDeviceId()
                                if (deviceId != null) UUID.nameUUIDFromBytes(
                                    deviceId.toByteArray(charset("utf8"))
                                ) else UUID.randomUUID()
                            }
                        } catch (e: UnsupportedEncodingException) {
                            var timeStr: Long = System.currentTimeMillis()
                            val random: Random = Random()
                            timeStr -= random.nextInt(100000)
                            if (timeStr >= 0) {
                                appPreferences?.setUUID(timeStr.toString().toString())
                            } else {
                                appPreferences?.setUUID(
                                    random.nextInt(100000).toString().toString()
                                )
                            }
                        }
                        appPreferences?.setUUID(uuid.toString())
                    }
                }
            }
        }
    }

}