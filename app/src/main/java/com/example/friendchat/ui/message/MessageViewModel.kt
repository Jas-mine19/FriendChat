
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
class MessageViewModel @Inject constructor(
    private val messageRepository: MessageRepository
) : ViewModel() {


    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages

    fun loadMessagesForChat(chatId: String) {
        viewModelScope.launch {
            val messagesFromRoom = messageRepository.getMessagesForChat(chatId)
            _messages.postValue(messagesFromRoom)
        }
    }

    fun sendMessage(message: Message) {
        viewModelScope.launch {
            messageRepository.sendMessage(message)
            loadMessagesForChat(message.chatId)
        }
    }
}