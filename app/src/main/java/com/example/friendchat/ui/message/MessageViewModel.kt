package com.example.friendchat.ui.message

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.friendchat.data.repository.MessageRepository
import com.example.friendchat.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(private val messageRepository: MessageRepository) : ViewModel() {
    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> = _messages

    fun loadMessages(chatId: String) {
        viewModelScope.launch {
            val messages = messageRepository.getMessagesForChat(chatId)
            _messages.value = messages
        }
    }
     fun getMessages(chatId: String): List<Message> {
        return messageRepository.getMessagesForChat(chatId)
    }

    fun sendMessage(message: Message) {
        viewModelScope.launch {
            messageRepository.sendMessage(message)
        }
    }

    fun syncMessages(chatId: String) {
        viewModelScope.launch {
            messageRepository.syncMessagesFromFirebase(chatId)
        }
    }
}