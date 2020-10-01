package com.example.githatch.presentation.di.repo

import com.example.githatch.domain.usecase.GetReposUseCase
import com.example.githatch.domain.usecase.LoadMoreReposUseCase
import com.example.githatch.presentation.repo.RepoViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class RepoModule {
    @RepoScope
    @Provides
    fun provideMovieViewModelFactory(
        getReposUseCase: GetReposUseCase,
        loadMoreReposUseCase: LoadMoreReposUseCase): RepoViewModelFactory {
        return RepoViewModelFactory(getReposUseCase, loadMoreReposUseCase)
    }
}