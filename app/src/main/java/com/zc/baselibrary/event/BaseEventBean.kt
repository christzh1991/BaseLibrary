package com.zc.baselibrary.event

/**
 * Created by zhangchao on 19/11/27.
 */
open class BaseEventBean {
    private var mMsg: String? = null
    private var mTag: String? = null

    constructor() {}
    constructor(tag: String?, msg: String?) {
        mMsg = msg
        mTag = tag
    }

    fun setmMsg(mMsg: String?) {
        this.mMsg = mMsg
    }

    fun setmTag(mTag: String?) {
        this.mTag = mTag
    }

    fun getMsg(): String? {
        return mMsg
    }

    fun getmTag(): String? {
        return mTag
    }
}