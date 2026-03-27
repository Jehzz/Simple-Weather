package com.jessosborn.simpleweather.view.compose.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.Typeface
import android.provider.AlarmClock
import androidx.compose.runtime.Composable
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.LocalSize
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ClockWidget : GlanceAppWidget() {
    override val sizeMode: SizeMode = SizeMode.Exact

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId,
    ) {
        // Trigger the minute-by-minute update cycle when a widget instance is created/updated
        ClockWidgetUpdater.scheduleNextUpdate(context)

        provideContent {
            ClockWidgetContent()
        }
    }
}

/**
 * ActionCallback to handle opening Clock and Calendar apps.
 * Using a callback instead of actionStartActivity(Intent) avoids Glance's 
 * internal data URI injection which can break generic intent matching.
 */
class OpenAppActionCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val type = parameters[typeKey]
        val intent = when (type) {
            "clock" -> Intent(AlarmClock.ACTION_SHOW_ALARMS)
            "calendar" -> Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_APP_CALENDAR)
            }
            else -> null
        }
        
        intent?.apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(this)
        }
    }

    companion object {
        val typeKey = ActionParameters.Key<String>("open_type")
    }
}

@Composable
private fun ClockWidgetContent() {
    val size = LocalSize.current

    val baseWidth = 800f
    val widgetAspectRatio = (size.height.value / size.width.value).coerceIn(0.2f, 0.9f)
    val baseHeight = baseWidth * widgetAspectRatio

	val isNight = (LocalContext.current.resources.configuration.uiMode and
            android.content.res.Configuration.UI_MODE_NIGHT_MASK) ==
            android.content.res.Configuration.UI_MODE_NIGHT_YES

    val now = Date()
    val dayText = SimpleDateFormat("EEEE", Locale.getDefault()).format(now).uppercase()
    val timeText = SimpleDateFormat("HH:mm", Locale.getDefault()).format(now)
    val dateText = SimpleDateFormat("d, MMMM", Locale.getDefault()).format(now)

    val bitmap = createStencilBitmap(
        day = dayText,
        time = timeText,
        date = dateText,
        isNight = isNight,
        width = baseWidth.toInt(),
        height = baseHeight.toInt()
    )

    Box(
        modifier = GlanceModifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            provider = ImageProvider(bitmap),
            contentDescription = "Stencil Clock Widget",
            modifier = GlanceModifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )

        // Overlay transparent clickable areas
        Column(modifier = GlanceModifier.fillMaxSize()) {
            // Day area (Top ~65%) -> Calendar
            Spacer(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .defaultWeight()
                    .clickable(actionRunCallback<OpenAppActionCallback>(
                        actionParametersOf(OpenAppActionCallback.typeKey to "calendar")
                    ))
            )
            // Bottom area (Time and Date bar ~35%)
            Row(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .height(size.height * 0.35f)
            ) {
                // Time (Left half) -> Clock
                Spacer(
                    modifier = GlanceModifier
                        .defaultWeight()
                        .fillMaxHeight()
                        .clickable(actionRunCallback<OpenAppActionCallback>(
                            actionParametersOf(OpenAppActionCallback.typeKey to "clock")
                        ))
                )
                // Date (Right half) -> Calendar
                Spacer(
                    modifier = GlanceModifier
                        .defaultWeight()
                        .fillMaxHeight()
                        .clickable(actionRunCallback<OpenAppActionCallback>(
                            actionParametersOf(OpenAppActionCallback.typeKey to "calendar")
                        ))
                )
            }
        }
    }
}

private fun createStencilBitmap(
    day: String,
    time: String,
    date: String,
    isNight: Boolean,
    width: Int,
    height: Int
): Bitmap {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val mainColor = if (isNight) Color.parseColor("#1C1C1C") else Color.WHITE
    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    // Set horizontal margins to 0 to align with the widget cell edges (system padding still applies)
    val sideMargin = 0f
    val topMargin = height * 0.05f
    val topFrameHeight = height * 0.6f
    val verticalGap = height * 0.05f
    val bottomBarHeight = height * 0.25f
    
    val strokeThickness = (width * 0.02f).coerceIn(4f, 16f)
    val halfStroke = strokeThickness / 2f

    val frameRect = RectF(
        sideMargin + halfStroke,
        topMargin + halfStroke,
        width.toFloat() - sideMargin - halfStroke,
        topFrameHeight + topMargin - halfStroke
    )
    
    paint.style = Paint.Style.STROKE
    paint.strokeWidth = strokeThickness
    paint.color = mainColor
    canvas.drawRect(frameRect, paint)
    
    paint.style = Paint.Style.FILL
    paint.textSize = frameRect.height() * 0.5f
    paint.letterSpacing = 0.5f 
    
    val maxDayWidth = frameRect.width() - (width * 0.1f) // Keep some inner text padding
    while (paint.measureText(day) > maxDayWidth && paint.textSize > 20f) {
        paint.textSize -= 2f
        if (paint.textSize < 40f && paint.letterSpacing > 0.1f) {
            paint.letterSpacing -= 0.05f
        }
    }
    
    val dayY = frameRect.centerY() - ((paint.descent() + paint.ascent()) / 2)
    canvas.drawText(day, width / 2f, dayY, paint)

    paint.letterSpacing = 0.05f 
    val barTop = topFrameHeight + topMargin + verticalGap
    val barRect = RectF(sideMargin, barTop, width.toFloat() - sideMargin, barTop + bottomBarHeight)
    
    paint.color = mainColor
    paint.style = Paint.Style.FILL
    canvas.drawRect(barRect, paint)
    
    val stencilContent = "$time  |  $date"
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    paint.textSize = barRect.height() * 0.50f
    
    val maxStencilWidth = barRect.width() - (width * 0.1f)
    while (paint.measureText(stencilContent) > maxStencilWidth && paint.textSize > 15f) {
        paint.textSize -= 1f
    }
    
    val stencilY = barRect.centerY() - ((paint.descent() + paint.ascent()) / 2)
    canvas.drawText(stencilContent, width / 2f, stencilY, paint)

    return bitmap
}
