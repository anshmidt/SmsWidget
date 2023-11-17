package com.anshmidt.smswidget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.*
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.color.ColorProvider
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.anshmidt.smswidget.ui.theme.Red
import com.anshmidt.smswidget.ui.theme.TranslucentBlack
import com.anshmidt.smswidget.ui.theme.White

class SmsWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {
                WidgetContent()
            }
        }
    }

    @Composable
    private fun WidgetContent() {
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(ColorProvider(day = TranslucentBlack, night = TranslucentBlack)),
            verticalAlignment = Alignment.Vertical.CenterVertically,
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally
        ) {
            Text(
                text = "SmsWidget2",
                modifier = GlanceModifier.padding(8.dp),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = ColorProvider(day = Red, night = Red)
                )
            )
            Row(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    text = "Send SMS",
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = ColorProvider(day = Red, night = Red),
                        contentColor = ColorProvider(day = White, night = White)
                    ),
                    onClick = { }
                )
            }
        }
    }
}