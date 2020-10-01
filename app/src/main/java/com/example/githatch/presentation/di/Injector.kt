package com.example.githatch.presentation.di

import com.example.githatch.presentation.di.detail.DetailSubComponent
import com.example.githatch.presentation.di.owner.OwnerSubComponent
import com.example.githatch.presentation.di.repo.RepoSubComponent

interface Injector {
    fun createRepoSubComponent(): RepoSubComponent
    fun createDetailSubComponent(): DetailSubComponent
    fun createOwnerSubComponent(): OwnerSubComponent
}