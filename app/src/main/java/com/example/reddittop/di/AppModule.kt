package com.example.reddittop.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class AppModule constructor(private val mApplication: Application) {

    @Provides
    fun getApplicationContext() : Context {
        return mApplication.applicationContext
    }
}