package com.anshmidt.smswidget.ui

import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.FloatRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.drawable.toBitmap
import androidx.glance.BitmapImageProvider
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.action.Action
import androidx.glance.action.clickable
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Box
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width

fun GlanceModifier.cornerRadiusCompat(
    cornerRadius: Int,
    @ColorInt color: Int,
    @FloatRange(from = 0.0, to = 1.0) backgroundAlpha: Float = 1f,
): GlanceModifier {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        this.background(Color(color).copy(alpha = backgroundAlpha))
            .cornerRadius(cornerRadius.dp)
    } else {
        val radii = FloatArray(8) { cornerRadius.toFloat() }
        val shape = ShapeDrawable(RoundRectShape(radii, null, null))
        shape.paint.color = ColorUtils.setAlphaComponent(color, (255 * backgroundAlpha).toInt())
        val bitmap = shape.toBitmap(width = 150, height = 75)
        this.background(BitmapImageProvider(bitmap))
    }
}

const val cornerRadius = 12
const val backgroundAlpha = 0.08f
@Composable
fun BackgroundCompat(
    modifier: GlanceModifier = GlanceModifier,
    onClick: Action,
    @ColorRes colorRes: Int,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val color = context.getColor(colorRes)
    Box(
        modifier = modifier.cornerRadiusCompat(cornerRadius, color, backgroundAlpha)
            .padding(12.dp)
            .clickable(onClick)
            .width(150.dp)
            .height(75.dp),
        content = content
    )
}