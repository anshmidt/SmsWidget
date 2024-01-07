package com.anshmidt.smswidget.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.lifecycleScope
import com.anshmidt.smswidget.MainViewModel
import com.anshmidt.smswidget.datasources.database.MessageEntity
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

    private fun requestSmsPermission() {
        sendSmsPermissionLauncher.launch(Manifest.permission.SEND_SMS)
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
        var showAddDialog by remember { mutableStateOf(false) }

        Scaffold(
            floatingActionButton = {
                AddButton(onAddButtonClicked = { showAddDialog = true })
            }
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Row {
                    Text("SmsWidget")
                    MessageList()
                }
            }
        }

        if (showAddDialog) {
            Dialog(
                onDismissRequest = { showAddDialog = false }
            ) {
                AddDialogContent(
                    onCancelButtonClicked = { showAddDialog = false },
                    onSaveButtonClicked = { showAddDialog = false }
                )
            }
        }
    }

    @Composable
    private fun AddDialogContent(
        onCancelButtonClicked: () -> Unit,
        onSaveButtonClicked: () -> Unit
    ) {
        Column {
            TextField(value = "9011", onValueChange = {})
            TextField(value = "A90", onValueChange = {})
            TextField(value = "9011: A90", onValueChange = {})
            Button(
                onClick = onCancelButtonClicked,
            ) {
                Text("Cancel")
            }
            Button(
                onClick = onSaveButtonClicked
            ) {
                Text("Save")
            }
        }
    }

    @Composable
    private fun MessageList() {

    }

    @Composable
    private fun Message(
        messageEntity: MessageEntity,
        onEditButtonClicked: (MessageEntity) -> Unit,
        onDeleteButtonClicked: (MessageEntity) -> Unit
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Column {
                Text("Phone number: ${messageEntity.phoneNumber}")
                Text("Message text: ${messageEntity.messageText}")
                Text("Caption: ${messageEntity.caption}")

                Row {
                    IconButton(onClick = { onEditButtonClicked(messageEntity) }) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = { onDeleteButtonClicked(messageEntity) }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun AddButton(onAddButtonClicked: () -> Unit) {
        FloatingActionButton(
            onClick = {
                onAddButtonClicked()
            },
            shape = MaterialTheme.shapes.small.copy(CornerSize(percent = 50)),
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }




}




