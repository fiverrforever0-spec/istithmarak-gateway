package com.istithmarak.gateway

import android.telecom.Call
import android.telecom.InCallService
import android.util.Log

class GsmInCallService : InCallService() {

    companion object {
        var activeCall: Call? = null
        private const val TAG = "IstithmarakInCall"
    }

    override fun onCallAdded(call: Call) {
        super.onCallAdded(call)
        activeCall = call
        Log.d(TAG, "Call Added Successfully. State: ${call.state}")
        
        call.registerCallback(object : Call.Callback() {
            override fun onStateChanged(call: Call, newState: Int) {
                super.onStateChanged(call, newState)
                Log.d(TAG, "Call State Updated: $newState")
                
                if (newState == Call.STATE_ACTIVE) {
                    AudioBridgeEngine.startBridge()
                } else if (newState == Call.STATE_DISCONNECTED) {
                    AudioBridgeEngine.stopBridge()
                    activeCall = null
                }
            }
        })
    }

    override fun onCallRemoved(call: Call) {
        super.onCallRemoved(call)
        if (activeCall == call) {
            activeCall = null
            AudioBridgeEngine.stopBridge()
        }
        Log.d(TAG, "Call Removed and Terminated")
    }
}

