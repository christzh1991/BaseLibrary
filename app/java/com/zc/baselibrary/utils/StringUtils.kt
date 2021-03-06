package com.hh.hlibrary.utils

import android.content.Context

/**
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
object StringUtils {
    private val DEBUG: Boolean = false
    private val TAG: String? = "StringUtils"
    private val BUFFER_SIZE: Int = 1024
    private val ENCODING_FORMAT: String? = "UTF-8"

    /**
     * 获取字符串中的数字
     *
     * @param string
     * @return
     */
    fun getDigitalFromString(string: String?): Int {
        val p: Pattern? = Pattern.compile(RegEx.DIGITAL)
        val m: Matcher? = p.matcher(string)
        val str: String? = m.replaceAll("").trim()
        if (isEmpty(str)) {
            return 0
        } else {
            return Integer.parseInt(str)
        }
    }

    /***
     * 文件输入流 转字符串
     *
     * @param in InputStream
     * @return String
     * @throws Exception
     */
    @Throws(Exception::class)
    fun inputStreamToString(`in`: InputStream?): String? {
        val outStream: ByteArrayOutputStream? = ByteArrayOutputStream()
        var data: ByteArray? = ByteArray(BUFFER_SIZE)
        var count: Int = -1
        while ((`in`.read(data, 0, BUFFER_SIZE).also({ count = it })) != -1) {
            outStream.write(data, 0, count)
        }
        data = null
        `in`.close()
        return String(outStream.toByteArray(), ENCODING_FORMAT)
    }

    /**
     * 获取***手机号
     */
    fun getPhoneNum(@NonNull phone: String?): String? {
        if (!isEmpty(phone)) {
            return phone.substring(0, 3).toString() + "****" + phone.substring(7, 11)
        } else {
            return ""
        }
    }

    /***
     * 安全姓名
     */
    fun safeName(str: String?): String? {
        if (!isEmpty(str)) {
            val sb: StringBuilder? = StringBuilder()
            for (i in 0 until str.length() - 1) {
                sb.append("*")
            }
            return sb.append(str.substring(str.length() - 1, str.length())).toString()
        }
        return str
    }

    /***
     * 安全身份证
     */
    fun safecardNum(str: String?): String? {
        if (!isEmpty(str)) {
            val s: String? = str.substring(str.length() - 1)
            val s_: String? = str.substring(0, 6)
            return s_.toString() + "***********" + s
        }
        return str
    }

    /***
     * 判断一个字符串非空
     *
     * @param str String
     * @return true is null,false is not null.
     */
    fun isEmpty(str: String?): Boolean {
        return (str == null) || str.equals("") || (str.length() === 0) || str.equals("null")
    }

    /***
     * 判断一个字符串非空
     *
     * @param str String
     * @return true is null,false is not null.
     */
    fun isEmpty(str: Editable?): Boolean {
        return (str == null) || (str.toString() == null) || str.toString()
            .equals("") || (str.toString().length() === 0)
    }

    /**
     * 半角字符转换为全角字符
     *
     * @param input
     * @return FanLei
     */
    fun ToDBC(input: String?): String? {
        val c: CharArray? = input.toCharArray()
        for (i in c.indices) {
            if (c.get(i) == 12288) {
                c.get(i) = 32 as Char
                continue
            }
            if (c.get(i) > 65280 && c.get(i) < 65375) {
                c.get(i) = (c.get(i) - 65248) as Char
            }
        }
        return String(c)
    }

    /**
     * 替换、过滤特殊字符
     *
     * @param str
     * @return
     * @throws PatternSyntaxException FanLei
     */
    @Throws(PatternSyntaxException::class)
    fun StringFilter(str: String?): String? {
        var str: String? = str
        str = str.replaceAll("【", "[").replaceAll("】", "]").replaceAll("！", "!") // 替换中文标号
        val regEx: String? = "[『』]" // 清除掉特殊字符
        val p: Pattern? = Pattern.compile(regEx)
        val m: Matcher? = p.matcher(str)
        return m.replaceAll("").trim()
    }

    /**
     * 验证手机格式
     * 格式正确返回null
     * 否则返回错误提示
     */
    fun isMobileNO(mobiles: String?, pattern: String?): String? {
        var pattern: String? = pattern
        if (pattern == null) {
            pattern = RegEx.DIGITAL_PHONE
        }
        if (TextUtils.isEmpty(mobiles)) {
            return "手机号不能为空"
        } else {
            if (!mobiles.matches(pattern)) {
                return "手机号码不正确"
            }
        }
        //        校验通过返回null
        return null
    }

    /**
     * 检查字符串是否为电话号码的方法,并返回true or false的判断值
     */
    fun isPhoneNumberValid(phoneNumber: String?, pattern: String?): String? {
        /** 可接受的电话格式有:
         * ^//(? : 可以使用 "(" 作为开头
         * (//d{3}): 紧接着三个数字
         * //)? : 可以使用")"接续
         * [- ]? : 在上述格式后可以使用具选择性的 "-".
         * (//d{3}) : 再紧接着三个数字
         * [- ]? : 可以使用具选择性的 "-" 接续.
         * (//d{5})$: 以五个数字结束.
         * 可以比较下列数字格式:
         * (123)456-7890, 123-456-7890, 1234567890, (123)-456-7890
         */
        var pattern: String? = pattern
        if (pattern == null) {
            pattern = RegEx.TELPHONE
        }
        if (TextUtils.isEmpty(phoneNumber)) {
            return "请输入手机号"
        } else {
            if (!phoneNumber.matches(pattern)) {
                return "手机号不正确"
            }
        }
        return null
    }

    /**
     * 验证用户名格式
     *
     * @param account
     * @param pattern FanLei
     */
    fun isAccount(account: String?, pattern: String?): String? {
        var pattern: String? = pattern
        if (pattern == null) {
            pattern = RegEx.ACCOUNT
        }
        if (TextUtils.isEmpty(account)) {
            return "用户名不能为空"
        } else {
            if (!account.matches(pattern)) {
                return "用户名不正确"
            }
        }
        return null
    }

    /**
     * 验证密码格式
     *
     * @param pattern FanLei
     */
    fun isPassword(pwd: String?, pattern: String?): String? {
        var pattern: String? = pattern
        if (pattern == null) {
            pattern = RegEx.ACCOUNT
        }
        if (TextUtils.isEmpty(pwd)) {
            return "密码不能为空"
        } else {
            if (!pwd.matches(pattern)) {
                return "密码不正确"
            }
        }
        return null
    }

    /**
     * 本地校验手机号码
     *
     * @return
     */
    fun validPhone(act: Context?, phone: String?): Boolean {
        var isValid: Boolean = true
        if (!TextUtils.isEmpty(isMobileNO(phone, "[1]\\d{10}"))) {
            isValid = false
            Toast.makeText(act, isMobileNO(phone, "[1]\\d{10}"), Toast.LENGTH_SHORT).show()
        }
        return isValid
    }

    /**
     * 本地校验密码
     *
     * @return
     */
    fun validPwd(act: Context?, pwd: String?): Boolean {
        var isValid: Boolean = true
        if (!TextUtils.isEmpty(
                isPassword(
                    pwd,
                    "([0-9](?=[0-9]*?[a-zA-Z])\\w{5,})|([a-zA-Z](?=[a-zA-Z]*?[0-9])\\w{5,})"
                )
            )
        ) {
            isValid = false
            Toast.makeText(
                act,
                isPassword(
                    pwd,
                    "([0-9](?=[0-9]*?[a-zA-Z])\\w{5,})|([a-zA-Z](?=[a-zA-Z]*?[0-9])\\w{5,})"
                ),
                Toast.LENGTH_SHORT
            ).show()
        }
        return isValid
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    fun isEmpty(str: CharSequence?): Boolean {
        return (str == null) || (str.length() === 0) || "null".equals(str)
    }

    /**
     * md5加密
     *
     * @param string
     * @return
     */
    fun md5(string: String?): String? {
        if (isEmpty(string)) {
            return ""
        }
        var hash: ByteArray? = null
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"))
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        val hex: StringBuilder? = StringBuilder(hash.size * 2)
        for (b: Byte in hash) {
            if ((b and 0xFF) < 0x10) hex.append("0")
            hex.append(Integer.toHexString(b and 0xFF))
        }
        return hex.toString()
    }

    /**
     * 手机号格式
     */
    fun ToPhoneCod(str: String?): String? {
        var str: String? = str
        str = str.replace(" ", "")
        val length: Int = str.length()
        if (str != null && length > 3) {
            for (i in 0 until length / 4) {
                val end: Int = if (i > 0) (i + 1) * 5 - 2 else 3
                if (end > length) {
                    continue
                }
                val start: String? = str.substring(0, end)
                val ends: String? = str.substring(end)
                if (start.endsWith(" ") || ends.startsWith(" ")) {
                    continue
                }
                str = start.toString() + " " + ends
            }
        }
        return str.trim()
    }

    fun ToPhoneCodeNum(str: String?): String? {
        var str: String? = str
        str = str.replace(" ", "")
        return str
    }

    /**
     * 转换银行卡形式
     */
    fun ToBankCode(str: String?): String? {
        var str: String? = str
        str = str.replace(" ", "")
        var length: Int = str.length()
        if (str != null && length > 4) {
            for (i in 0 until length / 4) {
                val end: Int = if (i > 0) (i + 1) * 5 - 1 else 4
                if (end > length) {
                    continue
                }
                val start: String? = str.substring(0, end)
                val ends: String? = str.substring(end)
                if (start.endsWith(" ") || ends.startsWith(" ")) {
                    continue
                }
                str = start.toString() + " " + ends
                length = str.length()
            }
        }
        return str.trim()
    }

    /**
     * 银行卡号转换成数字
     */
    @Throws(Exception::class)
    fun ToBankCodeNum(str: String?): String? {
        val code: String? = ToBankCodeString(str)
        val codeL: Long = Long.valueOf(code)
        return code
    }

    /**
     * 银行卡号转换成数字
     */
    @Throws(NumberFormatException::class)
    fun ToBankCodeString(str: String?): String? {
        val ss: Array<String?>? = str.split(" ")
        val sb: StringBuffer? = StringBuffer()
        if (ss != null && ss.size > 0) {
            for (i in ss.indices) {
                sb.append(ss.get(i).trim())
            }
        }
        val code: String? = sb.toString()
        return code
    }
    /*********************************** 身份证验证开始  */
    /**
     * 身份证号码验证 1、号码的结构 公民身份号码是特征组合码，由十七位数字本体码和一位校验码组成。排列顺序从左至右依次为：六位数字地址码， 八位数字出生日期码，三位数字顺序码和一位数字校验码。 2、地址码(前六位数）
     * 表示编码对象常住户口所在县(市、旗、区)的行政区划代码，按GB/T2260的规定执行。 3、出生日期码（第七位至十四位） 表示编码对象出生的年、月、日，按GB/T7408的规定执行，年、月、日代码之间不用分隔符。
     * 4、顺序码（第十五位至十七位） 表示在同一地址码所标识的区域范围内，对同年、同月、同日出生的人编定的顺序号， 顺序码的奇数分配给男性，偶数分配给女性。 5、校验码（第十八位数） （1）十七位数字本体码加权求和公式 S =
     * Sum(Ai * Wi), i = 0, ... , 16 ，先对前17位数字的权求和 Ai:表示第i位置上的身份证号码数字值 Wi:表示第i位置上的加权因子 Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5
     * 8 4 2 （2）计算模 Y = mod(S, 11) （3）通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10 校验码: 1 0 X 9 8 7 6 5 4 3 2
     */
    /**
     * 功能：身份证的有效验证
     *
     * @param IDStr 身份证号
     * @return 有效：返回"" 无效：返回String信息
     * @throws ParseException
     */
    @SuppressWarnings("unchecked")
    @Throws(ParseException::class)
    fun IDCardValidate(IDStr: String?): String? {
        var IDStr: String? = IDStr
        IDStr = IDStr.toLowerCase()
        var errorInfo: String? = "" // 记录错误信息
        val ValCodeArr: Array<String?>? =
            arrayOf("1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2")
        val Wi: Array<String?>? = arrayOf(
            "7",
            "9",
            "10",
            "5",
            "8",
            "4",
            "2",
            "1",
            "6",
            "3",
            "7",
            "9",
            "10",
            "5",
            "8",
            "4",
            "2"
        )
        var Ai: String? = ""
        // ================ 号码的长度 15位或18位 ================
        if (IDStr.length() !== 15 && IDStr.length() !== 18) {
            errorInfo = "请输入真实身份证号"
            //            errorInfo = "身份证号码长度应该为15位或18位。";
            return errorInfo
        }
        // =======================(end)========================

        // ================ 数字 除最后以为都为数字 ================
        if (IDStr.length() === 18) {
            Ai = IDStr.substring(0, 17)
        } else if (IDStr.length() === 15) {
            Ai = IDStr.substring(0, 6).toString() + "19" + IDStr.substring(6, 15)
        }
        if (isNumeric(Ai) == false) {
            errorInfo = "请输入真实身份证号"
            //            errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
            return errorInfo
        }
        // =======================(end)========================

        // ================ 出生年月是否有效 ================
        val strYear: String? = Ai.substring(6, 10) // 年份
        val strMonth: String? = Ai.substring(10, 12) // 月份
        val strDay: String? = Ai.substring(12, 14) // 月份
        if (isDate(strYear.toString() + "-" + strMonth + "-" + strDay) == false) {
            errorInfo = "请输入真实身份证号"
            //            errorInfo = "身份证生日无效。";
            return errorInfo
        }
        val gc: GregorianCalendar? = GregorianCalendar()
        val s: SimpleDateFormat? = SimpleDateFormat("yyyy-MM-dd")
        try {
            if (((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
                        || (gc.getTime()
                    .getTime() - s.parse(strYear.toString() + "-" + strMonth + "-" + strDay)
                    .getTime()) < 0)
            ) {
                errorInfo = "请输入真实身份证号"
                //                errorInfo = "身份证生日不在有效范围。";
                return errorInfo
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) === 0) {
            errorInfo = "请输入真实身份证号"
            //            errorInfo = "身份证月份无效";
            return errorInfo
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) === 0) {
            errorInfo = "请输入真实身份证号"
            //            errorInfo = "身份证日期无效";
            return errorInfo
        }
        // =====================(end)=====================

        // ================ 地区码时候有效 ================
        val h: Hashtable? = GetAreaCode()
        if (h.get(Ai.substring(0, 2)) == null) {
            errorInfo = "请输入真实身份证号"
            //            errorInfo = "身份证地区编码错误。";
            return errorInfo
        }
        // ==============================================

        // ================ 判断最后一位的值 ================
        var TotalmulAiWi: Int = 0
        for (i in 0..16) {
            TotalmulAiWi =
                TotalmulAiWi + Integer.parseInt(String.valueOf(Ai.charAt(i))) * Integer.parseInt(
                    Wi.get(i)
                )
        }
        val modValue: Int = TotalmulAiWi % 11
        val strVerifyCode: String? = ValCodeArr.get(modValue)
        Ai = Ai + strVerifyCode
        if (IDStr.length() === 18) {
            if (Ai.equals(IDStr) === false) {
                errorInfo = "请输入真实身份证号"
                //                errorInfo = "身份证无效，不是合法的身份证号码";
                return errorInfo
            }
        } else {
            return errorInfo
        }
        // =====================(end)=====================
        return errorInfo
    }

    /**
     * 功能：判断字符串是否为数字
     *
     * @param str
     * @return
     */
    fun isNumeric(str: String?): Boolean {
        if (isEmpty(str)) {
            return false
        }
        val pattern: Pattern? = Pattern.compile("[0-9]*")
        val isNum: Matcher? = pattern.matcher(str)
        if (isNum.matches()) {
            return true
        } else {
            return false
        }
    }

    /**
     * 功能：判断字符串是否为日期格式
     *
     * @param strDate
     * @return
     */
    fun isDate(strDate: String?): Boolean {
        val pattern: Pattern? =
            Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$")
        val m: Matcher? = pattern.matcher(strDate)
        if (m.matches()) {
            return true
        } else {
            return false
        }
    }

    /**
     * 功能：设置地区编码
     *
     * @return Hashtable 对象
     */
    @SuppressWarnings("unchecked")
    private fun GetAreaCode(): Hashtable? {
        val hashtable: Hashtable? = Hashtable()
        hashtable.put("11", "北京")
        hashtable.put("12", "天津")
        hashtable.put("13", "河北")
        hashtable.put("14", "山西")
        hashtable.put("15", "内蒙古")
        hashtable.put("21", "辽宁")
        hashtable.put("22", "吉林")
        hashtable.put("23", "黑龙江")
        hashtable.put("31", "上海")
        hashtable.put("32", "江苏")
        hashtable.put("33", "浙江")
        hashtable.put("34", "安徽")
        hashtable.put("35", "福建")
        hashtable.put("36", "江西")
        hashtable.put("37", "山东")
        hashtable.put("41", "河南")
        hashtable.put("42", "湖北")
        hashtable.put("43", "湖南")
        hashtable.put("44", "广东")
        hashtable.put("45", "广西")
        hashtable.put("46", "海南")
        hashtable.put("50", "重庆")
        hashtable.put("51", "四川")
        hashtable.put("52", "贵州")
        hashtable.put("53", "云南")
        hashtable.put("54", "西藏")
        hashtable.put("61", "陕西")
        hashtable.put("62", "甘肃")
        hashtable.put("63", "青海")
        hashtable.put("64", "宁夏")
        hashtable.put("65", "新疆")
        hashtable.put("71", "台湾")
        hashtable.put("81", "香港")
        hashtable.put("82", "澳门")
        hashtable.put("91", "国外")
        return hashtable
    }

    /**
     * 判定输入汉字
     *
     * @param c
     * @return
     */
    fun isChinese(c: Char): Boolean {
        val ub: Character.UnicodeBlock? = Character.UnicodeBlock.of(c)
        if ((ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                    ) || (ub === Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                    ) || (ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A) /*|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS*/) {
            return true
        }
        return false
    }

    /**
     * 检测String是否全是中文
     *
     * @param name
     * @return
     */
    fun checkNameChese(name: String?): Boolean {
        var res: Boolean = true
        if (name.length() < 2 || name.length() > 15) {
            res = false
        }
        val cTemp: CharArray? = name.toCharArray()
        for (i in 0 until name.length()) {
            if (!isChinese(cTemp.get(i))) {
                res = false
                break
            }
        }
        return res
    }

    fun formatString(str: String?): String? {
        val df: DecimalFormat? = DecimalFormat("0.00")
        return df.format(Float.parseFloat(str))
    }

    fun formatNumber(number: Double, decimals: Int): String? {
        val nf: NumberFormat? = NumberFormat.getNumberInstance()
        nf.setMaximumFractionDigits(decimals)
        nf.setMinimumFractionDigits(decimals)
        nf.setGroupingUsed(false)
        nf.setRoundingMode(RoundingMode.HALF_UP)
        return nf.format(number)
    }

    fun formatNumber(number: Float, decimals: Int): String? {
        val nf: NumberFormat? = NumberFormat.getNumberInstance()
        nf.setMaximumFractionDigits(decimals)
        nf.setMinimumFractionDigits(decimals)
        nf.setGroupingUsed(false)
        nf.setRoundingMode(RoundingMode.HALF_UP)
        return nf.format(number)
    }

    @Throws(IllegalAccessException::class)
    fun <T> equalsObject(t1: T?, t2: T?): Boolean {
        if (t1 == null && t2 == null) {
            return true
        }
        if ((t1 == null && t2 != null) || (t2 == null && t1 != null)) {
            return false
        }
        val class1: Class<*>? = t1.getClass()
        val fs: Array<Field?>? = class1.getDeclaredFields()
        for (i in fs.indices) {
            val f: Field? = fs.get(i)
            f.setAccessible(true) //设置些属性是可以访问的
            var val1: Object? = f.get(t1) //得到此属性的值
            var val2: Object? = f.get(t2) //得到此属性的值
            if (val1 == null && val2 == null) {
                continue
            }
            if ((val1 == null && val2 != null) || (val2 == null && val1 != null)) {
                return false
            }
            if (!val1.equals(val2)) {
                return false
            }
            val type: String? = f.getType().toString() //得到此属性的类型
            if (type.endsWith("String")) {
                if (val1 == null) {
                    val1 = ""
                }
                if (val2 == null) {
                    val2 = ""
                }
            }
        }
        return true
    }
}