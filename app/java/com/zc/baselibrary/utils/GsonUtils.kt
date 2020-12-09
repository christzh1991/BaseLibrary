package com.hh.hlibrary.utils

import com.google.gson.Gson

/**
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
object GsonUtils {
    private val gson: Gson? = Gson()

    /**
     * 转成bean
     *
     * @param jsonData
     * @param cls
     * @return
     */
    fun <T> parseJsonWithGson(jsonData: String?, cls: Class<T?>?): T? {
        var t: T? = null
        if (gson != null) {
            t = gson.fromJson(jsonData, cls)
        }
        return t
    }

    fun <T> parseJsonArrayWithGson(jsonData: String?, type: Class<T?>?): List<T?>? {
        val gson: Gson? = Gson()
        val mList: List<T?>? = ArrayList()
        val array: JsonArray? = JsonParser().parse(jsonData).getAsJsonArray()
        for (elem: JsonElement? in array) {
            mList.add(gson.fromJson(elem, type))
        }
        //List<T> result = gson.fromJson(jsonData, new TypeToken<List<T>>() {}.getType());
        return mList
    }

    /**
     * 转成json
     *
     * @param object
     * @return
     */
    fun getJsonStrByObj(`object`: Object?): String? {
        var gsonString: String? = null
        if (gson != null) {
            gsonString = gson.toJson(`object`)
        }
        return gsonString
    }

    /**
     * 转成list
     *
     * @param gsonString
     * @param cls
     * @return
     */
    fun <T> GsonToList(gsonString: String?, cls: Class<T?>?): List<T?>? {
        var list: List<T?>? = null
        if (gson != null) {
            list = gson.fromJson(gsonString, object : TypeToken<List<T?>?>() {}.getType())
        }
        return list
    }

    /**
     * 转成list中有map的
     *
     * @param gsonString
     * @return
     */
    fun <T> GsonToListMaps(gsonString: String?): List<Map<String?, T?>?>? {
        var list: List<Map<String?, T?>?>? = null
        if (gson != null) {
            list = gson.fromJson(
                gsonString,
                object : TypeToken<List<Map<String?, T?>?>?>() {}.getType()
            )
        }
        return list
    }

    /**
     * 转成map的
     *
     * @param gsonString
     * @return
     */
    fun <T> GsonToMaps(gsonString: String?): Map<String?, T?>? {
        var map: Map<String?, T?>? = null
        if (gson != null) {
            map = gson.fromJson(gsonString, object : TypeToken<Map<String?, T?>?>() {}.getType())
        }
        return map
    }
}