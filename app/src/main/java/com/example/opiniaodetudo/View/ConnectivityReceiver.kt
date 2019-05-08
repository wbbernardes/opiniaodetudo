package com.example.opiniaodetudo.View

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class ConnectivityReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("RECEIVER", intent?.action)
    }
}