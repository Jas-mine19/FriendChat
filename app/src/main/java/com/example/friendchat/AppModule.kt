package com.example.friendchat

import android.content.Context
import com.example.friendchat.data.AppDatabase
import com.example.friendchat.data.dao.ChatDao
import com.example.friendchat.data.dao.MessageDao
import com.example.friendchat.data.dao.UserDao
import com.example.friendchat.data.repository.ChatRepository
import com.example.friendchat.data.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideChatRepository(
        database: AppDatabase,
        firestore: FirebaseFirestore
    ): ChatRepository {
        return ChatRepository(
            chatDao = database.chatDao(),
            messageDao = database.messageDao(),
            firestore = firestore
        )
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        database: AppDatabase,
        firestore: FirebaseFirestore
    ): UserRepository {
        return UserRepository(
            userDao = database.userDao(),
            firestore = firestore
        )
    }

    @Provides
    fun provideMessageDao(appDatabase: AppDatabase): MessageDao {
        return appDatabase.messageDao()
    }

    @Provides
    fun provideChatDao(appDatabase: AppDatabase): ChatDao {
        return appDatabase.chatDao()
    }

    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }
}