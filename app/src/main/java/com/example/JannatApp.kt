package com.example

import android.app.Application
import com.example.data.local.AppDatabase
import com.example.data.repository.AppRepository

class JannatApp : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { AppRepository(database.appDao(), this) }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: JannatApp
            private set
    }
}
