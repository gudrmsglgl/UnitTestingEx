package com.fastival.unittestex.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText

class LinedEditText: AppCompatEditText {

    private lateinit var mRect: Rect
    private lateinit var mPaint: Paint

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        mRect = Rect()
        mPaint = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = 2f
            color = 0xFFFFD966.toInt()
        }

    }

    override fun onDraw(canvas: Canvas?) {

        val height = (parent as View).height

        val numberOfLines = height / lineHeight

        val r = mRect
        val paint = mPaint

        var baseLine = getLineBounds(0, r)

        for (i in 0 until numberOfLines) {
            canvas?.drawLine(r.left.toFloat(), (baseLine+1).toFloat(), r.right.toFloat(),(baseLine+1).toFloat(), paint )

            baseLine += lineHeight
        }


        super.onDraw(canvas)
    }
}