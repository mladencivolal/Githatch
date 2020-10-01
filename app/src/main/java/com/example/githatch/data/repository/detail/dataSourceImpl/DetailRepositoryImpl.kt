package com.example.githatch.data.repository.detail.dataSourceImpl

import android.annotation.SuppressLint
import android.util.Log
import com.example.githatch.data.model.owner.Owner
import com.example.githatch.data.repository.detail.dataSource.DetailRemoteDataSource
import com.example.githatch.domain.repository.DetailRepository

class DetailRepositoryImpl(private val detailRemoteDataSource: DetailRemoteDataSource) :
    DetailRepository {
    private var contribsList: List<Owner> = emptyList()

    override suspend fun getContribs(ownerName: String, repoName: String): List<Owner>? {
        return getContribsFromAPI(ownerName, repoName)
    }

    @SuppressLint("LogNotTimber")
    suspend fun getContribsFromAPI(ownerName: String, repoName: String): List<Owner> {
        try {
            val response =
                detailRemoteDataSource.getContribs(ownerName, repoName)

            val body = response.body()
            if (body != null) {
                contribsList = body
            }

        } catch (exception: Exception) {
            Log.i("MYTAG", exception.message.toString())
        }

        return contribsList
    }
}