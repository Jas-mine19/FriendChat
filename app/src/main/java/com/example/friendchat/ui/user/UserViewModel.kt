package com.example.friendchat.ui.user

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
        syncUsersFromFirebase()
    }

    fun syncUsersFromFirebase() {
        viewModelScope.launch {
            val usersFromDatabase = userRepository.getAllUsers()
            _users.postValue(usersFromDatabase)
        }
    }

    private fun loadUsers() {
        viewModelScope.launch {
            val usersFromRoom = userRepository.getAllUsers()
            _users.postValue(usersFromRoom)
        }
    }



    private fun listenForUserUpdates() {
        userRepository.listenForUserUpdates { updatedUsers ->
            _users.postValue(updatedUsers)
        }
    }
}