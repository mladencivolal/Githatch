package com.example.githatch.data.repository.detail.dataSource

import com.example.githatch.data.model.owner.Owner
import retrofit2.Response

interface DetailRemoteDataSource {
    suspend fun getContribs(ownerName: String, repoName: String): Response<List<Owner>>
}