package com.example.friendchat.data.repository

import com.example.friendchat.data.dao.ChatDao
import com.example.friendchat.model.Chat
import com.example.friendchat.model.ChatWithParticipants
import com.example.friendchat.model.Participant
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val chatDao: ChatDao
) {

    suspend fun addChatWithParticipants(chat: Chat, participants: List<String>) {
        chatDao.insertChat(chat)
        val participantEntities = participants.map { Participant(chatId = chat.id, userId = it) }
        chatDao.insertParticipants(participantEntities)
    }
    suspend fun createOrGetChatWithParticipants(userId: String): ChatWithParticipants {
        val existingChat = chatDao.getChatWithUser(userId)
        if (existingChat != null) {
            return existingChat
        }

        val newChat = Chat(
            id = System.currentTimeMillis().toString(),
            timestamp = System.currentTimeMillis()
        )
        chatDao.insertChat(newChat)

        val participant = Participant(
            chatId = newChat.id,
            userId = userId
        )
        chatDao.insertParticipants(listOf(participant))

        return chatDao.getChatWithParticipants(newChat.id)
            ?: throw IllegalStateException("Failed to create or retrieve chat")
    }

    private fun generateUniqueId(): String {
        return System.currentTimeMillis().toString() // Or use UUID.randomUUID().toString()
    }

    suspend fun getAllChatsWithParticipants(): List<ChatWithParticipants> {
        return chatDao.getAllChatsWithParticipants()
    }

    suspend fun getOrCreateChat(participantId: String): Chat {
        return chatDao.getChatByParticipant(participantId) ?: run {
            val chat = Chat(
                id = generateUniqueId(),
                timestamp = System.currentTimeMillis()
            )
            chatDao.insertChat(chat)
            chatDao.insertParticipants(listOf(Participant(chatId = chat.id, userId = participantId)))
            chat
        }
    }



}