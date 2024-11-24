package com.example.friendchat.data.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.friendchat.model.Chat
import com.example.friendchat.model.ChatWithParticipants
import com.example.friendchat.model.Participant

@Dao
interface ChatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat: Chat)

    @Query("SELECT * FROM chats")
    suspend fun getAllChats(): List<Chat>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParticipants(participants: List<Participant>)

    @Transaction
    @Query("SELECT * FROM chats WHERE id = :chatId")
    suspend fun getChatWithParticipants(chatId: String): ChatWithParticipants

    @Transaction
    @Query("SELECT * FROM chats")
    suspend fun getAllChatsWithParticipants(): List<ChatWithParticipants>

    @Transaction
    @Query("""
        SELECT * FROM chats
        WHERE id IN (
            SELECT chatId FROM participants WHERE userId = :userId
        )
        LIMIT 1
    """)
    suspend fun getChatWithUser(userId: String): ChatWithParticipants?
    @Transaction
    @Query("""
        SELECT chats.* FROM chats
        INNER JOIN participants ON chats.id = participants.chatId
        WHERE participants.userId = :userId
    """)
    suspend fun getChatByParticipant(userId: String): Chat?
}