package uk.ac.aber.dcs.cs39440.myvitalife.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private const val PREFERENCES_NAME = "MyDataStore"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

/**
 * DataStore Preferences class used for persistent data storage
 * @author Maciej Traczyk
 */
class MyDataStore(
    private val context: Context
) {
    /**
     * Saves a boolean value for a given key.
     *
     * @param value The value to save.
     * @param key The key associated with the value.
     */
    suspend fun saveBoolean(value: Boolean, key: String) {
        context.dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(key)] = value
        }
    }

    /**
     * Returns a boolean value based on a given key.
     *
     * @param key The key associated with the value to retrieve.
     * @return The value associated with the key, or null if it does not exist.
     */
    suspend fun getBoolean(key: String): Boolean? {
        val preferencesKey = booleanPreferencesKey(key)
        return context.dataStore.data.first()[preferencesKey]
    }
}