package com.anant.myapplication.application

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // init Realm database
        Realm.init(this)
        val configBuilder = RealmConfiguration.Builder()
            .name("MyApplication.realm")
            .schemaVersion(1)
            .build()

        Realm.setDefaultConfiguration(configBuilder)
    }
}
