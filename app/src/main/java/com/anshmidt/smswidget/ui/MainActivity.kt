package com.anshmidt.smswidget.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.lifecycleScope
import com.anshmidt.smswidget.MainViewModel
import com.anshmidt.smswidget.datasources.database.MessageEntity
import com.anshmidt.smswidget.ui.theme.AppTheme
import com.anshmidt.smswidget.ui.theme.Black600
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
        val isPermissionGranted by viewModel.permissionGranted.observeAsState(
            initial = isSmsPermissionGranted()
        )
        val isPermissionDenied by viewModel.permissionDenied.observeAsState(initial = false)
        val uiState by viewModel.uiState.observeAsState(UiState(emptyList()))

        if (isPermissionDenied) {
            SmsPermissionDeniedScreen()
            return
        }

        if (isPermissionGranted) {
            NormalScreen(
                uiState = uiState,
                onNewMessageAdded = { newMessage -> viewModel.onNewMessageAdded(newMessage) }
            )
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
    private fun NormalScreen(
        uiState: UiState,
        onNewMessageAdded: (messageEntity: MessageEntity) -> Unit
    ) {
        var showAddDialog by remember { mutableStateOf(false) }

        Scaffold(
            floatingActionButton = {
                AddButton(onAddButtonClicked = { showAddDialog = true })
            }
        ) { contentPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                color = MaterialTheme.colorScheme.background
            ) {
                Row {
                    Text("SmsWidget")
                    MessageList(uiState.messages)
                }
            }
        }

        if (showAddDialog) {
            Dialog(
                onDismissRequest = { showAddDialog = false },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                AddDialogContent(
                    onCancelButtonClicked = { showAddDialog = false },
                    onSaveButtonClicked = { newMessage ->
                        onNewMessageAdded(newMessage)
                        showAddDialog = false
                    }
                )
            }
        }
    }

    @Composable
    private fun AddDialogContent(
        onCancelButtonClicked: () -> Unit,
        onSaveButtonClicked: (messageEntity: MessageEntity) -> Unit
    ) {
        var phoneNumber by remember { mutableStateOf("9011") }
        var message by remember { mutableStateOf("A90") }
        var caption by remember { mutableStateOf("9011: A90") }

        Column(
            modifier = Modifier
                .background(Black600)
                .padding(24.dp)
                .fillMaxSize()
        ) {
            AddDialogTextField(
                label = "Phone number",
                text = phoneNumber,
                onValueChange = { phoneNumber = it }
            )
            AddDialogTextField(
                label = "Message",
                text = message,
                onValueChange = { message = it }
            )
            AddDialogTextField(
                label = "Caption",
                text = caption,
                onValueChange = { caption = it }
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = onCancelButtonClicked,
                ) {
                    Text("Cancel")
                }
                Button(
                    onClick = { onSaveButtonClicked(MessageEntity(
                        phoneNumber = phoneNumber,
                        messageText = message,
                        caption = caption
                    )) }
                ) {
                    Text("Save")
                }
            }

        }
    }

    @Composable
    private fun AddDialogTextField(label: String, text: String, onValueChange: (String) -> Unit) {
        TextField(
            value = text,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                focusedLabelColor = MaterialTheme.colorScheme.onBackground,
                unfocusedLabelColor = MaterialTheme.colorScheme.onBackground
            )
        )
    }

    @Composable
    private fun MessageList(messages: List<MessageEntity>) {
        LazyColumn {
            items(messages.size) { index ->
                Message(
                    messageEntity = messages[index],
                    onEditButtonClicked = { },
                    onDeleteButtonClicked = { }
                )
            }
        }
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
            containerColor = MaterialTheme.colorScheme.primary,
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




