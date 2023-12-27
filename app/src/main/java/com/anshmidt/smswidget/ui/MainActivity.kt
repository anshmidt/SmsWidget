package com.anshmidt.smswidget.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.lifecycleScope
import com.anshmidt.smswidget.MainViewModel
import com.anshmidt.smswidget.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    private val sendSmsPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            Log.d("MainActivity", "permission granted: $isGranted")
            if (isGranted) {
                viewModel.setPermissionGranted(true)
            } else {
                viewModel.setPermissionDenied(true)
            }
            lifecycleScope.launch(Dispatchers.Main) {
                updateWidget()
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

    override fun onResume() {
        super.onResume()
        // Checking permission in case user granted it from Settings app
        if (isSmsPermissionGranted()) {
            viewModel.setPermissionGranted(true)
            viewModel.setPermissionDenied(false)
        }
    }

    @Composable
    private fun Content() {
        val isPermissionGranted by viewModel.permissionGranted.observeAsState(initial = isSmsPermissionGranted())
        val isPermissionDenied by viewModel.permissionDenied.observeAsState(initial = false)

        if (isPermissionDenied) {
            SmsPermissionDeniedScreen()
            return
        }

        if (isPermissionGranted) {
            NormalScreen()
        } else {
            AskSmsPermissionScreen()
        }
    }

    private fun isSmsPermissionGranted() =
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.SEND_SMS
        ) == PackageManager.PERMISSION_GRANTED


    private suspend fun updateWidget() {
        SmsWidget().updateAll(this)
    }

    @Composable
    private fun AskSmsPermissionScreen() {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                Text("SmsWidget")

                Text("SmsWidget transforms experience of sending standard SMS messages to just one click! In order to use it, please grant permission to send SMS")

                Button(onClick = {
                    requestSmsPermission()
                }) {
                    Text("Grant SMS permission")
                }
            }
        }
    }

    @Composable
    private fun SmsPermissionDeniedScreen() {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                Text("SmsWidget")
                Text("Oops! Looks like permission to send SMS is not granted. Please open the Settings app and grant SmsWidget permission to send SMS")
            }
        }
    }


    @Composable
    private fun NormalScreen() {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                Text("SmsWidget")
            }
        }
    }

    private fun requestSmsPermission() {
        sendSmsPermissionLauncher.launch(Manifest.permission.SEND_SMS)
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


