package com.anshmidt.smswidget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.glance.*
import androidx.glance.action.clickable
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.color.ColorProvider
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.anshmidt.smswidget.ui.SendButtonClickCallback
import com.anshmidt.smswidget.ui.theme.Red
import com.anshmidt.smswidget.ui.theme.TranslucentBlack
import com.anshmidt.smswidget.ui.theme.White

class SmsWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {
                val isLoading = currentState(key = isLoadingKey) ?: false

                WidgetContent(isLoading)
            }
        }
    }

    @Composable
    private fun WidgetContent(isLoading: Boolean) {
        Column(
            modifier = GlanceModifier
                .wrapContentHeight()
                .wrapContentWidth()
                .background(ColorProvider(day = TranslucentBlack, night = TranslucentBlack)),
            verticalAlignment = Alignment.Vertical.CenterVertically,
            horizontalAlignment = Alignment.Horizontal.Start
        ) {
            Title()
            WidgetRow(isLoading = isLoading)
        }
    }

    @Composable
    private fun Title() {
        Text(
            text = "SmsWidget",
            modifier = GlanceModifier.padding(horizontal = 8.dp, vertical = 8.dp),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = ColorProvider(day = Red, night = Red)
            )
        )
    }

    @Composable
    private fun WidgetRow(isLoading: Boolean) {
        Row(
            horizontalAlignment = Alignment.Start,
            modifier = GlanceModifier
                .padding(8.dp)
        ) {
            Text(
                text = "9011:",
                modifier = GlanceModifier.padding(start = 0.dp, end = 0.dp,top = 8.dp, bottom = 8.dp),
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    color = ColorProvider(day = White, night = White)
                )
            )

            Text(
                text = "A9000",
                modifier = GlanceModifier.padding(start = 8.dp, end = 16.dp,top = 8.dp, bottom = 8.dp),
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    color = ColorProvider(day = White, night = White)
                )
            )

            if (isLoading) {
                ProgressBar()
            } else {
                SendButton()
            }
        }
    }


    @Composable
    private fun SendButton() {
        Box(
            modifier = GlanceModifier
                .background(ImageProvider(R.drawable.circle_button_background))
                .size(SEND_BUTTON_SIZE)
                .clickable(onClick = actionRunCallback(SendButtonClickCallback::class.java)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = GlanceModifier.padding(10.dp),
                contentDescription = "Send SMS",
                provider = ImageProvider(R.drawable.ic_send_sms)
            )
        }
    }

    @Composable
    private fun ProgressBar() {
        Box(
            modifier = GlanceModifier
                .size(SEND_BUTTON_SIZE),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = ColorProvider(day = Red, night = Red)
            )
        }
    }

    companion object {
        val isLoadingKey = booleanPreferencesKey("isLoading")
        private val SEND_BUTTON_SIZE = 44.dp
    }


}