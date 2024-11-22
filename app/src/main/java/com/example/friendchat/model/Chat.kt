package com.example.friendchat.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chats")
data class Chat(
    @PrimaryKey val id: String = "",
    val participants: String = "",
    val lastMessageId: String? = null,
    val lastMessageContent: String? = null,
    val lastMessageSenderId: String? = null,
    val lastMessageTimestamp: Long? = null,
    val timestamp: Long = 0L
)

data class LastMessage(
    val id: String = "",
    val content: String = "",
    val senderId: String = "",
    val timestamp: Long = 0L
)