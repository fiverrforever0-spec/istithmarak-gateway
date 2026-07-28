package com.istithmarak.gateway

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioRecord
import android.media.AudioTrack
import android.media.MediaRecorder
import android.util.Log

object AudioBridgeEngine {
    private const val SAMPLE_RATE = 8000
    private const val CHANNEL_CONFIG_IN = AudioFormat.CHANNEL_IN_MONO
    private const val CHANNEL_CONFIG_OUT = AudioFormat.CHANNEL_OUT_MONO
    private const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT
    private const val TAG = "AudioBridgeEngine"
    
    private var isBridging = false
    private var audioRecord: AudioRecord? = null
    private var audioTrack: AudioTrack? = null
    private var bridgeThread: Thread? = null

    fun startBridge() {
        if (isBridging) return
        isBridging = true

        val bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG_IN, AUDIO_FORMAT)
        if (bufferSize <= 0) return

        try {
            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.VOICE_COMMUNICATION,
                SAMPLE_RATE,
                CHANNEL_CONFIG_IN,
                AUDIO_FORMAT,
                bufferSize
            )

            audioTrack = AudioTrack(
                AudioManager.STREAM_VOICE_CALL,
                SAMPLE_RATE,
                CHANNEL_CONFIG_OUT,
                AUDIO_FORMAT,
                bufferSize,
                AudioTrack.MODE_STREAM
            )

            audioRecord?.startRecording()
            audioTrack?.play()

            bridgeThread = Thread {
                val buffer = ByteArray(bufferSize)
                while (isBridging) {
                    val readSize = audioRecord?.read(buffer, 0, buffer.size) ?: 0
                    if (readSize > 0) {
                        audioTrack?.write(buffer, 0, readSize)
                    }
                }
            }
            bridgeThread?.start()
            Log.d(TAG, "Audio Bridge Started Successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start Audio Bridge: ${e.message}")
            stopBridge()
        }
    }

    fun stopBridge() {
        isBridging = false
        try {
            bridgeThread?.interrupt()
            bridgeThread = null
            
            audioRecord?.stop()
            audioRecord?.release()
            audioRecord = null

            audioTrack?.stop()
            audioTrack?.release()
            audioTrack = null
            
            Log.d(TAG, "Audio Bridge Stopped Safely")
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping Audio Bridge: ${e.message}")
        }
    }
}

