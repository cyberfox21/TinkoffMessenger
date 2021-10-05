package com.cyberfox21.homework1

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class SecondActivity : AppCompatActivity() {

    private val localBCManager = LocalBroadcastManager.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // register receiver to retrieve result from service
        registerReceiver()
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onPause() {
        super.onPause()

    }


    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val extra = Intent().apply {
                putStringArrayListExtra(CONTACT_NAME, intent?.getStringArrayListExtra(CONTACT_NAME))
            }
            setResult(RESULT_OK, extra)
        }

    }

    private fun registerReceiver() = localBCManager.registerReceiver(
        broadcastReceiver,
        IntentFilter()
    )

    override fun onDestroy() {
        super.onDestroy()
        localBCManager.unregisterReceiver(broadcastReceiver)
    }

    companion object {

        const val CONTACT_NAME = "contact_name"

        fun newIntent(context: Context): Intent {
            return Intent(context, SecondActivity::class.java)
        }
    }
}