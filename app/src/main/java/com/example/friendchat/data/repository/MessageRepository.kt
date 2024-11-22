package com.example.friendchat.data.repository

import androidx.lifecycle.LiveData
import com.example.friendchat.data.dao.MessageDao
import com.example.friendchat.model.Message
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MessageRepository @Inject constructor(
    private val messageDao: MessageDao,
    private val firestore: FirebaseFirestore
) {

   fun getMessagesForChat(chatId: String): List<Message> {
        return messageDao.getMessagesForChat(chatId)
    }
    suspend fun getLastMessage(lastMessageId: String?): Message? {
        return if (lastMessageId != null) {
            messageDao.getMessageById(lastMessageId)
        } else {
            null
        }
    }
    suspend fun sendMessage(message: Message) {
        withContext(Dispatchers.IO) {
            messageDao.insertMessage(message)
        }
        try {
            firestore.collection("messages").document(message.id).set(message).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun syncMessagesFromFirebase(chatId: String) {
        withContext(Dispatchers.IO) {
            try {
                val snapshot = firestore.collection("messages")
                    .whereEqualTo("chatId", chatId)
                    .get()
                    .await()
                val messages = snapshot.toObjects(Message::class.java)
                messageDao.insertMessages(messages)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}