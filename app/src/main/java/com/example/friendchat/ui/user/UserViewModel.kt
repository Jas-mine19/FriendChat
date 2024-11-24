package com.example.friendchat.ui.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.friendchat.data.repository.UserRepository
import com.example.friendchat.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> get() = _users

    init {
        Log.d("UserViewModel", "Initializing UserViewModel")
        loadUsersFromRoomList()
        syncUsersFromFirebase()
    }

    fun loadUsersFromRoomList() {
        viewModelScope.launch {
            val usersFromRoom = userRepository.getAllUsersList()
            Log.d("UserViewModel", "Loaded users from Room in ViewModel: $usersFromRoom")
            _users.postValue(usersFromRoom)
        }
    }
    fun syncUsersFromFirebase() {
        viewModelScope.launch {
            userRepository.syncUsersFromFirebase()
            loadUsersFromRoomList()
        }
    }


}