package com.example.githatch.presentation.repo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.githatch.domain.usecase.GetReposUseCase
import com.example.githatch.domain.usecase.LoadMoreReposUseCase

class RepoViewModel(
    private val getReposUseCase: GetReposUseCase,
    private val loadMoreReposUseCase: LoadMoreReposUseCase) : ViewModel() {
    private var searchTerm = ""

    fun getRepos(searchPhrase: String, sortBy: String, orderBy: String) = liveData {
        searchTerm = searchPhrase
        val reposList = getReposUseCase.execute(searchPhrase, sortBy, orderBy)
        emit(reposList)
    }

    fun loadMoreRepos(/*sortBy: String, orderBy: String*/) = liveData {
        val reposList = loadMoreReposUseCase.execute()
        emit(reposList)
    }
}

