package com.example.githatch.presentation

import android.app.Application
import com.example.githatch.BuildConfig
import com.example.githatch.presentation.di.Injector
import com.example.githatch.presentation.di.core.*
import com.example.githatch.presentation.di.detail.DetailSubComponent
import com.example.githatch.presentation.di.owner.OwnerSubComponent
import com.example.githatch.presentation.di.repo.RepoSubComponent

class App : Application(), Injector {
        private lateinit var appComponent: AppComponent

        override fun onCreate() {
            super.onCreate()
            @Suppress("DEPRECATION")
            appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(applicationContext))
                .netModule(NetModule(BuildConfig.BASE_URL))
                .build()
        }

    override fun createRepoSubComponent(): RepoSubComponent = appComponent.repoSubComponent().create()

    override fun createDetailSubComponent(): DetailSubComponent = appComponent.detailSubComponent().create()

    override fun createOwnerSubComponent(): OwnerSubComponent = appComponent.ownerSubComponent().create()
}