package uk.ac.aber.dcs.cs39440.myvitalife.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import uk.ac.aber.dcs.cs39440.myvitalife.MainActivity
import uk.ac.aber.dcs.cs39440.myvitalife.R

class MyNotification(private val context: Context, private val title: String, private val msg: String) {

    companion object {
        private const val CHANNEL_ID = "FCM100"
        private const val CHANNEL_NAME = "FCMMessage"
        private const val NOTIFICATION_ID = 100
    }

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private lateinit var notificationChannel: NotificationChannel
    private lateinit var notificationBuilder: NotificationCompat.Builder

    init {
        createNotificationChannel()
        buildNotification()
    }

    private fun createNotificationChannel() {
        notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(notificationChannel)
    }

    private fun buildNotification() {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.lifestyle)
            .addAction(R.drawable.lifestyle, "Open Message", pendingIntent)
            .setContentTitle(title)
            .setContentText(msg)
            .setAutoCancel(true)
    }

    fun showNotification() {
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }
}