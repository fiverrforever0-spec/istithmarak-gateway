package com.istithmarak.gateway

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class MqttControlService : Service() {

    companion object {
        private const val TAG = "MqttControlService"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "MQTT Control Service Started and listening for commands.")
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "MQTT Control Service Destroyed.")
    }
}

