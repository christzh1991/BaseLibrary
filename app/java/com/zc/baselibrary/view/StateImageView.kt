package com.hh.hlibrary.view

import android.content.Context

/**
 * Created by deadline on 2017/1/11.
 * 根据用户点击状态设置不同的图片
 * @author zhangchao
 * @date  2020年12月3日
 */
class StateImageView constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
    AppCompatImageView(context, attrs, defStyleAttr) {
    private var mNormalDrawable: Drawable?
    private var mPressedDrawable: Drawable?
    private var mUnableDrawable: Drawable?
    private var mDuration: Int = 0
    private val states: Array<IntArray?>?
    private val mStateBackground: StateListDrawable? = null

    constructor(context: Context?) : this(context, null) {}
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0) {}

    /**
     * 设置不同状态下的背景
     *
     * @param normal
     * @param pressed
     * @param unable
     */
    fun setStateBackground(normal: Drawable?, pressed: Drawable?, unable: Drawable?) {
        mNormalDrawable = normal
        mPressedDrawable = pressed
        mUnableDrawable = unable

        //set background
        if (mPressedDrawable != null) {
            mStateBackground.addState(states.get(0), mPressedDrawable)
            mStateBackground.addState(states.get(1), mPressedDrawable)
        }
        if (mUnableDrawable != null) {
            mStateBackground.addState(states.get(3), mUnableDrawable)
        }
        if (mNormalDrawable != null) {
            mStateBackground.addState(states.get(2), mNormalDrawable)
        }
        setBackgroundDrawable(mStateBackground)
    }

    /**
     * 设置动画时长
     *
     * @param duration
     */
    fun setAnimationDuration(@IntRange(from = 0) duration: Int) {
        mDuration = duration
        mStateBackground.setEnterFadeDuration(mDuration)
        mStateBackground.setExitFadeDuration(mDuration)
    }

    init {
        states = arrayOfNulls<IntArray?>(4)
        states.get(0) = intArrayOf(android.R.attr.state_pressed, android.R.attr.state_enabled)
        states.get(1) = intArrayOf(android.R.attr.state_enabled, android.R.attr.state_focused)
        states.get(3) = intArrayOf(-android.R.attr.state_enabled)
        states.get(2) = intArrayOf(android.R.attr.state_enabled)
        val drawable: Drawable? = getBackground()
        if (drawable != null && drawable is StateListDrawable) {
            mStateBackground = drawable as StateListDrawable?
        } else {
            mStateBackground = StateListDrawable()
        }
        val a: TypedArray? = getContext().obtainStyledAttributes(attrs, R.styleable.StateImageView)
        mNormalDrawable = a.getDrawable(R.styleable.StateImageView_normalBackground)
        mPressedDrawable = a.getDrawable(R.styleable.StateImageView_pressedBackground)
        mUnableDrawable = a.getDrawable(R.styleable.StateImageView_unableBackground)
        setStateBackground(mNormalDrawable, mPressedDrawable, mUnableDrawable)
        mDuration = a.getInteger(R.styleable.StateImageView_AnimationDuration, mDuration)
        setAnimationDuration(mDuration)
        a.recycle()
    }
}