package com.example.githatch.presentation.di.repo

import com.example.githatch.presentation.repo.RepoActivity
import dagger.Subcomponent

@RepoScope
@Subcomponent(modules = [RepoModule::class])
interface RepoSubComponent {
    fun inject(repoActivity: RepoActivity)

    @Subcomponent.Factory
    interface Factory {
        fun create():RepoSubComponent
    }
}