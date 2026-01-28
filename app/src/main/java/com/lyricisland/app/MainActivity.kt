package com.lyricisland.app

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import com.lyricisland.app.ui.island.HyperIslandManager

class MainActivity : AppCompatActivity() {

    private lateinit var statusText: TextView
    private lateinit var permissionButton: Button
    private lateinit var modeSwitch: SwitchMaterial
    private lateinit var testIslandButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.status_text)
        permissionButton = findViewById(R.id.permission_button)
        modeSwitch = findViewById(R.id.mode_switch)
        testIslandButton = findViewById(R.id.test_island_button)

        setupSwitch()

        permissionButton.setOnClickListener {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }

        testIslandButton.setOnClickListener {
            val useHyperIsland = modeSwitch.isChecked
            HyperIslandManager.showTestNotification(this, useHyperIsland)
        }
    }

    private fun setupSwitch() {
        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val useHyperIsland = prefs.getBoolean("use_hyper_island", true)
        modeSwitch.isChecked = useHyperIsland

        modeSwitch.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("use_hyper_island", isChecked).apply()
        }
    }

    override fun onResume() {
        super.onResume()
        checkPermission()
    }

    private fun checkPermission() {
        val cn = componentName
        val flat = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        val granted = flat != null && flat.contains(cn.flattenToString())

        if (granted) {
            statusText.text = "Notification Permission: GRANTED"
            permissionButton.isEnabled = false
        } else {
            statusText.text = "Notification Permission: REQUIRED"
            permissionButton.isEnabled = true
        }
    }
}
