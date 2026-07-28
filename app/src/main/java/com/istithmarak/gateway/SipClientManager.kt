package com.istithmarak.gateway

import android.util.Log

object SipClientManager {
    private const val TAG = "SipClientManager"
    private var isRegistered = false

    fun initializeSipStack(serverIp: String, extension: String, secret: String) {
        Log.d(TAG, "Initializing SIP Stack for extension: $extension on server: $serverIp")
        // تهيئة بروتوكول PJSIP والربط مع السنترال المركزي
        isRegistered = true
    }

    fun terminateSipSession() {
        Log.d(TAG, "Terminating active SIP session.")
        isRegistered = false
    }
}

