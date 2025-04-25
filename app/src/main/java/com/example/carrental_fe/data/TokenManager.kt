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

class TokenManager(private val context: Context) {

    companion object {
        val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        val ROLE = stringPreferencesKey("role")
    }

    suspend fun saveTokens(accessToken: String, refreshToken: String, role: String) {
        context.dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN_KEY] = accessToken
            prefs[REFRESH_TOKEN_KEY] = refreshToken
            prefs[ROLE] = role
        }
    }
    suspend fun clearTokens(){
        context.dataStore.edit {
            it.clear()
        }
    }

    suspend fun getAccessToken(): String? =
        context.dataStore.data.map { it[ACCESS_TOKEN_KEY] }.firstOrNull()

    suspend fun getRefreshToken(): String? =
        context.dataStore.data.map { it[REFRESH_TOKEN_KEY] }.firstOrNull()
    suspend fun getRole(): String? =
        context.dataStore.data.map { it[ROLE] }.firstOrNull()
}