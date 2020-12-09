package com.hh.hlibrary.utils

import android.app.Application

/**
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
class AppPreferences constructor(context: Context?) {
    private val mPreferences: SharedPreferences?
    fun getUUID(): String? {
        val uuid: String? = mPreferences.getString(UUID_KEY, "")
        return uuid
    }

    fun setUUID(uuid: String?): Boolean {
        val editor: Editor? = mPreferences.edit()
        editor.putString(UUID_KEY, uuid)
        return editor.commit()
    }

    fun getAuthkey(): String? {
        val uuid: String? = mPreferences.getString(AUTH_KEY, "")
        return uuid
    }

    fun setAuthkey(authkey: String?): Boolean {
        val editor: Editor? = mPreferences.edit()
        editor.putString(AUTH_KEY, authkey)
        return editor.commit()
    }

    fun getAuthflag(): Boolean {
        val uuid: Boolean = mPreferences.getBoolean(AUTH_FLAG, false)
        return uuid
    }

    fun setAuthflag(authflag: Boolean): Boolean {
        val editor: Editor? = mPreferences.edit()
        editor.putBoolean(AUTH_FLAG, authflag)
        return editor.commit()
    }

    fun getFirstRefreshHome(): Boolean {
        val firstFlag: Boolean = mPreferences.getBoolean(FIRST_REFRESH_HOME, false)
        return firstFlag
    }

    fun setFirstRefreshHome(firstFlag: Boolean): Boolean {
        val editor: Editor? = mPreferences.edit()
        editor.putBoolean(FIRST_REFRESH_HOME, firstFlag)
        return editor.commit()
    }

    fun setUpdateVersion(version: Int): Boolean {
        val editor: Editor? = mPreferences.edit()
        editor.putInt(UPDATE_SUCCESS, version)
        return editor.commit()
    }

    fun getPreVersion(): Int {
        val lastVersion: Int = mPreferences.getInt(UPDATE_SUCCESS, 0)
        return lastVersion
    }

    companion object {
        private val PREFERENCE: String? = "vtm"
        private val UUID_KEY: String? = "uuid_key"
        private val AUTH_KEY: String? = "auth_key"
        private val AUTH_FLAG: String? = "auth_flag"
        private val UPDATE_SUCCESS: String? = "update_success_version"
        private val FIRST_REFRESH_HOME: String? = "first_refresh_home"
        private var mHaierPreference: AppPreferences? = null
        fun init(application: Application?) {
            if (mHaierPreference == null) {
                mHaierPreference = AppPreferences(application)
            }
        }

        fun getInstance(): AppPreferences? {
            return mHaierPreference
        }
    }

    init {
        mPreferences = context.getSharedPreferences(
            PREFERENCE,
            Context.MODE_PRIVATE
        )
    }
}