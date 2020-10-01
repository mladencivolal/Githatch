package com.example.githatch.data.repository.repo.dataSource

import com.example.githatch.data.model.repo.RepoList
import retrofit2.Response

interface RepoRemoteDataSource {
    suspend fun getRepos(searchPhrase: String, sort:String, order: String, pageLength:Int, pageNum:Int): Response<RepoList>
}