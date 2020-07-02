package com.redapp.myowncustomviewactivity

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.ImageView
import androidx.core.content.contentValuesOf
import kotlin.math.atan

class RotatedImageView(context: Context, attrs:AttributeSet? =null): androidx.appcompat.widget.AppCompatImageView(context, attrs) {
    init {
        this.setImageResource(R.drawable.fire_ying_yang)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        this.rotation ++
        invalidate()

    }
}