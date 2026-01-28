package com.lyricisland.app.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class MediaNotificationListenerService : NotificationListenerService() {

    companion object {
        private const val TAG = "MediaListener"
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.i(TAG, "Notification Listener Connected")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)
        // Basic filtering for media notifications could be added here
        // For Phase 1, we just log everything to confirm it works
        val extras = sbn.notification.extras
        val title = extras.getString("android.title")
        val text = extras.getString("android.text")
        
        Log.d(TAG, "Notification Posted: Pkg=${sbn.packageName} Title=$title Text=$text")
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        super.onNotificationRemoved(sbn)
        Log.d(TAG, "Notification Removed: ${sbn.packageName}")
    }
}
