package com.example.reddittop.di

import com.example.reddittop.view.MainActivity
import dagger.Component

@Component(modules = [AppModule::class, NetworkModule::class, UseCaseModule::class])
interface AppComponent {

    fun inject(mainActivity: MainActivity)
}