package com.example.githatch.data.repository.owner.dataSource

import com.example.githatch.data.model.repo.Repo
import retrofit2.Response

interface OwnerRemoteDataSource {
    suspend fun getReposFromAuthor(ownerName: String, pageLength:Int, pageNum:Int): Response<List<Repo>>
}