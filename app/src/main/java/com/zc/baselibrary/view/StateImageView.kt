package com.zc.baselibrary.view

import android.R
import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import androidx.annotation.IntRange
import androidx.appcompat.widget.AppCompatImageView

/**
 * Created by deadline on 2017/1/11.
 * 根据用户点击状态设置不同的图片
 * @author zhangchao
 * @date  2020年12月3日
 */
class StateImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {
    private var mNormalDrawable: Drawable?
    private var mPressedDrawable: Drawable?
    private var mUnableDrawable: Drawable?
    private var mDuration = 0
    private val states: Array<IntArray?>?
    private val mStateBackground: StateListDrawable? = null

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
        states.get(0) = intArrayOf(R.attr.state_pressed, R.attr.state_enabled)
        states.get(1) = intArrayOf(R.attr.state_enabled, R.attr.state_focused)
        states.get(3) = intArrayOf(-R.attr.state_enabled)
        states.get(2) = intArrayOf(R.attr.state_enabled)
        val drawable: Drawable = getBackground()
        mStateBackground =
            if (drawable != null && drawable is StateListDrawable) {
                drawable as StateListDrawable
            } else {
                StateListDrawable()
            }
        val a: TypedArray = getContext().obtainStyledAttributes(attrs, R.styleable.StateImageView)
        mNormalDrawable = a.getDrawable(R.styleable.StateImageView_normalBackground)
        mPressedDrawable = a.getDrawable(R.styleable.StateImageView_pressedBackground)
        mUnableDrawable = a.getDrawable(R.styleable.StateImageView_unableBackground)
        setStateBackground(mNormalDrawable, mPressedDrawable, mUnableDrawable)
        mDuration = a.getInteger(R.styleable.StateImageView_AnimationDuration, mDuration)
        setAnimationDuration(mDuration)
        a.recycle()
    }
}