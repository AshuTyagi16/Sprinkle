package com.jarvis.sprinkle.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.jarvis.sprinkle.util.NetworkUtil

abstract class NetworkStateReceiver(private val networkUtil: NetworkUtil) :
    BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (networkUtil.isNetworkAvailable())
            onConnectedToInternet()
    }

    abstract fun onConnectedToInternet()

    companion object {
        fun getIntentFilter() = IntentFilter().apply {
            addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        }
    }
}