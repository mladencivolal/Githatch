package com.example.githatch.data.repository.repo.dataSourceImpl

import android.util.Log
import com.example.githatch.data.model.repo.Repo
import com.example.githatch.data.repository.repo.dataSource.RepoRemoteDataSource
import com.example.githatch.domain.repository.RepoRepository

class RepoRepositoryImpl(
    private val repoRemoteDataSource: RepoRemoteDataSource
) : RepoRepository {

    private var searchPhrase: String = ""
    private var sortBy: String = ""
    private var orderBy: String = ""
    private var pageNum: Int = 1
    private var pageLength: Int = 50
    private var itemsCount: Int = 0
    private var totalCount: Int = 0

    private var repoList: List<Repo> = emptyList()

    private fun resetData() {
        pageLength = 50
        pageNum = 1
        totalCount = 0
        itemsCount = 0
        sortBy = ""
        orderBy = ""
    }

    override suspend fun getRepos(
        searchPhrase: String, sortBy: String, orderBy: String
    ): List<Repo>? {
        resetData()
        this.searchPhrase = searchPhrase
        return getReposFromAPI(searchPhrase, sortBy, orderBy)
    }

    override suspend fun loadMoreRepos(): List<Repo>? {
        if (itemsCount != 0 && totalCount != itemsCount) this.pageNum++
        Log.i("MYTAG", "Impl: loadMoreRepos: searchPhrase: $searchPhrase, sortBy: $sortBy, orderBy: $orderBy")

        return getReposFromAPI(searchPhrase, sortBy, orderBy)
    }

    private suspend fun getReposFromAPI(searchPhrase: String, sortBy: String, orderBy: String): List<Repo> {
        this.sortBy = sortBy
        this.orderBy = orderBy
        this.searchPhrase = searchPhrase
        Log.i("MYTAG", "Impl: getReposFromAPI: searchPhrase: $searchPhrase, sortBy: $sortBy, orderBy: $orderBy")

        try {
            val response =
                repoRemoteDataSource.getRepos(searchPhrase, sortBy, orderBy, pageLength, pageNum)
            val body = response.body()

            if (body != null) {
                repoList = body.repos
                totalCount = body.totalCount
                itemsCount += body.repos.size
            }

        } catch (exception: Exception) {
            Log.i("MYTAG", exception.message.toString())
        }
        return repoList
    }
}