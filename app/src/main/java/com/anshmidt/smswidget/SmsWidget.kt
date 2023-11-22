package com.anshmidt.smswidget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.anshmidt.smswidget.ui.UnlockButtonClickCallback
import com.anshmidt.smswidget.ui.theme.Red
import com.anshmidt.smswidget.ui.theme.TranslucentBlack
import com.anshmidt.smswidget.ui.theme.White

class SmsWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {
                val rowState = currentState(key = rowStateKey)?.toRowState() ?: RowState.DEFAULT_ROW_STATE
                WidgetContent(rowState)
            }
        }
    }

    @Composable
    private fun WidgetContent(rowState: RowState) {
        Column(
            modifier = GlanceModifier
                .wrapContentSize()
                .background(ImageProvider(R.drawable.rounded_corners_background)),
            verticalAlignment = Alignment.Vertical.CenterVertically,
            horizontalAlignment = Alignment.Horizontal.Start
        ) {
            Title()
            if (rowState == RowState.MESSAGE_SENT) {
                MessageSentRow()
            } else {
                WidgetRow(rowState = rowState)
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
    private fun WidgetRow(rowState: RowState) {
        Row(
            horizontalAlignment = Alignment.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = GlanceModifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "9011:",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = ColorProvider(day = White, night = White)
                )
            )

            Text(
                text = "A9000",
                modifier = GlanceModifier.padding(start = 8.dp, end = 16.dp),
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    color = ColorProvider(day = White, night = White)
                )
            )

            Spacer(modifier = GlanceModifier.defaultWeight())

            when (rowState) {
                RowState.LOADING -> ProgressBar()
                RowState.NORMAL -> SendButton()
                RowState.LOCKED -> UnlockButton()
                else -> UnlockButton() //shouldn't happen
            }
        }
    }

    @Composable
    private fun MessageSentRow() {
        Row(
            horizontalAlignment = Alignment.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = GlanceModifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Message sent!",
                modifier = GlanceModifier.padding(start = 0.dp, end = 16.dp),
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = ColorProvider(day = White, night = White)
                )
            )
            Spacer(modifier = GlanceModifier.defaultWeight())
            MessageSentIcon()
        }
    }

    @Composable
    private fun SendButton() {
        Box(
            modifier = GlanceModifier
                .background(ImageProvider(R.drawable.circular_background))
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
    private fun UnlockButton() {
        Box(
            modifier = GlanceModifier
                .background(ImageProvider(R.drawable.circular_background))
                .size(SEND_BUTTON_SIZE)
                .clickable(onClick = actionRunCallback(UnlockButtonClickCallback::class.java)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = GlanceModifier.padding(10.dp),
                contentDescription = "Unlock",
                provider = ImageProvider(R.drawable.ic_lock)
            )
        }
    }

    @Composable
    private fun MessageSentIcon() {
        Box(
            modifier = GlanceModifier
                .background(ImageProvider(R.drawable.circular_background))
                .size(SEND_BUTTON_SIZE),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = GlanceModifier.padding(10.dp),
                contentDescription = "Message sent!",
                provider = ImageProvider(R.drawable.ic_check)
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
        val rowStateKey = intPreferencesKey("rowState")
        private val SEND_BUTTON_SIZE = 44.dp
    }


}