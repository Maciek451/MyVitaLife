package uk.ac.aber.dcs.cs39440.myvitalife.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import uk.ac.aber.dcs.cs39440.myvitalife.datastore.MyDataStore
import javax.inject.Inject

/**
 * ViewModel that manages the storage and retrieval of data using a given MyDataStore instance.
 *
 * @property storage The MyDataStore instance used to store and retrieve data.
 */
@HiltViewModel
class DataViewModel @Inject constructor(
    private val storage: MyDataStore
) : ViewModel() {

    /**
     * Saves a boolean value with the given key to the data store.
     *
     * @param value The boolean value to save.
     * @param key The key under which to save the value.
     */
    fun saveBoolean(value: Boolean, key: String) {
        viewModelScope.launch {
            storage.saveBoolean(value, key)
        }
    }

    /**
     * Retrieves a boolean value from the data store with the given key.
     *
     * @param key The key under which the value is stored.
     * @return The boolean value associated with the key, or null if no such value exists.
     */
    fun getBoolean(key: String): Boolean? = runBlocking {
        storage.getBoolean(key)
    }
}