package com.example.githatch.presentation.owner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githatch.domain.usecase.GetAuthorUseCase
import com.example.githatch.domain.usecase.GetReposFromAuthorUseCase
import com.example.githatch.domain.usecase.LoadMoreReposFromAuthorUseCase

@Suppress("UNCHECKED_CAST")
class OwnerViewModelFactory(
    private val getReposFromAuthorUseCase: GetReposFromAuthorUseCase,
    private val loadMoreReposFromAuthorUseCase: LoadMoreReposFromAuthorUseCase,
    private val getAuthorUseCase: GetAuthorUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return OwnerViewModel(getReposFromAuthorUseCase, loadMoreReposFromAuthorUseCase, getAuthorUseCase) as T
    }
}