package com.zc.baselibrary.utils

import java.util.regex.Pattern

/**
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
object RegEx {
    private const val DEBUG = true
    private val TAG: String= "RegEx"

    // 手机号码
    val PHONENUMBER: String= "^(((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0-9])|(18[0-9]))[0-9]{8})$"

    // 电话号码
    val TELPHONE: String= "^//(?(//d{3})//)?[- ]?(//d{6})[- ]?(//d{4})$"

    // 账号
    val ACCOUNT: String= "^[A-Za-z0-9]{6,16}$"

    // 用户密码
    val PASSWORD: String= "(?!.*[^A-Za-z\\d])((?=.*\\d)(?=.*[A-Za-z]).{6,16})"

    // 判断是否为数字
    val DIGITAL: String = "[0-9]"

    //电话号码
    val DIGITAL_PHONE: String= "^1[0-9]{10}$"

    // 表情
    val FACE: String= "f0[0-9]{2}|f10[0-7]"

    //验证码
    val VERIFICATIONCODE: String= "[0-9]{6}"

    //邮箱验证
    val EMAIL: String= "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}"

    //银行卡
    val BANKCODE: String= "[0-9]{14,21}"

    // \u4e00-\u9fbf
    val REAL: String= "^(?=.*[\\u4e00-\\u9fbf]+)(?=.*\\w*)[\\u4e00-\\u9fbf\\w]{2,15}$"
    val REAL_MORE: String= "^(?=.*[\\u4e00-\\u9fbf]+)(?=.*\\w*)[\\u4e00-\\u9fbf\\w]{2,50}$"
    fun check(regex: String, matchString: String?): Boolean {
        if (matchString == null) {
            return false
        }
        val p = Pattern.compile(regex)
        val m = p.matcher(matchString)
        return m.find()
    }
}