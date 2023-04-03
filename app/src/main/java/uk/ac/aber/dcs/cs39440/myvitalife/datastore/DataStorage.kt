package uk.ac.aber.dcs.cs39440.myvitalife.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStore(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("UserData")
    }

    fun getString(key: String): Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[stringPreferencesKey(key)] ?: ""
        }

    suspend fun saveString(string: String, key: String) {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = string
        }
    }
}