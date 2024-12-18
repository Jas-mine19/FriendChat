package com.example.friendchat.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation


@Entity(tableName = "chats")
data class Chat(
    @PrimaryKey val id: String = "",
    val lastMessageId: String? = null,
    var lastMessageContent: String? = null,
    val lastMessageSenderId: String? = null,
    var lastMessageTimestamp: Long? = null,
    val timestamp: Long = 0L
)


@Entity(tableName = "participants")
data class Participant(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val chatId: String,
    val userId: String
)



data class ChatWithParticipants(
    @Embedded val chat: Chat,
    @Relation(
        parentColumn = "id",
        entityColumn = "chatId"
    )
    val participants: List<Participant>
)