package com.zc.baselibrary.utils

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import java.util.*

/**
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
object GsonUtils {

    private val gson: Gson = Gson()

    /**
     * 转成bean
     *
     * @param jsonData
     * @param cls
     * @return
     */
    fun <T> parseJsonWithGson(jsonData: String?, cls: Class<T?>?): T? {
        var t: T? = null
        t = gson.fromJson(jsonData, cls)
        return t
    }

    fun <T> parseJsonArrayWithGson(jsonData: String, type: Class<T?>): MutableList<T?> {
        val gson: Gson = Gson()
        val mList: MutableList<T?> = ArrayList()
        val array: JsonArray = JsonParser().parse(jsonData).asJsonArray
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
    fun getJsonStrByObj(`object`: Any?): String? {
        var gsonString: String? = null
        gsonString = gson.toJson(`object`)
        return gsonString
    }

    /**
     * 转成list
     *
     * @param gsonString
     * @param cls
     * @return
     */
    fun <T> GsonToList(gsonString: String?, cls: Class<T?>?): MutableList<T?>? {
        var list: MutableList<T?>? = null
        list = gson.fromJson(gsonString, object : TypeToken<MutableList<T?>?>() {}.type)
        return list
    }

    /**
     * 转成list中有map的
     *
     * @param gsonString
     * @return
     */
    fun <T> GsonToListMaps(gsonString: String?): MutableList<MutableMap<String?, T?>?>? {
        var list: MutableList<MutableMap<String?, T?>?>? = null
        list = gson.fromJson(
            gsonString,
            object : TypeToken<MutableList<MutableMap<String?, T?>?>?>() {}.getType()
        )
        return list
    }

    /**
     * 转成map的
     *
     * @param gsonString
     * @return
     */
    fun <T> GsonToMaps(gsonString: String?): MutableMap<String?, T?>? {
        var map: MutableMap<String?, T?>? = null
        map = gson.fromJson(
            gsonString,
            object : TypeToken<MutableMap<String?, T?>?>() {}.getType()
        )
        return map
    }
}