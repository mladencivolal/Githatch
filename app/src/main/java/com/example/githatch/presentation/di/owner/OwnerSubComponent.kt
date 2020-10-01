package com.example.githatch.presentation.di.owner

import com.example.githatch.presentation.di.detail.DetailScope
import com.example.githatch.presentation.owner.OwnerActivity
import dagger.Subcomponent

@DetailScope
@Subcomponent(modules = [OwnerModule::class])
interface OwnerSubComponent {
    fun inject(ownerActivity: OwnerActivity)

    @Subcomponent.Factory
    interface Factory {
        fun create(): OwnerSubComponent
    }
}