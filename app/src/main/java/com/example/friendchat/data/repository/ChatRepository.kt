package com.example.friendchat.data.repository

import androidx.lifecycle.LiveData
import com.example.friendchat.data.dao.ChatDao
import com.example.friendchat.data.dao.MessageDao
import com.example.friendchat.model.Chat
import com.example.friendchat.model.Message
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ChatRepository(
    private val chatDao: ChatDao,
    private val messageDao: MessageDao,
    private val firestore: FirebaseFirestore
) {



    suspend fun getChatsForUser(uid: String): List<Chat> {
        return try {
            val snapshot = firestore.collection("chats")
                .whereArrayContains("participants", uid)
                .get()
                .await()
            snapshot.toObjects(Chat::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }



    suspend fun getChatWithUser(userId: String): Chat? {
        return chatDao.getChatByParticipant(userId)
    }

    suspend fun addChat(chat: Chat) {
        withContext(Dispatchers.IO) {
            try {
                chatDao.insertChat(chat) // Insert into Room
                firestore.collection("chats").document(chat.id).set(chat).await() // Sync to Firebase
            } catch (e: Exception) {
                println("Error adding chat: ${e.message}")
            }
        }
    }

    suspend fun deleteChat(chat: Chat) {
        withContext(Dispatchers.IO) {
            try {
                chatDao.deleteChat(chat.id) // Delete from Room
                firestore.collection("chats").document(chat.id).delete().await() // Delete from Firebase
            } catch (e: Exception) {
                println("Error deleting chat: ${e.message}")
            }
        }
    }

    fun getMessagesForChat(chatId: String): List<Message> {
        return messageDao.getMessagesForChat(chatId)
    }

    fun listenForChatUpdates(onChatsUpdated: (List<Chat>) -> Unit) {
        firestore.collection("chats")
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    println("Error listening to chat updates: ${error.message}")
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    val chats = snapshots.toObjects(Chat::class.java)
                    // Pass the updated chats to the callback
                    onChatsUpdated(chats)
                }
            }
    }
}