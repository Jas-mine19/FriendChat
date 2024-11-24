package com.example.friendchat.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.friendchat.data.dao.ChatDao
import com.example.friendchat.data.dao.MessageDao
import com.example.friendchat.data.dao.UserDao
import com.example.friendchat.model.Chat
import com.example.friendchat.model.Message
import com.example.friendchat.model.Participant
import com.example.friendchat.model.User


@Database(
    entities = [User::class, Chat::class, Message::class, Participant::class],
    version = 4,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun messageDao(): MessageDao
    abstract fun chatDao(): ChatDao


    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }

        }

    }

}
