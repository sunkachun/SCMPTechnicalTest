package com.example.scmptechnicaltest

import android.app.Application
import com.example.scmptechnicaltest.di.ServiceLocator

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ServiceLocator.provideAuthRepository()
        ServiceLocator.provideStaffRepository()
    }
}