package com.hh.hlibrary.view

import android.animation.Animator

/**
 * SwitchButton.
 * @author zhangchao
 * @date  2020年12月3日
 */
class SwitchButton : View, Checkable {
    /**
     * 动画状态：
     * 1.静止
     * 2.进入拖动
     * 3.处于拖动
     * 4.拖动-复位
     * 5.拖动-切换
     * 6.点击切换
     */
    private val ANIMATE_STATE_NONE: Int = 0
    private val ANIMATE_STATE_PENDING_DRAG: Int = 1
    private val ANIMATE_STATE_DRAGING: Int = 2
    private val ANIMATE_STATE_PENDING_RESET: Int = 3
    private val ANIMATE_STATE_PENDING_SETTLE: Int = 4
    private val ANIMATE_STATE_SWITCH: Int = 5

    constructor(context: Context?) : super(context) {
        init(context, null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }

    @Override
    fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(0, 0, 0, 0)
    }

    /**
     * 初始化参数
     */
    private fun init(context: Context?, attrs: AttributeSet?) {
        var typedArray: TypedArray? = null
        if (attrs != null) {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.SwitchButton)
        }
        shadowEffect = optBoolean(
            typedArray,
            R.styleable.SwitchButton_sb_shadow_effect,
            true
        )
        uncheckCircleColor = optColor(
            typedArray,
            R.styleable.SwitchButton_sb_uncheckcircle_color,
            -0x555556
        ) //0XffAAAAAA;
        uncheckCircleWidth = optPixelSize(
            typedArray,
            R.styleable.SwitchButton_sb_uncheckcircle_width,
            dp2pxInt(1.5f)
        ) //dp2pxInt(1.5f);
        uncheckCircleOffsetX = dp2px(10f)
        uncheckCircleRadius = optPixelSize(
            typedArray,
            R.styleable.SwitchButton_sb_uncheckcircle_radius,
            dp2px(4f)
        ) //dp2px(4);
        checkedLineOffsetX = dp2px(4f)
        checkedLineOffsetY = dp2px(4f)
        shadowRadius = optPixelSize(
            typedArray,
            R.styleable.SwitchButton_sb_shadow_radius,
            dp2pxInt(2.5f)
        ) //dp2pxInt(2.5f);
        shadowOffset = optPixelSize(
            typedArray,
            R.styleable.SwitchButton_sb_shadow_offset,
            dp2pxInt(1.5f)
        ) //dp2pxInt(1.5f);
        shadowColor = optColor(
            typedArray,
            R.styleable.SwitchButton_sb_shadow_color,
            0X33000000
        ) //0X33000000;
        uncheckColor = optColor(
            typedArray,
            R.styleable.SwitchButton_sb_uncheck_color,
            -0x222223
        ) //0XffDDDDDD;
        checkedColor = optColor(
            typedArray,
            R.styleable.SwitchButton_sb_checked_color,
            -0xae2c99
        ) //0Xff51d367;
        borderWidth = optPixelSize(
            typedArray,
            R.styleable.SwitchButton_sb_border_width,
            dp2pxInt(1f)
        ) //dp2pxInt(1);
        checkLineColor = optColor(
            typedArray,
            R.styleable.SwitchButton_sb_checkline_color,
            Color.WHITE
        ) //Color.WHITE;
        checkLineWidth = optPixelSize(
            typedArray,
            R.styleable.SwitchButton_sb_checkline_width,
            dp2pxInt(1f)
        ) //dp2pxInt(1.0f);
        checkLineLength = dp2px(6f)
        val buttonColor: Int = optColor(
            typedArray,
            R.styleable.SwitchButton_sb_button_color,
            Color.WHITE
        ) //Color.WHITE;
        uncheckButtonColor = optColor(
            typedArray,
            R.styleable.SwitchButton_sb_uncheckbutton_color,
            buttonColor
        )
        checkedButtonColor = optColor(
            typedArray,
            R.styleable.SwitchButton_sb_checkedbutton_color,
            buttonColor
        )
        val effectDuration: Int = optInt(
            typedArray,
            R.styleable.SwitchButton_sb_effect_duration,
            300
        ) //300;
        isChecked = optBoolean(
            typedArray,
            R.styleable.SwitchButton_sb_checked,
            false
        )
        showIndicator = optBoolean(
            typedArray,
            R.styleable.SwitchButton_sb_show_indicator,
            true
        )
        background = optColor(
            typedArray,
            R.styleable.SwitchButton_sb_background,
            Color.WHITE
        ) //Color.WHITE;
        enableEffect = optBoolean(
            typedArray,
            R.styleable.SwitchButton_sb_enable_effect,
            true
        )
        if (typedArray != null) {
            typedArray.recycle()
        }
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        buttonPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        buttonPaint.setColor(buttonColor)
        if (shadowEffect) {
            buttonPaint.setShadowLayer(
                shadowRadius,
                0, shadowOffset,
                shadowColor
            )
        }
        viewState = ViewState()
        beforeState = ViewState()
        afterState = ViewState()
        valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.setDuration(effectDuration)
        valueAnimator.setRepeatCount(0)
        valueAnimator.addUpdateListener(animatorUpdateListener)
        valueAnimator.addListener(animatorListener)
        super.setClickable(true)
        setPadding(0, 0, 0, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(LAYER_TYPE_SOFTWARE, null)
        }
    }

    @Override
    protected fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthMeasureSpec: Int = widthMeasureSpec
        var heightMeasureSpec: Int = heightMeasureSpec
        val widthMode: Int = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode: Int = MeasureSpec.getMode(heightMeasureSpec)
        if ((widthMode == MeasureSpec.UNSPECIFIED
                    || widthMode == MeasureSpec.AT_MOST)
        ) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(DEFAULT_WIDTH, MeasureSpec.EXACTLY)
        }
        if ((heightMode == MeasureSpec.UNSPECIFIED
                    || heightMode == MeasureSpec.AT_MOST)
        ) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(DEFAULT_HEIGHT, MeasureSpec.EXACTLY)
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    @Override
    protected fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val viewPadding: Float = Math.max(shadowRadius + shadowOffset, borderWidth)
        height = h - viewPadding - viewPadding
        width = w - viewPadding - viewPadding
        viewRadius = height * .5f
        buttonRadius = viewRadius - borderWidth
        left = viewPadding
        top = viewPadding
        right = w - viewPadding
        bottom = h - viewPadding
        centerX = (left + right) * .5f
        centerY = (top + bottom) * .5f
        buttonMinX = left + viewRadius
        buttonMaxX = right - viewRadius
        if (isChecked()) {
            setCheckedViewState(viewState)
        } else {
            setUncheckViewState(viewState)
        }
        isUiInited = true
        postInvalidate()
    }

    /**
     * @param viewState
     */
    private fun setUncheckViewState(viewState: ViewState?) {
        viewState.radius = 0f
        viewState.checkStateColor = uncheckColor
        viewState.checkedLineColor = Color.TRANSPARENT
        viewState.buttonX = buttonMinX
        buttonPaint.setColor(uncheckButtonColor)
    }

    /**
     * @param viewState
     */
    private fun setCheckedViewState(viewState: ViewState?) {
        viewState.radius = viewRadius
        viewState.checkStateColor = checkedColor
        viewState.checkedLineColor = checkLineColor
        viewState.buttonX = buttonMaxX
        buttonPaint.setColor(checkedButtonColor)
    }

    @Override
    protected fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.setStrokeWidth(borderWidth)
        paint.setStyle(Paint.Style.FILL)
        //绘制白色背景
        paint.setColor(background)
        drawRoundRect(
            canvas,
            left, top, right, bottom,
            viewRadius, paint
        )
        //绘制关闭状态的边框
        paint.setStyle(Paint.Style.STROKE)
        paint.setColor(uncheckColor)
        drawRoundRect(
            canvas,
            left, top, right, bottom,
            viewRadius, paint
        )

        //绘制小圆圈
        if (showIndicator) {
            drawUncheckIndicator(canvas)
        }

        //绘制开启背景色
        val des: Float = viewState.radius * .5f //[0-backgroundRadius*0.5f]
        paint.setStyle(Paint.Style.STROKE)
        paint.setColor(viewState.checkStateColor)
        paint.setStrokeWidth(borderWidth + des * 2f)
        drawRoundRect(
            canvas,
            left + des, top + des, right - des, bottom - des,
            viewRadius, paint
        )

        //绘制按钮左边绿色长条遮挡
        paint.setStyle(Paint.Style.FILL)
        paint.setStrokeWidth(1)
        drawArc(
            canvas,
            left, top,
            left + 2 * viewRadius, top + 2 * viewRadius, 90f, 180f, paint
        )
        canvas.drawRect(
            left + viewRadius, top,
            viewState.buttonX, top + 2 * viewRadius,
            paint
        )

        //绘制小线条
        if (showIndicator) {
            drawCheckedIndicator(canvas)
        }

        //绘制按钮
        drawButton(canvas, viewState.buttonX, centerY)
    }

    /**
     * 绘制选中状态指示器
     * @param canvas
     */
    protected fun drawCheckedIndicator(canvas: Canvas?) {
        drawCheckedIndicator(
            canvas,
            viewState.checkedLineColor,
            checkLineWidth.toFloat(),
            left + viewRadius - checkedLineOffsetX, centerY - checkLineLength,
            left + viewRadius - checkedLineOffsetY, centerY + checkLineLength,
            paint
        )
    }

    /**
     * 绘制选中状态指示器
     * @param canvas
     * @param color
     * @param lineWidth
     * @param sx
     * @param sy
     * @param ex
     * @param ey
     * @param paint
     */
    protected fun drawCheckedIndicator(
        canvas: Canvas?,
        color: Int,
        lineWidth: Float,
        sx: Float, sy: Float, ex: Float, ey: Float,
        paint: Paint?
    ) {
        paint.setStyle(Paint.Style.STROKE)
        paint.setColor(color)
        paint.setStrokeWidth(lineWidth)
        canvas.drawLine(
            sx, sy, ex, ey,
            paint
        )
    }

    /**
     * 绘制关闭状态指示器
     * @param canvas
     */
    private fun drawUncheckIndicator(canvas: Canvas?) {
        drawUncheckIndicator(
            canvas,
            uncheckCircleColor,
            uncheckCircleWidth.toFloat(),
            right - uncheckCircleOffsetX, centerY,
            uncheckCircleRadius,
            paint
        )
    }

    /**
     * 绘制关闭状态指示器
     * @param canvas
     * @param color
     * @param lineWidth
     * @param centerX
     * @param centerY
     * @param radius
     * @param paint
     */
    protected fun drawUncheckIndicator(
        canvas: Canvas?,
        color: Int,
        lineWidth: Float,
        centerX: Float, centerY: Float,
        radius: Float,
        paint: Paint?
    ) {
        paint.setStyle(Paint.Style.STROKE)
        paint.setColor(color)
        paint.setStrokeWidth(lineWidth)
        canvas.drawCircle(centerX, centerY, radius, paint)
    }

    /**
     * @param canvas
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @param startAngle
     * @param sweepAngle
     * @param paint
     */
    private fun drawArc(
        canvas: Canvas?,
        left: Float, top: Float,
        right: Float, bottom: Float,
        startAngle: Float, sweepAngle: Float,
        paint: Paint?
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawArc(
                left, top, right, bottom,
                startAngle, sweepAngle, true, paint
            )
        } else {
            rect.set(left, top, right, bottom)
            canvas.drawArc(
                rect,
                startAngle, sweepAngle, true, paint
            )
        }
    }

    /**
     * @param canvas
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @param backgroundRadius
     * @param paint
     */
    private fun drawRoundRect(
        canvas: Canvas?,
        left: Float, top: Float,
        right: Float, bottom: Float,
        backgroundRadius: Float,
        paint: Paint?
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(
                left, top, right, bottom,
                backgroundRadius, backgroundRadius, paint
            )
        } else {
            rect.set(left, top, right, bottom)
            canvas.drawRoundRect(
                rect,
                backgroundRadius, backgroundRadius, paint
            )
        }
    }

    /**
     * @param canvas
     * @param x px
     * @param y px
     */
    private fun drawButton(canvas: Canvas?, x: Float, y: Float) {
        canvas.drawCircle(x, y, buttonRadius, buttonPaint)
        paint.setStyle(Paint.Style.STROKE)
        paint.setStrokeWidth(1)
        paint.setColor(-0x222223)
        canvas.drawCircle(x, y, buttonRadius, paint)
    }

    @Override
    fun setChecked(checked: Boolean) {
        if (checked == isChecked()) {
            postInvalidate()
            return
        }
        toggle(enableEffect, false)
    }

    @Override
    fun isChecked(): Boolean {
        return isChecked
    }

    @Override
    fun toggle() {
        toggle(true)
    }

    /**
     * 切换状态
     * @param animate
     */
    fun toggle(animate: Boolean) {
        toggle(animate, true)
    }

    private fun toggle(animate: Boolean, broadcast: Boolean) {
        var animate: Boolean = animate
        animate = false
        if (!isEnabled()) {
            return
        }
        if (isEventBroadcast) {
            throw RuntimeException("should NOT switch the state in method: [onCheckedChanged]!")
        }
        if (!isUiInited) {
            isChecked = !isChecked
            if (broadcast) {
                broadcastEvent()
            }
            return
        }
        if (valueAnimator.isRunning()) {
            valueAnimator.cancel()
        }
        if (!enableEffect || !animate) {
            isChecked = !isChecked
            if (isChecked()) {
                setCheckedViewState(viewState)
            } else {
                setUncheckViewState(viewState)
            }
            postInvalidate()
            if (broadcast) {
                broadcastEvent()
            }
            return
        }
        animateState = ANIMATE_STATE_SWITCH
        beforeState.copy(viewState)
        if (isChecked()) {
            //切换到unchecked
            setUncheckViewState(afterState)
        } else {
            setCheckedViewState(afterState)
        }
        valueAnimator.start()
    }

    /**
     *
     */
    private fun broadcastEvent() {
        if (onCheckedChangeListener != null) {
            isEventBroadcast = true
            onCheckedChangeListener.onCheckedChanged(this, isChecked())
        }
        isEventBroadcast = false
    }
    /*@Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!isEnabled()){return false;}
        int actionMasked = event.getActionMasked();

        switch (actionMasked){
            case MotionEvent.ACTION_DOWN:{
                isTouchingDown = true;
                touchDownTime = System.currentTimeMillis();
                //取消准备进入拖动状态
                removeCallbacks(postPendingDrag);
                //预设100ms进入拖动状态
                postDelayed(postPendingDrag, 100);
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                float eventX = event.getX();
                if(isPendingDragState()){
                    //在准备进入拖动状态过程中，可以拖动按钮位置
                    float fraction = eventX / getWidth();
                    fraction = Math.max(0f, Math.min(1f, fraction));

                    viewState.buttonX = buttonMinX
                            + (buttonMaxX - buttonMinX)
                            * fraction;

                }else if(isDragState()){
                    //拖动按钮位置，同时改变对应的背景颜色
                    float fraction = eventX / getWidth();
                    fraction = Math.max(0f, Math.min(1f, fraction));

                    viewState.buttonX = buttonMinX
                            + (buttonMaxX - buttonMinX)
                            * fraction;

                    viewState.checkStateColor = (int) argbEvaluator.evaluate(
                            fraction,
                            uncheckColor,
                            checkedColor
                    );
                    postInvalidate();

                }
                break;
            }
            case MotionEvent.ACTION_UP:{
                isTouchingDown = false;
                //取消准备进入拖动状态
                removeCallbacks(postPendingDrag);

                if(System.currentTimeMillis() - touchDownTime <= 300){
                    //点击时间小于300ms，认为是点击操作
                    toggle();
                }else if(isDragState()){
                    //在拖动状态，计算按钮位置，设置是否切换状态
                    float eventX = event.getX();
                    float fraction = eventX / getWidth();
                    fraction = Math.max(0f, Math.min(1f, fraction));
                    boolean newCheck = fraction > .5f;
                    if(newCheck == isChecked()){
                        pendingCancelDragState();
                    }else{
                        isChecked = newCheck;
                        pendingSettleState();
                    }
                }else if(isPendingDragState()){
                    //在准备进入拖动状态过程中，取消之，复位
                    pendingCancelDragState();
                }
                break;
            }
            case MotionEvent.ACTION_CANCEL:{
                isTouchingDown = false;

                removeCallbacks(postPendingDrag);

                if(isPendingDragState()
                        || isDragState()){
                    //复位
                    pendingCancelDragState();
                }
                break;
            }
        }
        return true;
    }*/
    /**
     * 是否在动画状态
     * @return
     */
    private fun isInAnimating(): Boolean {
        return animateState != ANIMATE_STATE_NONE
    }

    /**
     * 是否在进入拖动或离开拖动状态
     * @return
     */
    private fun isPendingDragState(): Boolean {
        return (animateState == ANIMATE_STATE_PENDING_DRAG
                || animateState == ANIMATE_STATE_PENDING_RESET)
    }

    /**
     * 是否在手指拖动状态
     * @return
     */
    private fun isDragState(): Boolean {
        return animateState == ANIMATE_STATE_DRAGING
    }

    /**
     * 设置是否启用阴影效果
     * @param shadowEffect true.启用
     */
    fun setShadowEffect(shadowEffect: Boolean) {
        if (this.shadowEffect == shadowEffect) {
            return
        }
        this.shadowEffect = shadowEffect
        if (this.shadowEffect) {
            buttonPaint.setShadowLayer(
                shadowRadius,
                0, shadowOffset,
                shadowColor
            )
        } else {
            buttonPaint.setShadowLayer(
                0,
                0, 0,
                0
            )
        }
    }

    fun setEnableEffect(enable: Boolean) {
        enableEffect = enable
    }

    /**
     * 开始进入拖动状态
     */
    private fun pendingDragState() {
        if (isInAnimating()) {
            return
        }
        if (!isTouchingDown) {
            return
        }
        if (valueAnimator.isRunning()) {
            valueAnimator.cancel()
        }
        animateState = ANIMATE_STATE_PENDING_DRAG
        beforeState.copy(viewState)
        afterState.copy(viewState)
        if (isChecked()) {
            afterState.checkStateColor = checkedColor
            afterState.buttonX = buttonMaxX
            afterState.checkedLineColor = checkedColor
        } else {
            afterState.checkStateColor = uncheckColor
            afterState.buttonX = buttonMinX
            afterState.radius = viewRadius
        }
        valueAnimator.start()
    }

    /**
     * 取消拖动状态
     */
    private fun pendingCancelDragState() {
        if (isDragState() || isPendingDragState()) {
            if (valueAnimator.isRunning()) {
                valueAnimator.cancel()
            }
            animateState = ANIMATE_STATE_PENDING_RESET
            beforeState.copy(viewState)
            if (isChecked()) {
                setCheckedViewState(afterState)
            } else {
                setUncheckViewState(afterState)
            }
            valueAnimator.start()
        }
    }

    /**
     * 动画-设置新的状态
     */
    private fun pendingSettleState() {
        if (valueAnimator.isRunning()) {
            valueAnimator.cancel()
        }
        animateState = ANIMATE_STATE_PENDING_SETTLE
        beforeState.copy(viewState)
        if (isChecked()) {
            setCheckedViewState(afterState)
        } else {
            setUncheckViewState(afterState)
        }
        valueAnimator.start()
    }

    /*@Override
    public final void setOnClickListener(OnClickListener l) {}*/
    @Override
    fun setOnLongClickListener(l: OnLongClickListener?) {
    }

    fun setOnCheckedChangeListener(l: OnCheckedChangeListener?) {
        onCheckedChangeListener = l
    }

    open interface OnCheckedChangeListener {
        open fun onCheckedChanged(view: SwitchButton?, isChecked: Boolean)
    }
    /** */
    /**
     * 阴影半径
     */
    private var shadowRadius: Int = 0

    /**
     * 阴影Y偏移px
     */
    private var shadowOffset: Int = 0

    /**
     * 阴影颜色
     */
    private var shadowColor: Int = 0

    /**
     * 背景半径
     */
    private var viewRadius: Float = 0f

    /**
     * 按钮半径
     */
    private var buttonRadius: Float = 0f

    /**
     * 背景高
     */
    private var height: Float = 0f

    /**
     * 背景宽
     */
    private var width: Float = 0f

    /**
     * 背景位置
     */
    private var left: Float = 0f
    private var top: Float = 0f
    private var right: Float = 0f
    private var bottom: Float = 0f
    private var centerX: Float = 0f
    private var centerY: Float = 0f

    /**
     * 背景底色
     */
    private var background: Int = 0

    /**
     * 背景关闭颜色
     */
    private var uncheckColor: Int = 0

    /**
     * 背景打开颜色
     */
    private var checkedColor: Int = 0

    /**
     * 边框宽度px
     */
    private var borderWidth: Int = 0

    /**
     * 打开指示线颜色
     */
    private var checkLineColor: Int = 0

    /**
     * 打开指示线宽
     */
    private var checkLineWidth: Int = 0

    /**
     * 打开指示线长
     */
    private var checkLineLength: Float = 0f

    /**
     * 关闭圆圈颜色
     */
    private var uncheckCircleColor: Int = 0

    /**
     * 关闭圆圈线宽
     */
    private var uncheckCircleWidth: Int = 0

    /**
     * 关闭圆圈位移X
     */
    private var uncheckCircleOffsetX: Float = 0f

    /**
     * 关闭圆圈半径
     */
    private var uncheckCircleRadius: Float = 0f

    /**
     * 打开指示线位移X
     */
    private var checkedLineOffsetX: Float = 0f

    /**
     * 打开指示线位移Y
     */
    private var checkedLineOffsetY: Float = 0f

    /**
     * Color for button when it's uncheck
     */
    private var uncheckButtonColor: Int = 0

    /**
     * Color for button when it's check
     */
    private var checkedButtonColor: Int = 0

    /**
     * 按钮最左边
     */
    private var buttonMinX: Float = 0f

    /**
     * 按钮最右边
     */
    private var buttonMaxX: Float = 0f

    /**
     * 按钮画笔
     */
    private var buttonPaint: Paint? = null

    /**
     * 背景画笔
     */
    private var paint: Paint? = null

    /**
     * 当前状态
     */
    private var viewState: ViewState? = null
    private var beforeState: ViewState? = null
    private var afterState: ViewState? = null
    private val rect: RectF? = RectF()

    /**
     * 动画状态
     */
    private var animateState: Int = ANIMATE_STATE_NONE

    /**
     *
     */
    private var valueAnimator: ValueAnimator? = null
    private val argbEvaluator: android.animation.ArgbEvaluator? = ArgbEvaluator()

    /**
     * 是否选中
     */
    private var isChecked: Boolean = false

    /**
     * 是否启用动画
     */
    private var enableEffect: Boolean = false

    /**
     * 是否启用阴影效果
     */
    private var shadowEffect: Boolean = false

    /**
     * 是否显示指示器
     */
    private var showIndicator: Boolean = false

    /**
     * 收拾是否按下
     */
    private val isTouchingDown: Boolean = false

    /**
     *
     */
    private var isUiInited: Boolean = false

    /**
     *
     */
    private var isEventBroadcast: Boolean = false
    private var onCheckedChangeListener: OnCheckedChangeListener? = null

    /**
     * 手势按下的时刻
     */
    private val touchDownTime: Long = 0
    private val postPendingDrag: Runnable? = object : Runnable() {
        @Override
        fun run() {
            if (!isInAnimating()) {
                pendingDragState()
            }
        }
    }
    private val animatorUpdateListener: AnimatorUpdateListener? =
        object : AnimatorUpdateListener() {
            @Override
            fun onAnimationUpdate(animation: ValueAnimator?) {
                val value: Float = animation.getAnimatedValue() as Float?
                when (animateState) {
                    ANIMATE_STATE_PENDING_SETTLE -> {
                        run({})
                        run({})
                        run({
                            viewState.checkedLineColor = argbEvaluator.evaluate(
                                value,
                                beforeState.checkedLineColor,
                                afterState.checkedLineColor
                            ) as Int
                            viewState.radius = (beforeState.radius
                                    + (afterState.radius - beforeState.radius) * value)
                            if (animateState != ANIMATE_STATE_PENDING_DRAG) {
                                viewState.buttonX = (beforeState.buttonX
                                        + (afterState.buttonX - beforeState.buttonX) * value)
                            }
                            viewState.checkStateColor = argbEvaluator.evaluate(
                                value,
                                beforeState.checkStateColor,
                                afterState.checkStateColor
                            ) as Int
                        })
                    }
                    ANIMATE_STATE_PENDING_RESET -> {
                        run({})
                        run({
                            viewState.checkedLineColor = argbEvaluator.evaluate(
                                value,
                                beforeState.checkedLineColor,
                                afterState.checkedLineColor
                            ) as Int
                            viewState.radius = (beforeState.radius
                                    + (afterState.radius - beforeState.radius) * value)
                            if (animateState != ANIMATE_STATE_PENDING_DRAG) {
                                viewState.buttonX = (beforeState.buttonX
                                        + (afterState.buttonX - beforeState.buttonX) * value)
                            }
                            viewState.checkStateColor = argbEvaluator.evaluate(
                                value,
                                beforeState.checkStateColor,
                                afterState.checkStateColor
                            ) as Int
                        })
                    }
                    ANIMATE_STATE_PENDING_DRAG -> {
                        viewState.checkedLineColor = argbEvaluator.evaluate(
                            value,
                            beforeState.checkedLineColor,
                            afterState.checkedLineColor
                        ) as Int
                        viewState.radius = (beforeState.radius
                                + (afterState.radius - beforeState.radius) * value)
                        if (animateState != ANIMATE_STATE_PENDING_DRAG) {
                            viewState.buttonX = (beforeState.buttonX
                                    + (afterState.buttonX - beforeState.buttonX) * value)
                        }
                        viewState.checkStateColor = argbEvaluator.evaluate(
                            value,
                            beforeState.checkStateColor,
                            afterState.checkStateColor
                        ) as Int
                    }
                    ANIMATE_STATE_SWITCH -> {
                        viewState.buttonX = (beforeState.buttonX
                                + (afterState.buttonX - beforeState.buttonX) * value)
                        val fraction: Float =
                            (viewState.buttonX - buttonMinX) / (buttonMaxX - buttonMinX)
                        viewState.checkStateColor = argbEvaluator.evaluate(
                            fraction,
                            uncheckColor,
                            checkedColor
                        ) as Int
                        viewState.radius = fraction * viewRadius
                        viewState.checkedLineColor = argbEvaluator.evaluate(
                            fraction,
                            Color.TRANSPARENT,
                            checkLineColor
                        ) as Int
                    }
                    ANIMATE_STATE_DRAGING -> {
                        run({})
                        run({})
                    }
                    ANIMATE_STATE_NONE -> {
                    }
                    else -> {
                        run({})
                        run({})
                    }
                }
                postInvalidate()
            }
        }
    private val animatorListener: Animator.AnimatorListener? = object : AnimatorListener() {
        @Override
        fun onAnimationStart(animation: Animator?) {
        }

        @Override
        fun onAnimationEnd(animation: Animator?) {
            when (animateState) {
                ANIMATE_STATE_DRAGING -> {
                }
                ANIMATE_STATE_PENDING_DRAG -> {
                    animateState = ANIMATE_STATE_DRAGING
                    viewState.checkedLineColor = Color.TRANSPARENT
                    viewState.radius = viewRadius
                    postInvalidate()
                }
                ANIMATE_STATE_PENDING_RESET -> {
                    animateState = ANIMATE_STATE_NONE
                    postInvalidate()
                }
                ANIMATE_STATE_PENDING_SETTLE -> {
                    animateState = ANIMATE_STATE_NONE
                    postInvalidate()
                    broadcastEvent()
                }
                ANIMATE_STATE_SWITCH -> {
                    isChecked = !isChecked
                    animateState = ANIMATE_STATE_NONE
                    postInvalidate()
                    broadcastEvent()
                }
                ANIMATE_STATE_NONE -> {
                }
                else -> {
                }
            }
        }

        @Override
        fun onAnimationCancel(animation: Animator?) {
        }

        @Override
        fun onAnimationRepeat(animation: Animator?) {
        }
    }
    /** */
    /**
     * 保存动画状态
     */
    private class ViewState internal constructor() {
        /**
         * 按钮x位置[buttonMinX-buttonMaxX]
         */
        var buttonX: Float = 0f

        /**
         * 状态背景颜色
         */
        var checkStateColor: Int = 0

        /**
         * 选中线的颜色
         */
        var checkedLineColor: Int = 0

        /**
         * 状态背景的半径
         */
        var radius: Float = 0f
        private fun copy(source: ViewState?) {
            buttonX = source.buttonX
            checkStateColor = source.checkStateColor
            checkedLineColor = source.checkedLineColor
            radius = source.radius
        }
    }

    companion object {
        private val DEFAULT_WIDTH: Int = dp2pxInt(58f)
        private val DEFAULT_HEIGHT: Int = dp2pxInt(36f)

        /** */
        private fun dp2px(dp: Float): Float {
            val r: Resources? = Resources.getSystem()
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics())
        }

        private fun dp2pxInt(dp: Float): Int {
            return dp2px(dp) as Int
        }

        private fun optInt(
            typedArray: TypedArray?,
            index: Int,
            def: Int
        ): Int {
            if (typedArray == null) {
                return def
            }
            return typedArray.getInt(index, def)
        }

        private fun optPixelSize(
            typedArray: TypedArray?,
            index: Int,
            def: Float
        ): Float {
            if (typedArray == null) {
                return def
            }
            return typedArray.getDimension(index, def)
        }

        private fun optPixelSize(
            typedArray: TypedArray?,
            index: Int,
            def: Int
        ): Int {
            if (typedArray == null) {
                return def
            }
            return typedArray.getDimensionPixelOffset(index, def)
        }

        private fun optColor(
            typedArray: TypedArray?,
            index: Int,
            def: Int
        ): Int {
            if (typedArray == null) {
                return def
            }
            return typedArray.getColor(index, def)
        }

        private fun optBoolean(
            typedArray: TypedArray?,
            index: Int,
            def: Boolean
        ): Boolean {
            if (typedArray == null) {
                return def
            }
            return typedArray.getBoolean(index, def)
        }
    }
}