package com.example.githatch.data.repository.detail.dataSourceImpl

import android.util.Log
import com.example.githatch.data.model.owner.Owner
import com.example.githatch.data.repository.detail.dataSource.DetailRemoteDataSource
import com.example.githatch.domain.repository.DetailRepository

class DetailRepositoryImpl(private val detailRemoteDataSource: DetailRemoteDataSource) :
    DetailRepository {
    private var contributors: List<Owner> = emptyList()

    override suspend fun getContributors(ownerName: String, repoName: String): List<Owner>? =
        getContributorsFromAPI(ownerName, repoName)

    private suspend fun getContributorsFromAPI(ownerName: String, repoName: String): List<Owner> {
        try {
            val response = detailRemoteDataSource.getContribs(ownerName, repoName)
            val body = response.body()
            if (body != null) contributors = body
        } catch (exception: Exception) {
            Log.i("MYTAG", exception.message.toString())
        }
        return contributors
    }
}