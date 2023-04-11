package uk.ac.aber.dcs.cs39440.myvitalife.model

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import uk.ac.aber.dcs.cs39440.myvitalife.datastorage.DataStorage
import javax.inject.Inject

@HiltViewModel
class DataViewModel @Inject constructor(
    private val storage: DataStorage
) : ViewModel() {

    /**
     * Saves a string value in a given key
     */
    fun saveString(value: String, key: String) {
        viewModelScope.launch {
            storage.putString(value, key)
        }
    }

    /**
     * Returns a string value based on a given key
     */
    fun getString(key: String): String? = runBlocking {
        storage.getString(key)?.trim()
    }

    /**
     * Saves a boolean value in a given key
     */
    fun saveBoolean(value: Boolean, key: String) {
        viewModelScope.launch {
            storage.putBoolean(value, key)
        }
    }

    /**
     * Returns a boolean value based on a given key
     */
    fun getBoolean(key: String): Boolean? = runBlocking {
        storage.getBoolean(key)
    }
}