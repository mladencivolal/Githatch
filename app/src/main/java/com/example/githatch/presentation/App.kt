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
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(applicationContext))
            .netModule(NetModule(BuildConfig.BASE_URL))
            .remoteDataModule(RemoteDataModule())
            .build()
    }

    override fun createRepoSubComponent(): RepoSubComponent {
        return appComponent.repoSubComponent().create()
    }

    override fun createDetailSubComponent(): DetailSubComponent {
        return appComponent.detailSubComponent().create()
    }

    override fun createOwnerSubComponent(): OwnerSubComponent {
        return appComponent.ownerSubComponent().create()
    }
}