package com.example.githatch.presentation.repo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githatch.domain.usecase.GetReposUseCase
import com.example.githatch.domain.usecase.LoadMoreReposUseCase

@Suppress("UNCHECKED_CAST")
class RepoViewModelFactory(
    private val getReposUseCase: GetReposUseCase,
    private val loadMoreReposUseCase: LoadMoreReposUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        RepoViewModel(getReposUseCase, loadMoreReposUseCase) as T
}