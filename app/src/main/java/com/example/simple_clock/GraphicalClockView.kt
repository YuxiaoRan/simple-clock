package com.example.simple_clock

import android.content.Context
import android.graphics.Canvas
import android.view.View
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.util.AttributeSet

/**
 * A simple analog clock view
 * @author Yuxiao "Sean" Ran, ranyuxiao@bytedance.com
 * @since 07/17/20
 */
class GraphicalClockView: View {

    companion object {
        private val NUM_TWELVE = "12"
        private val NUM_THREE = "3"
        private val NUM_SIX = "6"
        private val NUM_NINE = "9"
        private val COLOR_COMPASS: Int = Color.rgb(0xFF, 0xFF, 0xFF)
        private val COLOR_MIN_HAND: Int = Color.rgb(0xC0, 0xC0, 0xC0)
        private val COLOR_SEC_HAND: Int = Color.rgb(0xFF, 0xC0, 0xCB)
        private val TOTAL_NUM = 60
        private val HALF_NUM = 30
        private val DIVISOR = 2
        private val STROKE_WIDTH = 20f
        private val TEXT_SIZE = 60f
        private val LENGTH_MIN_HAND = 0.6f
        private val LENGTH_SEC_HAND = 0.8f
        private val WIDTH_MIN_HAND = 1.2f
        private val WIDTH_SEC_HAND = 0.8f
        private val SEC_IN_MILLIS = 1000L
    }

    private val paint = Paint()
    private var minHandStartsAt: Int
    private var secHandStartsAt: Int
    private var radius: Float
    private var minHandLength: Float
    private var secHandLength: Float
    private var handler = getHandler()
    private lateinit var startClock: Runnable

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        context.theme.obtainStyledAttributes(attrs, R.styleable.GraphicalClockView,
            0, 0).apply {
            try {
                minHandStartsAt = getInt(R.styleable.GraphicalClockView_minHandStartsAt, 0)
                secHandStartsAt = getInt(R.styleable.GraphicalClockView_secHandStartsAt, 0)
                radius = 0f
                minHandLength = 0f
                secHandLength = 0f
                validateAttrs()
            } finally {
                recycle()
            }
        }
    }

    override fun getHandler(): Handler = Handler()

    fun start() {
        refresh()
    }

    fun stop() {
        handler.removeCallbacksAndMessages(null)
    }

    // validate attributes
    private fun validateAttrs() {
        if (minHandStartsAt < 0) {
            minHandStartsAt = 0
        }
        if (secHandStartsAt < 0) {
            secHandStartsAt = 0
        }
        minHandStartsAt %= TOTAL_NUM
        secHandStartsAt %= TOTAL_NUM
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        radius = Integer.min(w, h).toFloat() / DIVISOR - STROKE_WIDTH
        minHandLength = LENGTH_MIN_HAND * radius
        secHandLength = LENGTH_SEC_HAND * radius
        if (radius < 0f) {
            radius = 0f
            minHandLength = 0f
            secHandLength = 0f
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawCompass(canvas)
        drawNums(canvas)
        drawMinHand(canvas)
        drawSecHand(canvas)
    }

    private fun refresh() {
        startClock = kotlinx.coroutines.Runnable {
            handler.postDelayed(startClock, SEC_IN_MILLIS)
            // advance sec hand
            incrementSec()
            // advance min hand
            if (secHandStartsAt % TOTAL_NUM == 0)
                incrementMin()
            // refresh view
            invalidate()
        }
        handler.post(startClock)
    }

    private fun drawCompass(canvas: Canvas) {
        paint.color = COLOR_COMPASS
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = STROKE_WIDTH
        val center = radius + STROKE_WIDTH
        canvas.drawCircle(center, center, radius, paint)
        canvas.drawCircle(center, center, STROKE_WIDTH / DIVISOR, paint)
    }

    private fun drawNums(canvas: Canvas) {
        paint.color = COLOR_COMPASS
        paint.style = Paint.Style.FILL
        paint.textSize = TEXT_SIZE
        canvas.drawText(NUM_TWELVE, radius - STROKE_WIDTH,
            DIVISOR * DIVISOR * STROKE_WIDTH, paint)
        canvas.drawText(NUM_THREE, radius * DIVISOR - DIVISOR * STROKE_WIDTH,
            radius + DIVISOR * STROKE_WIDTH, paint)
        canvas.drawText(NUM_SIX, radius,
            radius * DIVISOR, paint)
        canvas.drawText(NUM_NINE, DIVISOR * STROKE_WIDTH,
            radius + DIVISOR * STROKE_WIDTH, paint)
    }

    private fun drawMinHand(canvas: Canvas) {
        paint.color = COLOR_MIN_HAND
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = STROKE_WIDTH * WIDTH_MIN_HAND
        val angle = Math.PI * minHandStartsAt / HALF_NUM - Math.PI / DIVISOR
        val center = radius + STROKE_WIDTH
        val minHandEndX = center + Math.cos(angle) * minHandLength
        val minHandEndY = center + Math.sin(angle) * minHandLength
        canvas.drawLine(center, center, minHandEndX.toFloat(), minHandEndY.toFloat(), paint)
    }

    private fun drawSecHand(canvas: Canvas) {
        paint.color = COLOR_SEC_HAND
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = STROKE_WIDTH * WIDTH_SEC_HAND
        val angle = Math.PI * secHandStartsAt / HALF_NUM - Math.PI / DIVISOR
        val center = radius + STROKE_WIDTH
        val secHandEndX = center + Math.cos(angle) * secHandLength
        val secHandEndY = center + Math.sin(angle) * secHandLength
        canvas.drawLine(center, center, secHandEndX.toFloat(), secHandEndY.toFloat(), paint)
    }

    fun incrementMin() {
        minHandStartsAt = ++minHandStartsAt % TOTAL_NUM
    }

    fun incrementSec() {
        secHandStartsAt = ++secHandStartsAt % TOTAL_NUM
    }

}