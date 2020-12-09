package com.hh.hlibrary.view

import android.content.Context

/**
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
class StateButton constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
    AppCompatButton(context, attrs, defStyleAttr) {
    //text color
    private var mNormalTextColor: Int = 0
    private var mPressedTextColor: Int = 0
    private var mUnableTextColor: Int = 0
    var mTextColorStateList: ColorStateList? = null

    //animation duration
    private var mDuration: Int = 0

    //radius
    private var mRadius: Float = 0f
    private var mRound: Boolean = false

    //stroke
    private var mStrokeDashWidth: Float = 0f
    private var mStrokeDashGap: Float = 0f
    private var mNormalStrokeWidth: Int = 0
    private var mPressedStrokeWidth: Int = 0
    private var mUnableStrokeWidth: Int = 0
    private var mNormalStrokeColor: Int = 0
    private var mPressedStrokeColor: Int = 0
    private var mUnableStrokeColor: Int = 0

    //background color
    private var mNormalBackgroundColor: Int = 0
    private var mPressedBackgroundColor: Int = 0
    private var mUnableBackgroundColor: Int = 0
    private var mNormalBackground: GradientDrawable? = null
    private var mPressedBackground: GradientDrawable? = null
    private var mUnableBackground: GradientDrawable? = null
    private var states: Array<IntArray?>?
    var mStateBackground: StateListDrawable? = null

    constructor(context: Context?) : this(context, null) {}
    constructor(context: Context?, attrs: AttributeSet?) : this(
        context,
        attrs,
        R.attr.buttonStyle
    ) {
    }

    private fun setup(attrs: AttributeSet?) {
        states = arrayOfNulls<IntArray?>(4)
        val drawable: Drawable? = getBackground()
        if (drawable != null && drawable is StateListDrawable) {
            mStateBackground = drawable as StateListDrawable?
        } else {
            mStateBackground = StateListDrawable()
        }
        mNormalBackground = GradientDrawable()
        mPressedBackground = GradientDrawable()
        mUnableBackground = GradientDrawable()

        //pressed, focused, normal, unable
        states.get(0) = intArrayOf(android.R.attr.state_enabled, android.R.attr.state_pressed)
        states.get(1) = intArrayOf(android.R.attr.state_enabled, android.R.attr.state_focused)
        states.get(3) = intArrayOf(-android.R.attr.state_enabled)
        states.get(2) = intArrayOf(android.R.attr.state_enabled)
        val a: TypedArray? = getContext().obtainStyledAttributes(attrs, R.styleable.StateButton)

        //get original text color as default
        //set text color
        mTextColorStateList = getTextColors()
        val mDefaultNormalTextColor: Int =
            mTextColorStateList.getColorForState(states.get(2), getCurrentTextColor())
        val mDefaultPressedTextColor: Int =
            mTextColorStateList.getColorForState(states.get(0), getCurrentTextColor())
        val mDefaultUnableTextColor: Int =
            mTextColorStateList.getColorForState(states.get(3), getCurrentTextColor())
        mNormalTextColor =
            a.getColor(R.styleable.StateButton_normalTextColor, mDefaultNormalTextColor)
        mPressedTextColor =
            a.getColor(R.styleable.StateButton_pressedTextColor, mDefaultPressedTextColor)
        mUnableTextColor =
            a.getColor(R.styleable.StateButton_unableTextColor, mDefaultUnableTextColor)
        setTextColor()

        //set animation duration
        mDuration = a.getInteger(R.styleable.StateButton_animationDuration, mDuration)
        mStateBackground.setEnterFadeDuration(mDuration)
        mStateBackground.setExitFadeDuration(mDuration)

        //set background color
        mNormalBackgroundColor = a.getColor(R.styleable.StateButton_normalBackgroundColor, 0)
        mPressedBackgroundColor = a.getColor(R.styleable.StateButton_pressedBackgroundColor, 0)
        mUnableBackgroundColor = a.getColor(R.styleable.StateButton_unableBackgroundColor, 0)
        mNormalBackground.setColor(mNormalBackgroundColor)
        mPressedBackground.setColor(mPressedBackgroundColor)
        mUnableBackground.setColor(mUnableBackgroundColor)

        //set radius
        mRadius = a.getDimensionPixelSize(R.styleable.StateButton_radius, 0)
        mRound = a.getBoolean(R.styleable.StateButton_round, false)
        mNormalBackground.setCornerRadius(mRadius)
        mPressedBackground.setCornerRadius(mRadius)
        mUnableBackground.setCornerRadius(mRadius)

        //set stroke
        mStrokeDashWidth = a.getDimensionPixelSize(R.styleable.StateButton_strokeDashWidth, 0)
        mStrokeDashGap = a.getDimensionPixelSize(R.styleable.StateButton_strokeDashWidth, 0)
        mNormalStrokeWidth = a.getDimensionPixelSize(R.styleable.StateButton_normalStrokeWidth, 0)
        mPressedStrokeWidth = a.getDimensionPixelSize(R.styleable.StateButton_pressedStrokeWidth, 0)
        mUnableStrokeWidth = a.getDimensionPixelSize(R.styleable.StateButton_unableStrokeWidth, 0)
        mNormalStrokeColor = a.getColor(R.styleable.StateButton_normalStrokeColor, 0)
        mPressedStrokeColor = a.getColor(R.styleable.StateButton_pressedStrokeColor, 0)
        mUnableStrokeColor = a.getColor(R.styleable.StateButton_unableStrokeColor, 0)
        setStroke()

        //set background
        mStateBackground.addState(states.get(0), mPressedBackground)
        mStateBackground.addState(states.get(1), mPressedBackground)
        mStateBackground.addState(states.get(3), mUnableBackground)
        mStateBackground.addState(states.get(2), mNormalBackground)
        setBackgroundDrawable(mStateBackground)
        a.recycle()
    }

    @Override
    protected fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setRound(mRound)
    }

    /****************** stroke color  */
    fun setNormalStrokeColor(@ColorInt normalStrokeColor: Int) {
        mNormalStrokeColor = normalStrokeColor
        setStroke(mNormalBackground, mNormalStrokeColor, mNormalStrokeWidth)
    }

    fun setPressedStrokeColor(@ColorInt pressedStrokeColor: Int) {
        mPressedStrokeColor = pressedStrokeColor
        setStroke(mPressedBackground, mPressedStrokeColor, mPressedStrokeWidth)
    }

    fun setUnableStrokeColor(@ColorInt unableStrokeColor: Int) {
        mUnableStrokeColor = unableStrokeColor
        setStroke(mUnableBackground, mUnableStrokeColor, mUnableStrokeWidth)
    }

    fun setStateStrokeColor(@ColorInt normal: Int, @ColorInt pressed: Int, @ColorInt unable: Int) {
        mNormalStrokeColor = normal
        mPressedStrokeColor = pressed
        mUnableStrokeColor = unable
        setStroke()
    }

    /****************** stroke width  */
    fun setNormalStrokeWidth(normalStrokeWidth: Int) {
        mNormalStrokeWidth = normalStrokeWidth
        setStroke(mNormalBackground, mNormalStrokeColor, mNormalStrokeWidth)
    }

    fun setPressedStrokeWidth(pressedStrokeWidth: Int) {
        mPressedStrokeWidth = pressedStrokeWidth
        setStroke(mPressedBackground, mPressedStrokeColor, mPressedStrokeWidth)
    }

    fun setUnableStrokeWidth(unableStrokeWidth: Int) {
        mUnableStrokeWidth = unableStrokeWidth
        setStroke(mUnableBackground, mUnableStrokeColor, mUnableStrokeWidth)
    }

    fun setStateStrokeWidth(normal: Int, pressed: Int, unable: Int) {
        mNormalStrokeWidth = normal
        mPressedStrokeWidth = pressed
        mUnableStrokeWidth = unable
        setStroke()
    }

    fun setStrokeDash(strokeDashWidth: Float, strokeDashGap: Float) {
        mStrokeDashWidth = strokeDashWidth
        mStrokeDashGap = strokeDashWidth
        setStroke()
    }

    private fun setStroke() {
        setStroke(mNormalBackground, mNormalStrokeColor, mNormalStrokeWidth)
        setStroke(mPressedBackground, mPressedStrokeColor, mPressedStrokeWidth)
        setStroke(mUnableBackground, mUnableStrokeColor, mUnableStrokeWidth)
    }

    private fun setStroke(mBackground: GradientDrawable?, mStrokeColor: Int, mStrokeWidth: Int) {
        mBackground.setStroke(mStrokeWidth, mStrokeColor, mStrokeDashWidth, mStrokeDashGap)
    }

    /********************   radius   */
    fun setRadius(@FloatRange(from = 0) radius: Float) {
        mRadius = radius
        mNormalBackground.setCornerRadius(mRadius)
        mPressedBackground.setCornerRadius(mRadius)
        mUnableBackground.setCornerRadius(mRadius)
    }

    fun setRound(round: Boolean) {
        mRound = round
        val height: Int = getMeasuredHeight()
        if (mRound) {
            setRadius(height / 2f)
        }
    }

    fun setRadius(radii: FloatArray?) {
        mNormalBackground.setCornerRadii(radii)
        mPressedBackground.setCornerRadii(radii)
        mUnableBackground.setCornerRadii(radii)
    }

    /********************  background color   */
    fun setStateBackgroundColor(
        @ColorInt normal: Int,
        @ColorInt pressed: Int,
        @ColorInt unable: Int
    ) {
        mNormalBackgroundColor = normal
        mPressedBackgroundColor = pressed
        mUnableBackgroundColor = unable
        mNormalBackground.setColor(mNormalBackgroundColor)
        mPressedBackground.setColor(mPressedBackgroundColor)
        mUnableBackground.setColor(mUnableBackgroundColor)
    }

    fun setNormalBackgroundColor(@ColorInt normalBackgroundColor: Int) {
        mNormalBackgroundColor = normalBackgroundColor
        mNormalBackground.setColor(mNormalBackgroundColor)
    }

    fun setPressedBackgroundColor(@ColorInt pressedBackgroundColor: Int) {
        mPressedBackgroundColor = pressedBackgroundColor
        mPressedBackground.setColor(mPressedBackgroundColor)
    }

    fun setUnableBackgroundColor(@ColorInt unableBackgroundColor: Int) {
        mUnableBackgroundColor = unableBackgroundColor
        mUnableBackground.setColor(mUnableBackgroundColor)
    }

    /*******************alpha animation duration */
    fun setAnimationDuration(@IntRange(from = 0) duration: Int) {
        mDuration = duration
        mStateBackground.setEnterFadeDuration(mDuration)
    }

    /***************  text color    */
    private fun setTextColor() {
        val colors: IntArray? =
            intArrayOf(mPressedTextColor, mPressedTextColor, mNormalTextColor, mUnableTextColor)
        mTextColorStateList = ColorStateList(states, colors)
        setTextColor(mTextColorStateList)
    }

    fun setStateTextColor(@ColorInt normal: Int, @ColorInt pressed: Int, @ColorInt unable: Int) {
        mNormalTextColor = normal
        mPressedTextColor = pressed
        mUnableTextColor = unable
        setTextColor()
    }

    fun setNormalTextColor(@ColorInt normalTextColor: Int) {
        mNormalTextColor = normalTextColor
        setTextColor()
    }

    fun setPressedTextColor(@ColorInt pressedTextColor: Int) {
        mPressedTextColor = pressedTextColor
        setTextColor()
    }

    fun setUnableTextColor(@ColorInt unableTextColor: Int) {
        mUnableTextColor = unableTextColor
        setTextColor()
    }

    init {
        setFocusableInTouchMode(true)
        setup(attrs)
    }
}