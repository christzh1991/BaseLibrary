package com.zc.baselibrary.cache.cache

/**
 * 内存缓存和磁盘缓存具备的功能列表：
 *
 *
 * put：将数据保存到本地（内存或磁盘）
 * get：从本地取回数据，update表示强制返回null，这在网络请求更新数据时可以发挥作用
 * contains：判断对应键值的数据是否存在
 * remove：移除对应键值的数据
 * clear：清除所有缓存
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
interface ICache {
    open fun put(key: String?, data: ByteArray?): Boolean
    open operator fun get(key: String?, update: Boolean): ByteArray?
    open operator fun contains(key: String?): Boolean
    open fun remove(key: String?): Boolean
    open fun clear(): Boolean
}