package com.zc.baselibrary.cache.mode

/**
 * 缓存的模式
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
enum class CacheMode {
    /**
     * 不使用缓存，这时缓存将不起作用，也就是调用时全部返回为NULL
     */
    NONE,

    /**
     * 仅使用内存缓存
     */
    ONLY_MEMORY,

    /**
     * 仅使用磁盘缓存
     */
    ONLY_DISK,

    /**
     * 同时使用内存缓存和磁盘缓存
     */
    BOTH
}