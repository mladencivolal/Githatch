package com.example.githatch.data.repository.owner.dataSource

import com.example.githatch.data.model.owner.Owner
import com.example.githatch.data.model.repo.Repo
import retrofit2.Response

interface OwnerRemoteDataSource {
    suspend fun getReposFromAuthor(ownerName: String, pageNum:Int): Response<List<Repo>>
    suspend fun getAuthor(ownerName: String): Response<Owner>
}