package com.example.appsample.framework.presentation.profile.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

class SquareFrameByWidthLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var preventSquare = false

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (preventSquare) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        } else {
            // Set a square layout.
            super.onMeasure(widthMeasureSpec, widthMeasureSpec)
        }
    }

    fun setPreventSquare(preventSquare: Boolean) {
        this.preventSquare = preventSquare
    }
}