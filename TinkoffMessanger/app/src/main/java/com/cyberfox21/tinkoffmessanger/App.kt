package com.cyberfox21.tinkoffmessanger

import android.app.Application
import com.cyberfox21.tinkoffmessanger.di.DaggerApplicationComponent

class App : Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }

}