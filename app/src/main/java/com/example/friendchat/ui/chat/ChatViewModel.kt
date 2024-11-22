package com.example.friendchat.ui.chat


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.friendchat.data.repository.ChatRepository
import com.example.friendchat.data.repository.UserRepository
import com.example.friendchat.model.Chat
import com.example.friendchat.model.User
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _chats = MutableLiveData<List<Chat>>()
    val chats: LiveData<List<Chat>> get() = _chats



    fun loadChatsForUser(uid: String) {
        viewModelScope.launch {
            val chats = chatRepository.getChatsForUser(uid)
            _chats.postValue(chats)
        }
    }


    suspend fun createOrGetChat(user: User): Chat {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid

        var chat = chatRepository.getChatWithUser(user.id) // Check if a chat already exists
        if (chat == null) {
            chat = Chat(
                id = System.currentTimeMillis().toString(), // Generate unique chat ID
                participants = user.id,
                lastMessageId = null,
                timestamp = System.currentTimeMillis()
            )
            addChat(chat,currentUserUid.toString()) // Add the new chat
        }
        return chat
    }
    fun getChatByParticipant(participantId: String): Chat? {
        return chats.value?.find { it.participants == participantId }
    }

    fun addChat(chat: Chat, uid: String) {
        viewModelScope.launch {
            chatRepository.addChat(chat)
            loadChatsForUser(uid) // Pass the UID
        }
    }

//    suspend fun syncChatsFromFirebase() {
//        val snapshot = firestore.collection("chats").get().await()
//        val chats = snapshot.toObjects(Chat::class.java)
//
//        val uniqueChats = chats.distinctBy { it.participants } // Ensure unique chats
//        chatDao.insertChats(uniqueChats)
//    }

    suspend fun getUserById(userId: String): User? {
        return userRepository.getAllUsers().find { it.id == userId }
    }
}