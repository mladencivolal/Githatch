package com.example.githatch.presentation.owner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githatch.domain.usecase.GetReposFromAuthorUseCase
import com.example.githatch.domain.usecase.LoadMoreReposFromAuthor

@Suppress("UNCHECKED_CAST")
class OwnerViewModelFactory(private val getReposFromAuthorUseCase: GetReposFromAuthorUseCase, private val loadMoreReposFromAuthor: LoadMoreReposFromAuthor):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return OwnerViewModel(getReposFromAuthorUseCase, loadMoreReposFromAuthor) as T
    }
}