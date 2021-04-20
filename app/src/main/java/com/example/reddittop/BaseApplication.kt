package com.example.reddittop

import android.app.Application
import com.example.reddittop.di.AppComponent
import com.example.reddittop.di.AppModule
import com.example.reddittop.di.DaggerAppComponent

class BaseApplication : Application() {

    companion object{
        private lateinit var appComponent:AppComponent

        fun getAppComponent(): AppComponent {
            return appComponent
        }
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}