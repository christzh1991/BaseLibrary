package com.zc.baselibrary.http

import com.google.gson.JsonParseException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.text.ParseException
import javax.net.ssl.SSLHandshakeException

/**
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
object ExceptionHandle {
    private const val UNAUTHORIZED = 401
    private const val FORBIDDEN = 403
    private const val NOT_FOUND = 404
    private const val REQUEST_TIMEOUT = 408
    private const val INTERNAL_SERVER_ERROR = 500
    private const val BAD_GATEWAY = 502
    private const val SERVICE_UNAVAILABLE = 503
    private const val GATEWAY_TIMEOUT = 504
    fun handleException(e: Throwable?): ResponseException? {
        //转换成ResponseException,根据状态码判定错误信息
        val ex: ResponseException
        return if (e is HttpException) {
            val httpException = e as HttpException?
            /**
             * 传入状态码，根据状态码判定错误信息
             */
            ex = ResponseException(e, ERROR.HTTP_ERROR)
            when (httpException.code()) {
                UNAUTHORIZED -> ex.message = "未验证"
                FORBIDDEN -> ex.message = "服务禁止访问"
                NOT_FOUND -> ex.message = "服务不存在"
                REQUEST_TIMEOUT -> ex.message = "请求超时"
                GATEWAY_TIMEOUT -> ex.message = "网关超时"
                INTERNAL_SERVER_ERROR -> ex.message = "服务器内部错误"
                BAD_GATEWAY -> {
                }
                SERVICE_UNAVAILABLE -> {
                }
                else -> ex.message = "网络错误"
            }
            ex
        } else if (e is JsonParseException
            || e is JSONException
            || e is ParseException
        ) {
            ex = ResponseException(e, ERROR.PARSE_ERROR)
            ex.message = "解析错误"
            ex
        } else if (e is ConnectException) {
            ex = ResponseException(e, ERROR.NETWORD_ERROR)
            ex.message = "连接失败"
            ex
        } else if (e is SSLHandshakeException) {
            ex = ResponseException(e, ERROR.SSL_ERROR)
            ex.message = "证书验证失败"
            ex
        } else {
            ex = ResponseException(e, ERROR.UNKNOWN)
            ex.message = "未知错误"
            ex
        }
    }

    /**
     * 约定异常
     */
    object ERROR {
        /**
         * 自定义异常
         */
        private const val UNAUTHORIZED = 401 //请求用户进行身份验证
        private const val UNREQUEST = 403 //服务器理解请求客户端的请求，但是拒绝执行此请求
        private const val UNFINDSOURCE = 404 //服务器无法根据客户端的请求找到资源
        private const val SEVERERROR = 500 //服务器内部错误，无法完成请求。

        /**
         * 协议出错
         */
        const val HTTP_ERROR = 1003

        /**
         * 未知错误
         */
        const val UNKNOWN = 1000

        /**
         * 解析错误
         */
        const val PARSE_ERROR = 1001

        /**
         * 网络错误
         */
        const val NETWORD_ERROR = 1002

        /**
         * 证书出错
         */
        const val SSL_ERROR = 1005
    }

    /**
     * 自定义Throwable
     */
    class ResponseThrowable(throwable: Throwable?, var code: Int) : Exception(throwable) {
        var message: String? = null
    }

    /**
     * 服务器异常
     */
    inner class ServerException : RuntimeException() {
        var code = 0
        var message: String? = null
    }

    /**
     * 统一异常类，便于处理
     */
    class ResponseException(throwable: Throwable?, var code: Int) : Exception(throwable) {
        var message: String? = null
    }
}