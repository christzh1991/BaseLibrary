package com.hh.hlibrary.http

import com.google.gson.JsonParseException

/**
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
object ExceptionHandle {
    private val UNAUTHORIZED: Int = 401
    private val FORBIDDEN: Int = 403
    private val NOT_FOUND: Int = 404
    private val REQUEST_TIMEOUT: Int = 408
    private val INTERNAL_SERVER_ERROR: Int = 500
    private val BAD_GATEWAY: Int = 502
    private val SERVICE_UNAVAILABLE: Int = 503
    private val GATEWAY_TIMEOUT: Int = 504
    fun handleException(e: Throwable?): ResponseException? {
        //转换成ResponseException,根据状态码判定错误信息
        val ex: ResponseException?
        if (e is HttpException) {
            val httpException: HttpException? = e as HttpException?
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
            return ex
        } else if ((e is JsonParseException
                    || e is JSONException
                    || e is ParseException)
        ) {
            ex = ResponseException(e, ERROR.PARSE_ERROR)
            ex.message = "解析错误"
            return ex
        } else if (e is ConnectException) {
            ex = ResponseException(e, ERROR.NETWORD_ERROR)
            ex.message = "连接失败"
            return ex
        } else if (e is javax.net.ssl.SSLHandshakeException) {
            ex = ResponseException(e, ERROR.SSL_ERROR)
            ex.message = "证书验证失败"
            return ex
        } else {
            ex = ResponseException(e, ERROR.UNKNOWN)
            ex.message = "未知错误"
            return ex
        }
    }

    /**
     * 约定异常
     */
    object ERROR {
        /**
         * 自定义异常
         */
        private val UNAUTHORIZED: Int = 401 //请求用户进行身份验证
        private val UNREQUEST: Int = 403 //服务器理解请求客户端的请求，但是拒绝执行此请求
        private val UNFINDSOURCE: Int = 404 //服务器无法根据客户端的请求找到资源
        private val SEVERERROR: Int = 500 //服务器内部错误，无法完成请求。

        /**
         * 协议出错
         */
        val HTTP_ERROR: Int = 1003

        /**
         * 未知错误
         */
        val UNKNOWN: Int = 1000

        /**
         * 解析错误
         */
        val PARSE_ERROR: Int = 1001

        /**
         * 网络错误
         */
        val NETWORD_ERROR: Int = 1002

        /**
         * 证书出错
         */
        val SSL_ERROR: Int = 1005
    }

    /**
     * 自定义Throwable
     */
    class ResponseThrowable constructor(throwable: Throwable?, var code: Int) :
        Exception(throwable) {
        var message: String? = null
    }

    /**
     * 服务器异常
     */
    inner class ServerException constructor() : RuntimeException() {
        var code: Int = 0
        var message: String? = null
    }

    /**
     * 统一异常类，便于处理
     */
    class ResponseException constructor(throwable: Throwable?, var code: Int) :
        Exception(throwable) {
        var message: String? = null
    }
}