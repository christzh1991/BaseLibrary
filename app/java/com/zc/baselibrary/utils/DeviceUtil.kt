//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package com.hh.hlibrary.utils

import android.annotation.SuppressLint

/**
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
class DeviceUtil private constructor(context: Context?) {
    private val tm: TelephonyManager?
    private val SMS_URI_ALL: String? = "content://sms/"
    private val CALL_TYPE_IN: String? = "0"
    private val CALL_TYPE_OUT: String? = "1"
    private val CALL_TYPE_UNRECEIVE: String? = "2"
    fun getDeviceModel(): String? {
        return Build.MODEL
    }

    fun getDeviceBrand(): String? {
        return Build.BRAND
    }

    fun getDeviceOS(): String? {
        return VERSION.RELEASE
    }

    fun getQQList(): List<String?>? {
        val path: String? = getSDPath()
        if (path != null) {
            QQlist.clear()
            traverseFileList(path + File.separator.toString() + "tencent/MobileQQ/")
        }
        return QQlist
    }

    fun getSDPath(): String? {
        var sdDir: File? = null
        val sdCardExist: Boolean = Environment.getExternalStorageState().equals("mounted")
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory()
            return sdDir.toString()
        } else {
            return getSDCardPath()
        }
    }

    fun traverseFileList(strPath: String?) {
        val dir: File? = File(strPath)
        val files: Array<File?>? = dir.listFiles()
        if (files != null) {
            for (i in files.indices) {
                if (files.get(i).isDirectory()) {
                    var strFileName: String? = files.get(i).getAbsolutePath()
                    if (strFileName.contains("/")) {
                        strFileName = strFileName.substring("/".lastIndexOf(strFileName) + 1)
                    }
                    System.out.println("---" + strFileName)
                    if (StringUtils.isNumeric(strFileName)) {
                        QQlist.add(strFileName)
                    }
                }
            }
        }
    }

    @SuppressLint(["DefaultLocale"])
    fun getIp(): String? {
        val wifiManager: WifiManager? =
            mContext.getSystemService(Context.WIFI_SERVICE) as WifiManager?
        val wifiInfo: WifiInfo? = wifiManager.getConnectionInfo()
        val ipAddress: Int = wifiInfo.getIpAddress()
        val ip: String? = String.format(
            "%d.%d.%d.%d",
            ipAddress and 255,
            ipAddress shr 8 and 255,
            ipAddress shr 16 and 255,
            ipAddress shr 24 and 255
        )
        return ip
    }

    companion object {
        private var mContext: Context?
        private var mDeviceUtil: DeviceUtil? = null
        private val QQlist: ArrayList<String?>? = ArrayList()
        @Synchronized
        fun getDeviceUtil(context: Context?): DeviceUtil? {
            if (mContext == null) {
                mDeviceUtil = DeviceUtil(context)
            }
            return mDeviceUtil
        }

        fun getSDCardPath(): String? {
            val cmd: String? = "cat /proc/mounts"
            val run: Runtime? = Runtime.getRuntime()
            try {
                val p: Process? = run.exec(cmd)
                val `in`: BufferedInputStream? = BufferedInputStream(p.getInputStream())
                val inBr: BufferedReader? = BufferedReader(InputStreamReader(`in`))
                var lineStr: String?
                while ((inBr.readLine().also({ lineStr = it })) != null) {
                    HLogUtils.d("CommonUtil:getSDCardPath" + lineStr)
                    if (lineStr.contains("sdcard") && lineStr.contains(".android_secure")) {
                        val strArray: Array<String?>? = lineStr.split(" ")
                        if (strArray != null && strArray.size >= 5) {
                            val result: String? = strArray.get(1).replace("/.android_secure", "")
                            return result
                        }
                    }
                    if (p.waitFor() !== 0 && p.exitValue() === 1) {
                        HLogUtils.d("CommonUtil:getSDCardPath", "命令执行失败!")
                    }
                }
                inBr.close()
                `in`.close()
            } catch (var8: Exception) {
                HLogUtils.d("CommonUtil:getSDCardPath", var8.toString())
                return Environment.getExternalStorageDirectory().getPath()
            }
            return Environment.getExternalStorageDirectory().getPath()
        }

        fun GetNetIp(): String? {
            var infoUrl: URL? = null
            var inStream: InputStream? = null
            try {
                infoUrl = URL("https://www.baidu.com")
                val connection: URLConnection? = infoUrl.openConnection()
                val httpConnection: HttpURLConnection? = connection as HttpURLConnection?
                val responseCode: Int = httpConnection.getResponseCode()
                if (responseCode == 200) {
                    inStream = httpConnection.getInputStream()
                    val reader: BufferedReader? =
                        BufferedReader(InputStreamReader(inStream, "utf-8"))
                    val strber: StringBuilder? = StringBuilder()
                    var line: String? = null
                    while ((reader.readLine().also({ line = it })) != null) {
                        strber.append(line.toString() + "\n")
                    }
                    inStream.close()
                    line = strber.substring(378, 395)
                    line.replaceAll(" ", "")
                    return line
                }
            } catch (var8: MalformedURLException) {
                var8.printStackTrace()
            } catch (var9: IOException) {
                var9.printStackTrace()
            }
            return null
        }

        fun GetNetIp2(): String? {
            var IP: String? = ""
            try {
                val address: String? = "http://ip.taobao.com/service/getIpInfo2.php?ip=myip"
                val url: URL? = URL(address)
                val connection: HttpURLConnection? = url.openConnection() as HttpURLConnection?
                connection.setUseCaches(false)
                if (connection.getResponseCode() === 200) {
                    val `in`: InputStream? = connection.getInputStream()
                    val reader: BufferedReader? = BufferedReader(InputStreamReader(`in`))
                    var tmpString: String? = ""
                    val retJSON: StringBuilder? = StringBuilder()
                    while ((reader.readLine().also({ tmpString = it })) != null) {
                        retJSON.append(tmpString.toString() + "\n")
                    }
                    val jsonObject: JSONObject? = JSONObject(retJSON.toString())
                    val code: String? = jsonObject.getString("code")
                    if (code.equals("0")) {
                        val data: JSONObject? = jsonObject.getJSONObject("data")
                        IP = data.getString("ip")
                        Log.e("提示", "您的IP地址是：" + IP)
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
                Log.e("提示", "获取IP地址时出现异常，异常信息是：" + var11.toString())
            }
            return IP
        }
    }

    init {
        mContext = context
        tm = mContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
    }
}