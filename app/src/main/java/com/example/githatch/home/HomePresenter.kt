package com.example.githatch.home

interface HomePresenter {

    fun searchRepositories (searchPhrase: String, sortBy: String, orderBy: String);

    fun onSortDialogCancel()

    fun onFilterApply()

    fun onFilterClear()

    fun onLoadMore()
}