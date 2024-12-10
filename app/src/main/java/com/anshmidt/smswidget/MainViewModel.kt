package com.anshmidt.smswidget

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anshmidt.smswidget.datasources.database.MessageEntity
import com.anshmidt.smswidget.ui.UiState

class MainViewModel : ViewModel() {

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> = _uiState

    private val _permissionGranted = MutableLiveData<Boolean>()
    val permissionGranted: LiveData<Boolean> get() = _permissionGranted

    private val _permissionDenied = MutableLiveData<Boolean>()
    val permissionDenied: LiveData<Boolean> get() = _permissionDenied

    fun setPermissionGranted(isGranted: Boolean) {
        _permissionGranted.value = isGranted
    }

    fun setPermissionDenied(isDenied: Boolean) {
        _permissionDenied.value = isDenied
    }

    fun onNewMessageAdded(messageEntity: MessageEntity) {
        Log.d("123", "new message added: $messageEntity")
    }
}