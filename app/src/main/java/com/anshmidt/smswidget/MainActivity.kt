package com.anshmidt.smswidget

import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.core.preferencesOf
import androidx.glance.appwidget.ExperimentalGlanceRemoteViewsApi
import com.anshmidt.smswidget.ui.theme.AppTheme
import android.Manifest
import androidx.activity.result.contract.ActivityResultContracts
import java.time.LocalDateTime
import java.time.ZoneOffset

class MainActivity : ComponentActivity() {

    private val SEND_SMS_PERMISSION_REQUEST_CODE = 1
    private val sendSmsPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Permission granted, now perform the action (e.g., send SMS)
                sendSMS()
            } else {
                // Permission denied, handle accordingly (e.g., show a message)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }

        // Check and request SEND_SMS permission when needed
        checkAndRequestSendSmsPermission()
    }

    private fun checkAndRequestSendSmsPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.SEND_SMS
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permission is already granted, send SMS
                sendSMS()
            }
            else -> {
                // Request the SEND_SMS permission
                sendSmsPermissionLauncher.launch(Manifest.permission.SEND_SMS)
            }
        }
    }

    private fun sendSMS() {
        val phoneNumber = "1234567890" // Replace with the recipient's phone number
        val message = "Hello, this is a test message: ${
            LocalDateTime.now()}"

        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            Toast.makeText(this, "SMS sent successfully", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to send SMS", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppTheme {
        Greeting("Android")
    }
}

