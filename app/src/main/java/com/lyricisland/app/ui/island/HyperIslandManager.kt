package com.lyricisland.app.ui.island

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import io.github.d4viddf.hyperisland_kit.HyperIslandNotification
import com.lyricisland.app.R

object HyperIslandManager {

    private const val CHANNEL_ID = "hyper_island_channel"
    private const val NOTIFICATION_ID = 1001

    fun showTestNotification(context: Context, useHyperIsland: Boolean) {
        // 0. Check for Support if HyperIsland is requested
        var canUseHyperIsland = useHyperIsland
        if (canUseHyperIsland && !HyperIslandNotification.isSupported(context)) {
            Log.e("HyperIsland", "Device not supported or permission missing")
            canUseHyperIsland = false
        }
        
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create Channel (Required for Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Island Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        // 1. Build Standard Notification Base
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setContentTitle("Lyric Island")
            .setContentText(if (canUseHyperIsland) "Hyper Island Mode" else "Live Update Mode")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setOnlyAlertOnce(true) // Crucial for Live Update: prevent noise on updates
            .setOngoing(true) // Treat as active session

        // 2. Attach Hyper Island Extras ONLY if enabled and supported
        if (canUseHyperIsland) {
            // Hyper Island Builder
            val hyperBuilder = HyperIslandNotification.Builder(context, CHANNEL_ID, "LyricIsland")
                .setSmallWindowTarget("com.lyricisland.app.MainActivity")
                .setBaseInfo(
                    title = "Lyric Island",
                    content = "Playing...",
                    pictureKey = "icon_key"
                )
                .setSmallIslandIcon("icon_key")

            // Add Extras
            notificationBuilder.addExtras(hyperBuilder.buildResourceBundle())
            
            val notification = notificationBuilder.build()
            // Attach Payload (for Xiaomi HyperOS)
            notification.extras.putString("miui.focus.param", hyperBuilder.buildJsonParam())
            
            notificationManager.notify(NOTIFICATION_ID, notification)
        } else {
            // Standard Android Notification (Live Update Style)
            // No extras from HyperIslandKit to avoid conflict
            val notification = notificationBuilder.build()
            notificationManager.notify(NOTIFICATION_ID, notification)
        }
    }
}
