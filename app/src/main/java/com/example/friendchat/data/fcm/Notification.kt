package com.example.friendchat.data.fcm



import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.friendchat.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class Notification : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val title = remoteMessage.notification?.title ?: "New Message"
        val body = remoteMessage.notification?.body ?: "You have a new message"
        val chatId = remoteMessage.data["chatId"]

//        showNotification(title, body, chatId)
    }

//    private fun showNotification(title: String, body: String, chatId: String?) {
////        val intent = Intent(this, ChatActivity::class.java).apply {
////            putExtra("chatId", chatId)
////            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
////        }
//
//        val pendingIntent = PendingIntent.getActivity(
//            this,
//            0,
//            intent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//
//        val channelId = "friendchat_notifications"
//        val channelName = "Messages"
//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                channelId,
//                channelName,
//                NotificationManager.IMPORTANCE_HIGH
//            )
//            notificationManager.createNotificationChannel(channel)
//        }
//
//        val notification = NotificationCompat.Builder(this, channelId)
//            .setContentTitle(title)
//            .setContentText(body)
////            .setSmallIcon(R.drawable.ic_notification)
//            .setContentIntent(pendingIntent)
//            .setAutoCancel(true)
//            .build()
//
//        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
//    }
}