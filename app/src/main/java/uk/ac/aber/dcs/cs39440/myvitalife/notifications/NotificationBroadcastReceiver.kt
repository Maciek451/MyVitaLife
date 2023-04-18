package uk.ac.aber.dcs.cs39440.myvitalife.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Broadcast receiver that listens for incoming notifications and shows them when received.
 */
class NotificationBroadcastReceiver : BroadcastReceiver() {

    /**
     * Called when a new notification is received.
     *
     * @param context The context in which the broadcast is received.
     * @param intent The intent containing the notification information.
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) {
            return
        }

        val title = if (intent.hasExtra("title")) {
            intent.getStringExtra("title")
        } else {
            "Notification Title"
        }

        val message = if (intent.hasExtra("message")) {
            intent.getStringExtra("message")
        } else {
            "Notification message"
        }

        /**
         * Constructs a new notification and displays it.
         *
         * @param context The context in which the notification will be displayed.
         * @param title The title of the notification.
         * @param message The message body of the notification.
         */
        val myNotification = MyNotification(context, title!!, message!!)
        myNotification.showNotification()
    }
}