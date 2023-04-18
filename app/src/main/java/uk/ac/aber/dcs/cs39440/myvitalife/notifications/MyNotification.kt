package uk.ac.aber.dcs.cs39440.myvitalife.notifications

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.NotificationCompat
import uk.ac.aber.dcs.cs39440.myvitalife.MainActivity
import uk.ac.aber.dcs.cs39440.myvitalife.R
import java.util.*

/**
 * Represents a notification with a title and message that can be scheduled to repeat at a given interval.
 *
 * @param context The context in which the notification will be displayed.
 * @param title The title of the notification.
 * @param msg The message body of the notification.
 */
class MyNotification(
    private val context: Context,
    private val title: String,
    private val msg: String,
) {

    companion object {
        private const val CHANNEL_ID = "MVL100"
        private const val CHANNEL_NAME = "MVL Notifications"
        private const val NOTIFICATION_ID = 100
    }

    /**
     * The notification manager that handles the notification channel and displaying the notification.
     */
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    /**
     * The notification channel associated with the notification.
     */
    private lateinit var notificationChannel: NotificationChannel
    /**
     * The builder used to construct the notification.
     */
    private lateinit var notificationBuilder: NotificationCompat.Builder

    init {
        createNotificationChannel()
        buildNotification()
    }

    /**
     * Creates a notification channel.
     */
    private fun createNotificationChannel() {
        notificationChannel =
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(notificationChannel)
    }

    /**
     * Builds the notification and sets the pending intent for opening the app.
     */
    private fun buildNotification() {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent =
            getActivity(context, 0, intent, FLAG_IMMUTABLE)

        notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.lifestyle)
            .addAction(R.drawable.lifestyle, "Open the app", pendingIntent)
            .setContentTitle(title)
            .setContentText(msg)
            .setAutoCancel(true)
    }

    /**
     * Schedules the repeating notification at the given interval and calendar time.
     *
     * @param calendar The calendar object representing the time to schedule the notification.
     * @param interval The interval at which the notification should repeat.
     */
    fun scheduleRepeatingNotification(calendar: Calendar, interval: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, NotificationBroadcastReceiver::class.java).let { intent ->
            val bundle = Bundle()
            bundle.putString("title", title)
            bundle.putString("message", msg)

            intent.apply {
                putExtras(bundle)
            }

            getBroadcast(context, 0, intent, FLAG_IMMUTABLE)
        }
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            interval,
            alarmIntent
        )
    }

    /**
     * Displays the notification.
     */
    fun showNotification() {
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }
}