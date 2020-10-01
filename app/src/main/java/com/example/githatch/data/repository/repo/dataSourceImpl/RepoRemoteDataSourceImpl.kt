package com.example.githatch.data.repository.repo.dataSourceImpl

import android.util.Log
import com.example.githatch.data.api.GitHubService
import com.example.githatch.data.model.repo.RepoList
import com.example.githatch.data.repository.repo.dataSource.RepoRemoteDataSource
import retrofit2.Response

class RepoRemoteDataSourceImpl(
    private val gitHubService: GitHubService
) :
    RepoRemoteDataSource {

    override suspend fun getRepos(
        searchPhrase: String,
        sort: String,
        order: String,
        pageLength: Int,
        pageNum: Int
    ): Response<RepoList> {
        Log.i("MYTAG", "RepoRemoteDataSource pageNum: ${pageNum}")
        return gitHubService.getRepos(searchPhrase, sort, order, pageLength, pageNum)
    }
}