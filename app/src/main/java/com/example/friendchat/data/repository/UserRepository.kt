package com.example.friendchat.data.repository

import android.util.Log
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

    suspend fun syncUsersFromFirebase() {
        withContext(Dispatchers.IO) {
            try {
                val snapshot = firestore.collection("users").get().await()
                val usersFromFirebase = snapshot.toObjects(User::class.java)
                Log.d("UserRepository", "Fetched users from Firebase: $usersFromFirebase")
                userDao.deleteAllUsers()
                userDao.insertUsers(usersFromFirebase)

                val usersInRoom = userDao.getAllUsersList()
                Log.d("UserRepository", "Users in Room after sync: $usersInRoom")
            } catch (e: Exception) {
                Log.e("UserRepository", "Error syncing users from Firebase", e)
            }
        }
    }

    suspend fun getAllUsersList(): List<User> {
        return withContext(Dispatchers.IO) {
            val users = userDao.getAllUsersList()
            Log.d("UserRepository", "Fetched users from Room: $users")
            users
        }
    }




    suspend fun getUserById(userId: String): User? {
        return withContext(Dispatchers.IO) {
            userDao.getUserById(userId)
        }
    }
}