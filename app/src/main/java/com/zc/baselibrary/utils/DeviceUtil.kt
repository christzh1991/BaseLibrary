//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package com.zc.baselibrary.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Build.VERSION
import android.os.Environment
import android.telephony.TelephonyManager
import android.util.Log
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.net.URLConnection
import java.util.*

/**
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
class DeviceUtil constructor(private val mContext: Context){

    private var tm: TelephonyManager?
    private val SMS_URI_ALL: String = "content://sms/"
    private val CALL_TYPE_IN: String = "0"
    private val CALL_TYPE_OUT: String = "1"
    private val CALL_TYPE_UNRECEIVE: String = "2"

    companion object {
        private var mDeviceUtil: DeviceUtil? = null
        private val QQlist: ArrayList<String?> = ArrayList<String?>()

        @Synchronized
        fun getDeviceUtil(context: Context): DeviceUtil? {
            return DeviceUtil(context)
        }

        fun getSDCardPath(): String {
            val cmd: String = "cat /proc/mounts"
            val run: Runtime? = Runtime.getRuntime()
            try {
                val p: Process? = run?.exec(cmd)
                val `in`: BufferedInputStream = BufferedInputStream(p?.inputStream)
                val inBr: BufferedReader = BufferedReader(InputStreamReader(`in`))
                var lineStr: String?
                while ((inBr.readLine().also { lineStr = it }) != null) {
                    HLogUtils.d("CommonUtil:getSDCardPath$lineStr")
                    if (lineStr?.contains("sdcard") == true && lineStr?.contains(".android_secure") == true) {
                        val strArray: Array<String?>? = lineStr?.split(" ".toRegex())?.toTypedArray()
                        if (strArray != null && strArray.size >= 5) {
                            val result: String = strArray[1]?.replace("/.android_secure", "").toString()
                            return result
                        }
                    }
                    if (p?.waitFor() != 0 && p?.exitValue() == 1) {
                        HLogUtils.d("CommonUtil:getSDCardPath", "命令执行失败!")
                    }
                }
                inBr.close()
                `in`.close()
            } catch (var8: Exception) {
                HLogUtils.d("CommonUtil:getSDCardPath", var8.toString())
                return Environment.getExternalStorageDirectory().path
            }
            return Environment.getExternalStorageDirectory().path
        }

        fun getNetIp(): String? {
            var infoUrl: URL? = null
            var inStream: InputStream? = null
            try {
                infoUrl = URL("https://www.baidu.com")
                val connection: URLConnection = infoUrl.openConnection()
                val httpConnection: HttpURLConnection = connection as HttpURLConnection
                val responseCode: Int = httpConnection.responseCode
                if (responseCode == 200) {
                    inStream = httpConnection.inputStream
                    val reader: BufferedReader =
                        BufferedReader(InputStreamReader(inStream, "utf-8"))
                    val strber: StringBuilder = StringBuilder()
                    var line: String? = null
                    while ((reader.readLine().also({ line = it })) != null) {
                        strber.append(line + "\n")
                    }
                    inStream.close()
                    line = strber.substring(378, 395)
                    line?.replace(" ".toRegex(), "")
                    return line
                }
            } catch (var8: MalformedURLException) {
                var8.printStackTrace()
            } catch (var9: IOException) {
                var9.printStackTrace()
            }
            return null
        }

        fun getNetIp2(): String? {
            var IP: String? = ""
            try {
                val address: String = "http://ip.taobao.com/service/getIpInfo2.php?ip=myip"
                val url: URL = URL(address)
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                connection.useCaches = false
                if (connection.responseCode == 200) {
                    val `in`: InputStream? = connection.inputStream
                    val reader: BufferedReader = BufferedReader(InputStreamReader(`in`))
                    var tmpString: String? = ""
                    val retJSON: StringBuilder = StringBuilder()
                    while ((reader.readLine().also { tmpString = it }) != null) {
                        retJSON.append(tmpString + "\n")
                    }
                    val jsonObject: JSONObject = JSONObject(retJSON.toString())
                    val code: String? = jsonObject.getString("code")
                    if ((code == "0")) {
                        val data: JSONObject? = jsonObject.getJSONObject("data")
                        IP = data?.getString("ip")
                        Log.e("提示", "您的IP地址是：$IP")
                    } else {
                        IP = ""
                        Log.e("提示", "IP接口异常，无法获取IP地址！")
                    }
                } else {
                    IP = ""
                    Log.e("提示", "网络连接异常，无法获取IP地址！")
                }
            } catch (var11: Exception) {
                IP = ""
                Log.e("提示", "获取IP地址时出现异常，异常信息是：$var11")
            }
            return IP
        }
    }

    init {
        tm = mContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
    }

    fun getDeviceModel(): String? {
        return Build.MODEL
    }

    fun getDeviceBrand(): String? {
        return Build.BRAND
    }

    fun getDeviceOS(): String? {
        return VERSION.RELEASE
    }

    fun getQQList(): MutableList<String?> {
        val path: String = getSDPath()
        QQlist.clear()
        traverseFileList(path + File.separator + "tencent/MobileQQ/")
        return QQlist
    }

    private fun getSDPath(): String {
        var sdDir: File? = null
        val sdCardExist: Boolean = (Environment.getExternalStorageState() == "mounted")
        return if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory()
            sdDir.toString()
        } else {
            getSDCardPath()
        }
    }

    private fun traverseFileList(strPath: String) {
        val dir: File = File(strPath)
        val files: Array<File?>? = dir.listFiles()
        if (files != null) {
            for (i in files.indices) {
                if (files[i]!!.isDirectory) {
                    var strFileName: String = files[i]!!.absolutePath
                    if (strFileName.contains("/")) {
                        strFileName = strFileName.substring("/".lastIndexOf(strFileName) + 1)
                    }
                    println("---$strFileName")
                    if (StringUtils.isNumeric(strFileName)) {
                        QQlist.add(strFileName)
                    }
                }
            }
        }
    }

    @SuppressLint("DefaultLocale")
    fun getIp(): String {
        val wifiManager: WifiManager =
            mContext.applicationContext?.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo: WifiInfo = wifiManager.connectionInfo
        val ipAddress: Int = wifiInfo.ipAddress
        val ip: String = String.format(
            "%d.%d.%d.%d",
            ipAddress and 255,
            ipAddress shr 8 and 255,
            ipAddress shr 16 and 255,
            ipAddress shr 24 and 255
        )
        return ip
    }

}