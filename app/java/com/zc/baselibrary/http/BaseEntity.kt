package com.hh.hlibrary.http

import com.google.gson.annotations.SerializedName

/**
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
class BaseEntity<T> constructor() {
    @SerializedName("code")
    private var code: Int = 0

    @SerializedName("message")
    private val message: String? = null

    @SerializedName("data")
    private var data: T? = null
    fun isSuccess(): Boolean {
        return code == 200
    }

    fun getCode(): Int {
        return code
    }

    fun setCode(code: Int) {
        this.code = code
    }

    fun getMsg(): String? {
        return message
    }

    fun getData(): T? {
        return data
    }

    fun setData(data: T?) {
        this.data = data
    }

    @Override
    fun toString(): String {
        return ("BaseEntity{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}')
    }
}