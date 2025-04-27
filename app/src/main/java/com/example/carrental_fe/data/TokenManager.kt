package com.example.carrental_fe.data


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "tokens")

class TokenManager(context: Context) {
    private val appContext = context.applicationContext
    companion object {
        val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        val ROLE = stringPreferencesKey("role")
        val DEVICE_TOKEN = stringPreferencesKey("device_token")
    }

    suspend fun saveTokens(accessToken: String, refreshToken: String, role: String) {
        appContext.dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN_KEY] = accessToken
            prefs[REFRESH_TOKEN_KEY] = refreshToken
            prefs[ROLE] = role
        }
    }
    suspend fun clearTokens(){
        appContext.dataStore.edit {
            it.clear()
        }
    }
    suspend fun saveDeviceToken(deviceToken: String) {
        appContext.dataStore.edit { prefs ->
            prefs[DEVICE_TOKEN] = deviceToken
        }
    }

    suspend fun getDeviceToken(): String? =
        appContext.dataStore.data.map { it[DEVICE_TOKEN] }.firstOrNull()

    suspend fun getAccessToken(): String? =
        appContext.dataStore.data.map { it[ACCESS_TOKEN_KEY] }.firstOrNull()

    suspend fun getRefreshToken(): String? =
        appContext.dataStore.data.map { it[REFRESH_TOKEN_KEY] }.firstOrNull()
    suspend fun getRole(): String? =
        appContext.dataStore.data.map { it[ROLE] }.firstOrNull()
}