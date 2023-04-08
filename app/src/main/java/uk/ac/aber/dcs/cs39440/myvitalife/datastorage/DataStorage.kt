package uk.ac.aber.dcs.cs39440.myvitalife.datastorage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private const val PREFERENCES_NAME = "DataStorage"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

class DataStorage(
    private val context: Context
) {

    /**
     * Saves a string value in a given key
     */
    suspend fun putString(value: String, key: String) {
        val preferencesKey = stringPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[preferencesKey] = value
        }
    }

    /**
     * Returns a string value based on a given key
     */
    suspend fun getString(key: String): String? {
        val preferencesKey = stringPreferencesKey(key)
        return context.dataStore.data.first()[preferencesKey]
    }

    /**
     * Saves a boolean value in a given key
     */
    suspend fun putBoolean(value: Boolean, key: String) {
        context.dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(key)] = value
        }
    }

    /**
     * Returns a boolean value based on a given key
     */
    suspend fun getBoolean(key: String): Boolean? {
        val preferencesKey = booleanPreferencesKey(key)
        return context.dataStore.data.first()[preferencesKey]
    }
}