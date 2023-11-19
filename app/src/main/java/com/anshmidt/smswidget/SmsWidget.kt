package com.anshmidt.smswidget

import android.content.Context
import android.widget.ImageButton
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.*
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.color.ColorProvider
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.anshmidt.smswidget.ui.BackgroundCompat
import com.anshmidt.smswidget.ui.cornerRadiusCompat
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
                .wrapContentHeight()
                .wrapContentWidth()
                .background(ColorProvider(day = TranslucentBlack, night = TranslucentBlack)),
            verticalAlignment = Alignment.Vertical.CenterVertically,
            horizontalAlignment = Alignment.Horizontal.Start
        ) {
            Title()
            WidgetRow3()
            WidgetRow3()
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
    private fun WidgetRow() {
        Row(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = GlanceModifier.padding(8.dp)
        ) {
            Text(
                text = "9011",
                modifier = GlanceModifier.padding(start = 8.dp, end = 16.dp,top = 8.dp, bottom = 8.dp),
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    color = ColorProvider(day = White, night = White)
                )
            )
            SendButton()
        }
    }

    @Composable
    private fun WidgetRow2() {
        Row(
            horizontalAlignment = Alignment.Start,
            modifier = GlanceModifier
                .padding(8.dp)
        ) {
            Text(
                text = "9011:",
                modifier = GlanceModifier.padding(start = 8.dp, end = 0.dp,top = 8.dp, bottom = 8.dp),
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    color = ColorProvider(day = White, night = White)
                )
            )

            Text(
                text = "A90",
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
    private fun WidgetRow3() {
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
            SendButton2()
        }
    }

    @Composable
    private fun CircleButton() {
        Box(
            modifier = GlanceModifier
                .background(ImageProvider(R.drawable.circle_button_background))
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
    private fun IconButton3() {
        Row(
            modifier = GlanceModifier
                .background(ImageProvider(R.drawable.rounded_corners_background)),
//                .width(80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
//            Text(
//                text = "SMS sent",
//                modifier = GlanceModifier.padding(8.dp),
//                style = TextStyle(
//                    fontWeight = FontWeight.Normal,
//                    fontSize = 18.sp,
//                    color = ColorProvider(day = White, night = White)
//                )
//            )
            Image(
                modifier = GlanceModifier.padding(12.dp),
                contentDescription = "Send SMS",
                provider = ImageProvider(R.drawable.ic_send_sms)
            )
        }
    }

    @Composable
    private fun IconButton2() {
        Box(
            modifier = GlanceModifier
                .cornerRadiusCompat(cornerRadius = 20, color = Red.toArgb()),
            contentAlignment = Alignment.Center
        ) {
//            CircularProgressIndicator(
//                color = ColorProvider(day = White, night = White)
//            )
//            Image(
//                modifier = GlanceModifier.padding(16.dp),
//                contentDescription = "Send SMS",
//                provider = ImageProvider(R.drawable.ic_send_sms)
//            )
        }
    }

    @Composable
    private fun SendButton() {
        Row(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = GlanceModifier
                .cornerRadiusCompat(cornerRadius = 25, color = Red.toArgb())
        ) {
            Text(
                text = "A90",
                modifier = GlanceModifier.padding(8.dp),
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    color = ColorProvider(day = White, night = White)
                )
            )
            Image(
                modifier = GlanceModifier.padding(8.dp),
                contentDescription = "Send SMS",
                provider = ImageProvider(R.drawable.ic_send_sms)
            )
        }

//        Button(
//            text = "A90",
//            colors = ButtonDefaults.buttonColors(
//                backgroundColor = ColorProvider(day = Red, night = Red),
//                contentColor = ColorProvider(day = White, night = White)
//            ),
//            onClick = { }
//        )
    }

    @Composable
    private fun SendButton2() {
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
}