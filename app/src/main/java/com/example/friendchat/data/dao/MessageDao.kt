package com.example.friendchat.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.friendchat.model.Message




    @Dao
    interface MessageDao {

        @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY timestamp ASC")
        fun getMessagesByChatId(chatId: String): List<Message>

        @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY timestamp ASC")
        suspend fun getMessagesForChat(chatId: String): List<Message>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertMessages(messages: List<Message>)

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertMessage(message: Message)
    }

