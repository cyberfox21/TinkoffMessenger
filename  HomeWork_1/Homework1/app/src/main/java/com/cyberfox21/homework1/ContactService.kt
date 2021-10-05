package com.cyberfox21.homework1

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Binder
import android.os.IBinder
import android.provider.ContactsContract
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class ContactService : Service() {

    private val binder: IBinder = LocalContactsBinder()

    private fun checkPermissionGranted() =
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED


    private fun sendError() {
        val error = Intent(SecondActivity.FILTER_INTENT).apply {
            putExtra(SecondActivity.PERMISSION_ERROR, true)
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(error)
    }

    private fun getContacts() {
        val result = Intent(SecondActivity.FILTER_INTENT)

        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            null
        )


        var contactsList = ArrayList<String>()
        if (cursor != null) contactsList = retrieveContacts(cursor)

        result.putExtra(SecondActivity.CONTACT_NAME, contactsList)

        LocalBroadcastManager.getInstance(this).sendBroadcast(result)
    }

    private fun retrieveContacts(cursor: Cursor): ArrayList<String> {
        val contactsList = arrayListOf<String>()

        with(cursor) {
            while (moveToNext()) {
                val name = getString(
                    getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
                )
                contactsList += name
            }
        }

        return contactsList
    }

    inner class LocalContactsBinder : Binder() {
        val service: ContactService
            get() = this@ContactService
    }

    override fun onBind(intent: Intent?): IBinder {
        if (checkPermissionGranted()) getContacts()
        else sendError()
        return binder
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, ContactService::class.java)
        }
    }
}