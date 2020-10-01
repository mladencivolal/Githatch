package com.example.githatch.presentation.di.core

import com.example.githatch.data.repository.detail.dataSource.DetailRemoteDataSource
import com.example.githatch.data.repository.detail.dataSourceImpl.DetailRepositoryImpl
import com.example.githatch.data.repository.owner.dataSource.OwnerRemoteDataSource
import com.example.githatch.data.repository.owner.dataSourceImpl.OwnerRepositoryImpl
import com.example.githatch.data.repository.repo.dataSource.RepoRemoteDataSource
import com.example.githatch.data.repository.repo.dataSourceImpl.RepoRepositoryImpl
import com.example.githatch.domain.repository.DetailRepository
import com.example.githatch.domain.repository.OwnerRepository
import com.example.githatch.domain.repository.RepoRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Singleton
    @Provides
    fun provideRepoRepository(
        repoRemoteDataSource: RepoRemoteDataSource
    ): RepoRepository {
        return RepoRepositoryImpl(
            repoRemoteDataSource
        )
    }

    @Singleton
    @Provides
    fun provideDetailRepository(
        detailRemoteDataSource: DetailRemoteDataSource
    ): DetailRepository {
        return DetailRepositoryImpl(
            detailRemoteDataSource
        )
    }

    @Singleton
    @Provides
    fun provideOwnerRepository(
        ownerRemoteDataSource: OwnerRemoteDataSource
    ): OwnerRepository {
        return OwnerRepositoryImpl(
            ownerRemoteDataSource
        )
    }
}