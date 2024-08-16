package com.example.chantingapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class BeadProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val totalBeads = 108
    private var filledBeads = 0

    init {
        paint.style = Paint.Style.FILL
    }

    fun setProgress(progress: Int) {
        filledBeads = progress
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = 160f // Slightly smaller to fit within the view

        // Draw white background circle
        paint.color = ContextCompat.getColor(context, android.R.color.white)
        canvas.drawCircle(centerX, centerY, radius, paint)

        // Draw border
        paint.style = Paint.Style.STROKE
        paint.color = ContextCompat.getColor(context, android.R.color.darker_gray)
        paint.strokeWidth = 2f
        canvas.drawCircle(centerX, centerY, radius, paint)

        paint.style = Paint.Style.FILL

        val angleStep = 360f / totalBeads
        val beadRadius = 10f  // Smaller bead size

        for (i in 0 until totalBeads) {
            val angle = Math.toRadians((-90 + i * angleStep).toDouble())  // Start from 12 o'clock
            val x = (centerX + radius * 0.85 * cos(angle)).toFloat()  // Move beads slightly inward
            val y = (centerY + radius * 0.85 * sin(angle)).toFloat()  // Move beads slightly inward

            paint.color = if (i < filledBeads) {
                ContextCompat.getColor(context, R.color.bead_filled)
            } else {
                ContextCompat.getColor(context, R.color.bead_empty)
            }

            canvas.drawCircle(x, y, beadRadius, paint)
        }
    }
}