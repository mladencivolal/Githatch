package com.example.githatch.presentation.di.core

import com.example.githatch.presentation.di.detail.DetailSubComponent
import com.example.githatch.presentation.di.owner.OwnerSubComponent
import com.example.githatch.presentation.di.repo.RepoSubComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        NetModule::class,
        UseCaseModule::class,
        RepositoryModule::class]
)
interface AppComponent {
    fun repoSubComponent(): RepoSubComponent.Factory
    fun detailSubComponent(): DetailSubComponent.Factory
    fun ownerSubComponent(): OwnerSubComponent.Factory
}