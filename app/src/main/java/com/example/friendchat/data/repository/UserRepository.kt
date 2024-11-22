package com.example.friendchat.data.repository

import com.example.friendchat.data.dao.UserDao
import com.example.friendchat.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val firestore: FirebaseFirestore
) {

    fun listenForUserUpdates(onUsersUpdated: (List<User>) -> Unit) {
        firestore.collection("users")
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    println("Error listening to user updates: ${error.message}")
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    val updatedUsers = snapshots.toObjects(User::class.java)
                    onUsersUpdated(updatedUsers)
                }
            }
    }
    suspend fun getAllUsers(): List<User> {
        return withContext(Dispatchers.IO) {
            try {
                val snapshot = firestore.collection("users").get().await()
                val usersFromFirebase = snapshot.toObjects(User::class.java)
                userDao.insertUsers(usersFromFirebase)
                usersFromFirebase
            } catch (e: Exception) {
                e.printStackTrace()
                userDao.getAllUsers().value ?: emptyList() // Convert LiveData to List
            }
        }
    }

    suspend fun getUserById(userId: String): User? {
        return withContext(Dispatchers.IO) {
            userDao.getUserById(userId)
        }
    }
}