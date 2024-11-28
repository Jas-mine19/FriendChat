package com.example.friendchat.model

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "messages")
data class Message(
    @PrimaryKey val id: String,
    val chatId: String = "",
    val senderId: String = "",
    val receiverId:String ="",
    val content: String = "",
    val type: String = "",
    val timestamp: Long =0L
) {
    constructor() : this("", "", "", "", "", "", 0L)
}