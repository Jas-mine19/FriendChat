package com.example.friendchat.ui

import androidx.lifecycle.ViewModel
import com.example.friendchat.data.preferences.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val appPreferences: AppPreferences
) : ViewModel() {

    val isAuthenticated: Flow<Boolean> = appPreferences.isAuthenticated
}