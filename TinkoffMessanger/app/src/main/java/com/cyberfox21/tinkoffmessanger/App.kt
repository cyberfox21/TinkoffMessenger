package com.cyberfox21.tinkoffmessanger

import android.app.Application
import android.content.Context

class App: Application() {

    var context: Context? = null

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    fun context() : Context {
        return applicationContext
    }

}