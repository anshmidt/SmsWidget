package com.anshmidt.smswidget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
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
import com.anshmidt.smswidget.ui.cornerRadiusCompat
import com.anshmidt.smswidget.ui.theme.Red
import com.anshmidt.smswidget.ui.theme.TranslucentBlack
import com.anshmidt.smswidget.ui.theme.White

object SmsWidget : GlanceAppWidget() {

    val isLoadingKey = booleanPreferencesKey("isLoading")

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
            if (isLoading) {
                LoadingRow()
            } else {
                WidgetCircularButtonRow()
            }
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
    private fun WidgetCircularButtonRow() {
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

            CircleButton()
        }
    }

    @Composable
    private fun WidgetCircularButtonWithoutBackgroundRow() {
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

            CircleButtonWithoutBackground()
        }
    }

    @Composable
    private fun WidgetPillButtonRow() {
        Row(
            horizontalAlignment = Alignment.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = GlanceModifier.padding(8.dp)
        ) {
            Text(
                text = "9011",
                modifier = GlanceModifier.padding(start = 0.dp, end = 8.dp,top = 8.dp, bottom = 8.dp),
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    color = ColorProvider(day = White, night = White)
                )
            )
            PillButton()
        }
    }

    @Composable
    private fun CircleButton() {
        Box(
            modifier = GlanceModifier
                .background(ImageProvider(R.drawable.circle_button_background))
                .wrapContentSize()
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
    private fun CircleButtonWithoutBackground() {
        Box(
            modifier = GlanceModifier
                .wrapContentSize(),
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
    private fun PillButton() {
        Row(
            modifier = GlanceModifier
                .background(ImageProvider(R.drawable.rounded_corners_background))
                .wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "A9000",
                modifier = GlanceModifier.padding(
                    start = 12.dp,
                    end = 0.dp,
                    top = 10.dp,
                    bottom = 10.dp
                ),
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    color = ColorProvider(day = White, night = White)
                )
            )
            Image(
                modifier = GlanceModifier.padding(
                    start = 8.dp,
                    end = 10.dp,
                    top = 10.dp,
                    bottom = 10.dp
                ),
                contentDescription = "Send SMS",
                provider = ImageProvider(R.drawable.ic_send_sms)
            )
        }
    }

    @Composable
    private fun LoadingRow() {
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

            CircularProgressIndicator(
                color = ColorProvider(day = Red, night = Red)
            )
        }
    }
}