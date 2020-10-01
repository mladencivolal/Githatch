package com.example.githatch.data.repository.repo.dataSourceImpl

import android.annotation.SuppressLint
import com.example.githatch.data.api.GitHubService
import com.example.githatch.data.model.repo.RepoList
import com.example.githatch.data.repository.repo.dataSource.RepoRemoteDataSource
import retrofit2.Response

class RepoRemoteDataSourceImpl(
    private val gitHubService: GitHubService
) :
    RepoRemoteDataSource {

    @SuppressLint("LogNotTimber")
    override suspend fun getRepos(
        searchPhrase: String,
        sort: String,
        order: String,
        pageLength: Int,
        pageNum: Int
    ): Response<RepoList> {
        return gitHubService.getRepos(searchPhrase, sort, order, pageLength, pageNum)
    }
}