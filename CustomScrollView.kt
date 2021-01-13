package com.example.customviewsamples.customscroll

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import com.example.customviewsamples.chart.ChartView
import kotlin.math.roundToInt

class CustomScrollView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): ScrollView(context, attrs, defStyleAttr) {

    private val TAG = "CustomScrollView"

    private fun isWithinBounds(view: View, ev: MotionEvent): Boolean {
        val xPoint = ev.rawX.roundToInt()
        val yPoint = ev.rawY.roundToInt()
        val l = IntArray(2)
        view.getLocationOnScreen(l)
        val x = l[0]
        val y = l[1]
        val w = view.width
        val h = view.height
        return !(xPoint < x || xPoint > x + w || yPoint < y || yPoint > y + h)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        for (i in 0 until this.childCount) {

            if (this.getChildAt(i) !is ViewGroup) {
                throw Exception("This custom scroll view should contains 1 ViewGroup inside it")
            }

            val child: ViewGroup = this.getChildAt(i) as ViewGroup

            for (j in 0 until this.childCount) {
                val innerChild: View = child.getChildAt(j)

                Log.d(TAG, "this.childCount ${this.childCount}")
                if(innerChild is ChartView ) {
                    Log.d(TAG, "found child of type ChartView")
                }

                ev?.run {
                    if (innerChild is ChartView && isWithinBounds(innerChild, this)) {
                        return false
                    }
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }
}