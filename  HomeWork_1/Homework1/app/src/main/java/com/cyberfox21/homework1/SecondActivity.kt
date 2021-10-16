package com.cyberfox21.homework1

import android.content.*
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class SecondActivity : AppCompatActivity() {

    private var state = false
    private var contactsService: ContactService? = null

    private var connection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as ContactService.LocalContactsBinder
            contactsService = binder.service
            state = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            state = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        // register receiver to retrieve result from service
        registerReceiver()
    }

    override fun onStart() {
        super.onStart()
        this.bindService(ContactService.newIntent(this), connection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        if (state) unbindService(connection)
        state = false
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val error = intent?.getBooleanExtra(PERMISSION_ERROR, false)
            val result = Intent().apply {
                putStringArrayListExtra(
                    CONTACT_NAME,
                    intent?.getStringArrayListExtra(CONTACT_NAME)
                )
            }
            if (error != null && !error) {
                setResult(RESULT_OK, result)
            } else setResult(RESULT_CANCELED, result)
            finish()
        }

    }

    private fun registerReceiver() = LocalBroadcastManager.getInstance(this)
        .registerReceiver(
            broadcastReceiver,
            IntentFilter(FILTER_INTENT)
        )

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
    }

    companion object {

        const val CONTACT_NAME = "contact_name"
        const val FILTER_INTENT = "contact_filter"
        const val PERMISSION_ERROR = "permission_error"

        fun newIntent(context: Context): Intent {
            return Intent(context, SecondActivity::class.java)
        }
    }
}
