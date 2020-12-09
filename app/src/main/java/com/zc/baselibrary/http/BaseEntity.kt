package com.zc.baselibrary.http

import com.google.gson.annotations.SerializedName

/**
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
class BaseEntity<T> {

    @SerializedName("code")
    var code = 0

    @SerializedName("message")
    val message: String? = null

    @SerializedName("data")
    var data: T? = null

    fun isSuccess(): Boolean {
        return code == 200
    }

    override fun toString(): String {
        return "BaseEntity{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}'
    }
}