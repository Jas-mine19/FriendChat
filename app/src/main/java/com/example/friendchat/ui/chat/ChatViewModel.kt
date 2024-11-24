package com.example.friendchat.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.friendchat.data.repository.ChatRepository
import com.example.friendchat.data.repository.MessageRepository
import com.example.friendchat.data.repository.UserRepository
import com.example.friendchat.model.Chat
import com.example.friendchat.model.ChatWithParticipants
import com.example.friendchat.model.Message
import com.example.friendchat.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository,
    private val userRepository:UserRepository
) : ViewModel() {

    private val _chats = MutableLiveData<List<ChatWithParticipants>>()
    val chats: LiveData<List<ChatWithParticipants>> get() = _chats


    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages

    suspend fun getUserById(userId: String): User? {
        return userRepository.getUserById(userId) // Fetch the user from the repository
    }


    fun loadChatsFromRoom() {
        viewModelScope.launch {
            try {
                val chatsFromRoom = chatRepository.getAllChatsWithParticipants()
                _chats.postValue(chatsFromRoom)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun createOrGetChat(user: User, onChatCreated: (ChatWithParticipants) -> Unit) {
        viewModelScope.launch {
            try {
                val chatWithParticipants = chatRepository.createOrGetChatWithParticipants(user.id)
                onChatCreated(chatWithParticipants)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun fetchMessages(chatId: String) {
        viewModelScope.launch {
            try {
                messageRepository.syncMessagesFromFirebase(chatId)
                val messagesFromRoom = messageRepository.getMessagesFromRoom(chatId)
                _messages.postValue(messagesFromRoom)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }}