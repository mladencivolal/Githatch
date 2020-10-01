package com.example.githatch.data.repository.owner.dataSourceImpl

import com.example.githatch.data.api.GitHubService
import com.example.githatch.data.model.repo.Repo
import com.example.githatch.data.repository.owner.dataSource.OwnerRemoteDataSource
import retrofit2.Response

class OwnerRemoteDataSourceImpl(private val gitHubService: GitHubService):OwnerRemoteDataSource {
    override suspend fun getReposFromAuthor(ownerName: String, pageLength:Int, pageNum:Int): Response<List<Repo>> {
        return gitHubService.getReposFromAuthor(ownerName, pageLength, pageNum)
    }
}