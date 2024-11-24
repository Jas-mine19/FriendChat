package com.example.friendchat.data.repository

import androidx.lifecycle.LiveData
import com.example.friendchat.data.dao.MessageDao
import com.example.friendchat.model.Message
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageRepository @Inject constructor(
    private val messageDao: MessageDao,
    private val firestore: FirebaseFirestore
) {
    suspend fun syncMessagesFromFirebase(chatId: String) {
        val messagesFromFirebase = firestore.collection("messages")
            .whereEqualTo("chatId", chatId)
            .get()
            .await()
            .toObjects(Message::class.java)
        messageDao.insertMessages(messagesFromFirebase)
    }

    suspend fun getMessagesFromRoom(chatId: String): List<Message> {
        return messageDao.getMessagesForChat(chatId)
    }
    suspend fun getMessagesForChat(chatId: String): List<Message> {
        return withContext(Dispatchers.IO) {
            val messagesFromRoom = messageDao.getMessagesByChatId(chatId)
            if (messagesFromRoom.isEmpty()) {
                val snapshot = firestore.collection("messages")
                    .whereEqualTo("chatId", chatId)
                    .get()
                    .await()
                val messagesFromFirebase = snapshot.toObjects(Message::class.java)
                messageDao.insertMessages(messagesFromFirebase)
                return@withContext messagesFromFirebase
            }
            messagesFromRoom
        }
    }

    suspend fun sendMessage(message: Message) {
        withContext(Dispatchers.IO) {
            firestore.collection("messages").document(message.id).set(message).await()
            messageDao.insertMessage(message)
        }
    }
}