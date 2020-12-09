package com.zc.baselibrary

import android.content.Context
import android.util.Log

/**
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
class UserProfile constructor(private var mContext: Context?) {

    private var mToken: String? = null
    private var mAccountId: Int = 0
    private var mShowPreferencesTimes: Int = 0

    fun setFirst() {
        if (DEBUG) {
            Log.v(TAG, "setFirst()")
        }
        mPreferences.edit().putBoolean(Preferences_Boolean_IsFirst, false).apply()
    }

    fun isFirst(): Boolean {
        if (DEBUG) {
            Log.v(TAG, "isFirst()")
        }
        return mPreferences.getBoolean(Preferences_Boolean_IsFirst, true)
    }

    /**
     * 获取用户token
     *
     * @return
     */
    fun getmToken(): String? {
        if (mToken != null && ("" == mToken)) {
            return mToken
        } else if (Companion.mContext != null) {
            mToken = mPreferences.getString(PREFERENCES_TOKEN, "")
        }
        return mToken
    }

    fun setmToken(token: String?) {
        if (Companion.mContext != null) {
            mToken = token
            mPreferences.edit().putString(PREFERENCES_TOKEN, token).apply()
        }
    }

    fun getAccountId(): Int {
        if (mAccountId != 0) {
            return mAccountId
        } else if (Companion.mContext != null) {
            mAccountId = mPreferences.getInt(ACCOUNT_ID, 0)
        }
        return mAccountId
    }

    fun getAccountIdSQL(): String? {
        if (getAccountId() > 0 /* && false*/) {
            return "=" + getAccountId()
        } else {
            return " is NULL"
        }
    }

    fun setAccountId(accountId: Int) {
        if (Companion.mContext != null) {
            mAccountId = accountId
            mPreferences.edit().putInt(ACCOUNT_ID, mAccountId).apply()
        }
    }

    fun setMsgRemind(isRemind: Int) {
        if (Companion.mContext != null) {
            mPreferences.edit().putInt(MESSAGE_NOTIFY_SWITCH, isRemind).apply()
        }
    }

    fun getMsgRemind(): Int {
        var isRemind: Int = 0
        if (Companion.mContext != null) {
            isRemind = mPreferences.getInt(MESSAGE_NOTIFY_SWITCH, 0)
        }
        return isRemind
    }

    fun getVersionCode(): Int {
        val versionCode: Int = mPreferences.getInt(VERSION_CODE, 1)
        return versionCode
    }

    fun setVersionCode(versionCode: Int) {
        if (Companion.mContext != null) {
            mPreferences.edit().putInt(VERSION_CODE, versionCode).apply()
        }
    }

    /**
     * 获取当前用户登录信息
     *
     * @return
     */
    fun getIsLogin(): Boolean {
        val islogin: Boolean = !StringUtils.isEmpty(getCurrentUser())
        return islogin
    }

    /**
     * 获取当前用户账号
     *
     * @return
     */
    fun getCurrentUser(): String? {
        val current_user: String? = mPreferences.getString(PREFERENCES_CURRENT_USER, "")
        return current_user
    }

    /**
     * 记录重置密码token
     *
     * @param islogin
     */
    fun setCurrentUser(islogin: String?) {
        mPreferences.edit().putString(PREFERENCES_CURRENT_USER, islogin).apply()
    }

    /**
     * 获取当前用户是否已授权
     *
     * @return
     */
    fun isPermissoned(): Boolean? {
        val ispermissoned: Boolean? = mPreferences.getBoolean(ISPERMISSONED, true)
        return ispermissoned
    }

    /**
     * 当前用户授权更改
     *
     * @param ispermissoned
     */
    fun setPermissoned(ispermissoned: Boolean?) {
        mPreferences.edit().putBoolean(ISPERMISSONED, ispermissoned).apply()
    }

    /**
     * 获取当前用户是否已授权
     *
     * @return
     */
    fun IsOpen(): Boolean? {
        val ispermissoned: Boolean? = mPreferences.getBoolean(PREFERENCES_IS_OPEN, false)
        return ispermissoned
    }

    /**
     * 当前用户授权更改
     *
     * @param isopen
     */
    fun setOpen(isopen: Boolean?) {
        mPreferences.edit().putBoolean(PREFERENCES_IS_OPEN, isopen).apply()
    }

    /**
     * 获取用户token
     *
     * @return
     */
    fun getDueTime(): String? {
        val duetime: String? = mPreferences.getString(PREFERENCES_DUE_TIME, "")
        return duetime
    }

    fun setDueTime(duetime: String?) {
        if (Companion.mContext != null) {
            mPreferences.edit().putString(PREFERENCES_DUE_TIME, duetime).apply()
        }
    }

    /**
     * 获取最新登录用户信息
     *
     * @return
     */
    fun getUserAccount(): T? {
        if (Companion.mContext != null) {
            return aCache.getAsObject(POJO_CACHE_KEY_CURRENT_USER) as UserModel?
        }
        return null
    }

    /**
     * 记录最新登录用户信息
     *
     * @param account
     */
    fun setUserAccount(account: Any?) {
        if (account != null) {
            aCache.remove(POJO_CACHE_KEY_CURRENT_USER)
            aCache.put(POJO_CACHE_KEY_CURRENT_USER, account)
        }
    }

    fun getShowPreferencesTimes(): Int {
        mShowPreferencesTimes =
            mPreferences.getInt(getAccountId().toString() + SHOW_PREFERENCES_TIMES, 0)
        return mShowPreferencesTimes
    }

    fun setShowPreferencesTimes(showPreferencesTimes: Int) {
        if (Companion.mContext != null) {
            mShowPreferencesTimes = showPreferencesTimes
            mPreferences.edit()
                .putInt(getAccountId().toString() + SHOW_PREFERENCES_TIMES, mShowPreferencesTimes)
                .apply()
        }
    }

    /**
     * 获取当前用户账号
     *
     * @return
     */
    fun getDurationOfUseThisWeek(): Int {
        val durationOfUseThisWeek: Int =
            mPreferences.getInt(getAccountId().toString() + DURATION_OF_USE_THIS_WEEK, 0)
        return durationOfUseThisWeek
    }

    /**
     * 记录本周使用时长
     *
     * @param durationofusethisweek
     */
    fun setDurationOfUseThisWeek(durationofusethisweek: Int) {
        if (Companion.mContext != null) {
            mPreferences.edit().putInt(
                getAccountId().toString() + DURATION_OF_USE_THIS_WEEK,
                durationofusethisweek
            ).apply()
        }
    }

    /**
     * 获取当前用户最後一次启动定时器时间
     *
     * @return
     */
    fun getLastTimerStartDate(): String? {
        val lastTimerStartDate: String? =
            mPreferences.getString(LAST_TIMER_START_DATE + getAccountId(), "")
        return lastTimerStartDate
    }

    /**
     * 记录当前用户最後一次启动定时器时间
     *
     * @param lastTimerStartDate
     */
    fun setLastTimerStartDate(lastTimerStartDate: String?) {
        mPreferences.edit().putString(LAST_TIMER_START_DATE + getAccountId(), lastTimerStartDate)
            .apply()
    }

    fun ClearUser() {
        mToken = null
        mAccountId = 0
        mPreferences.edit().remove(PREFERENCES_CURRENT_USER).apply()
        mPreferences.edit().remove(ACCOUNT_ID).apply()
        mPreferences.edit().remove(PREFERENCES_TOKEN).apply()
        mPreferences.edit().remove(PREFERENCES_IS_OPEN).apply()
        mPreferences.edit().remove(PREFERENCES_DUE_TIME).apply()
        aCache.remove(POJO_CACHE_KEY_CURRENT_USER)
    }

    companion object {
        private val DEBUG: Boolean = false
        private val TAG: String? = "UserProfile"

        /**
         * 最新用户登录信息key
         */
        private val POJO_CACHE_KEY_CURRENT_USER: String? = "CurrentUser"

        /**
         * 第一次用户启动信息key
         */
        val Preferences_Boolean_IsFirst: String? = "Preferences_Boolean_IsFirst"
        val PREFERENCES_CURRENT_USER: String? = "Preferences_Current_User"
        val PREFERENCES_IS_OPEN: String? = "Preferences_Is_Open"
        val PREFERENCES_DUE_TIME: String? = "Preferences_Due_Time"
        private val ISPERMISSONED: String? = "isPermissoned"
        private val PREFERENCES_TOKEN: String? = "token"
        private val ACCOUNT_ID: String? = "account_id"
        private val VERSION_CODE: String? = "version_code"

        //消息提醒开关
        private val MESSAGE_NOTIFY_SWITCH: String? = "message_notify_switch"

        /**
         * 展示偏好设置次数
         */
        private val SHOW_PREFERENCES_TIMES: String? = "ShowPreferencesTimes"

        /**
         * 本周使用时长
         */
        private val DURATION_OF_USE_THIS_WEEK: String? = "duration_of_use_this_week"

        /**
         * 最后一次定时器启动日期
         */
        private val LAST_TIMER_START_DATE: String? = "last_timer_start_date"
        private var mInstance: UserProfile? = null
        private var aCache: ACache? = null
        private val mUserAccount: UserModel? = null
        private var mPreferences: SharedPreferences? = null
        fun getInstance(context: Context?): UserProfile? {
            if (DEBUG) {
                Log.v(TAG, "getInstanse()")
            }
            if (mInstance != null) {
                return mInstance
            }
            mInstance = UserProfile(context)
            return mInstance
        }
    }

    init {
        if (mPreferences == null) {
            mPreferences = PreferenceManager.getDefaultSharedPreferences(Companion.mContext)
        }
        if (aCache == null) {
            aCache = ACache.get(mContext)
        }
    }
}