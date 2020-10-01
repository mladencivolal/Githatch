package com.example.githatch.data.repository.detail.dataSourceImpl

import com.example.githatch.data.api.GitHubService
import com.example.githatch.data.model.owner.Owner
import com.example.githatch.data.repository.detail.dataSource.DetailRemoteDataSource
import retrofit2.Response

class DetailRemoteDataSourceImpl(
    private val gitHubService: GitHubService
) :
    DetailRemoteDataSource {
    override suspend fun getContribs(ownerName: String, repoName: String): Response<List<Owner>> {
        return gitHubService.getContribs(ownerName, repoName)
    }
}