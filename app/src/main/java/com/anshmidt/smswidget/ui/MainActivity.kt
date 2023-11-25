package com.anshmidt.smswidget.ui

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
import androidx.core.content.ContextCompat
import com.anshmidt.smswidget.ui.theme.AppTheme
import android.Manifest
import android.Manifest.permission_group.SMS
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberUpdatedState
import com.anshmidt.smswidget.MainViewModel
import java.time.LocalDateTime

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    private val sendSmsPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                viewModel.setPermissionGranted(true)
            } else {
                viewModel.setPermissionDenied(true)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Content()
            }
        }
    }

    @Composable
    private fun Content() {
        val isPermissionGranted by viewModel.permissionGranted.observeAsState(initial = isSmsPermissionGranted())
        val isPermissionDenied by viewModel.permissionDenied.observeAsState(initial = false)

        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                Text("SmsWidget")

                if (isPermissionGranted) {
                    Text("SMS permission granted")
                } else {
                    Text("SMS permission not granted.")
                }

                if (isPermissionDenied) {
                    Text("Sms permission denied")
                } else {
                    Text("Sms permission not denied")
                }

                Button(onClick = {
                    requestSmsPermission()
                }) {
                    Text("Grant SMS permission")
                }

            }

        }
    }

    private fun isSmsPermissionGranted() =
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.SEND_SMS
        ) == PackageManager.PERMISSION_GRANTED

    private fun requestSmsPermission() {
        sendSmsPermissionLauncher.launch(Manifest.permission.SEND_SMS)
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

