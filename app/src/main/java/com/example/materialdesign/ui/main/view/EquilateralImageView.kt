package com.example.materialdesign.ui.main.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import java.util.jar.Attributes

class EquilateralImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): AppCompatImageView(context, attrs, defStyleAttr) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec) // определили, что ширина и высота одинаковые
    }
}