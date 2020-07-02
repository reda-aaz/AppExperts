package com.redapp.notenews.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Build
import android.util.AttributeSet
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.redapp.notenews.R

private const val LINE_OFFSET=3
private const val LINE_SIZE = LINE_OFFSET  + 3

class LinedTextView(context: Context, attributeSet: AttributeSet? = null) : androidx.appcompat.widget.AppCompatTextView(context,attributeSet) {
    private val mRec = Rect()
    private val mPaint = Paint()
    init {
        mPaint.color = context.getColor(R.color.colorLine)
        mPaint.style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas) {
        val count = height/lineHeight
        var baseLine = getLineBounds(0,mRec).toFloat()
        for ( i in 0..count){
            for (j in LINE_OFFSET..LINE_SIZE){
            canvas.drawLine(mRec.left.toFloat(),baseLine+j,mRec.right.toFloat(),baseLine+j,mPaint)}
            baseLine += lineHeight
        }

        super.onDraw(canvas)

    }
}