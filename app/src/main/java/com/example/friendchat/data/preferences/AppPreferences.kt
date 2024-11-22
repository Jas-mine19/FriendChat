package com.example.friendchat.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


private val Context.authDataStore by preferencesDataStore(name = "app_prefs")

@Singleton
class AppPreferences @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        private val IS_AUTHENTICATED_KEY = booleanPreferencesKey("is_authenticated")
        private val USER_NAME_KEY = stringPreferencesKey("user_name")
    }


    suspend fun setIsAuthenticated(isAuthenticated: Boolean) {
        context.authDataStore.edit { preferences ->
            preferences[IS_AUTHENTICATED_KEY] = isAuthenticated
        }
    }


    val isAuthenticated: Flow<Boolean> = context.authDataStore.data.map { preferences ->
        preferences[IS_AUTHENTICATED_KEY] ?: false
    }

    suspend fun setUserName(userName: String) {
        context.authDataStore.edit { preferences ->
            preferences[USER_NAME_KEY] = userName
        }
    }

    val userName: Flow<String?> = context.authDataStore.data.map { preferences ->
        preferences[USER_NAME_KEY]
    }
}