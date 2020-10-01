package com.example.githatch.presentation.di.detail

import com.example.githatch.presentation.detail.DetailActivity
import dagger.Subcomponent

@DetailScope
@Subcomponent(modules = [DetailModule::class])
interface DetailSubComponent {
    fun inject(detailActivity: DetailActivity)

    @Subcomponent.Factory
    interface Factory {
        fun create():DetailSubComponent
    }
}