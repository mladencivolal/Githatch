package com.example.githatch.home

interface HomePresenter {

    suspend fun searchRepositories (searchPhrase: String, sortBy: String, orderBy: String);

    suspend fun onSortDialogCancel()

    fun onFilterApply()

    suspend fun onFilterClear()

    suspend fun onLoadMore()
}