package com.anshmidt.smswidget

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val _permissionGranted = MutableLiveData<Boolean>()
    val permissionGranted: LiveData<Boolean> get() = _permissionGranted

    fun setPermissionGranted(isGranted: Boolean) {
        _permissionGranted.value = isGranted
    }

    private val _permissionDenied = MutableLiveData<Boolean>()
    val permissionDenied: LiveData<Boolean> get() = _permissionDenied

    fun setPermissionDenied(isDenied: Boolean) {
        _permissionDenied.value = isDenied
    }
}