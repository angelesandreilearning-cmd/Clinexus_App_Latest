package com.example.clinexusapp

import android.app.Application
import com.example.clinexusapp.util.SessionManager

class ClinexusApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SessionManager.init(this)
    }
}
