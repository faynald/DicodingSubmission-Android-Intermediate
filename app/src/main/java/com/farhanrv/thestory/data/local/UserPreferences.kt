package com.farhanrv.thestory.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.farhanrv.thestory.model.User
import kotlinx.coroutines.flow.first

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences")

class UserPreference(private val context: Context) {

    suspend fun getToken(): String? {
        val firstData = context.dataStore.data.first()
        return firstData[TOKEN]
    }

    suspend fun saveUser(token: String, user: User) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN] = token
            preferences[USER_EMAIL] = user.userEmail
            preferences[USER_NAME] = user.userName
            preferences[USER_PASS] = user.userPass
            preferences[IS_LOGIN] = true
        }
    }

    suspend fun logoutUser() {
        context.dataStore.edit { preferences ->
            preferences[USER_EMAIL] = ""
            preferences[USER_NAME] = ""
            preferences[USER_PASS] = ""
            preferences[IS_LOGIN] = false
            preferences[TOKEN] = ""
        }
    }

    companion object {
        val TOKEN = stringPreferencesKey("token")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_PASS = stringPreferencesKey("user_pass")
        val IS_LOGIN = booleanPreferencesKey("user_state")
    }
}