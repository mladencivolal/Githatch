package com.example.githatch.presentation.owner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.githatch.domain.usecase.GetReposFromAuthorUseCase
import com.example.githatch.domain.usecase.LoadMoreReposFromAuthor

class OwnerViewModel(private val getReposFromAuthorUseCase: GetReposFromAuthorUseCase, private val loadMoreReposFromAuthor: LoadMoreReposFromAuthor):ViewModel() {
    fun getReposFromAuthor(ownerName:String) = liveData {
        val reposFromAuthor = getReposFromAuthorUseCase.execute(ownerName)
        emit(reposFromAuthor)
    }

    fun loadMoreReposFromAuthor() = liveData {
        val reposList = loadMoreReposFromAuthor.execute(/*searchTerm, sortBy, orderBy*/)
        emit(reposList)
    }
}