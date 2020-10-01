package com.example.githatch.data.repository.owner.dataSourceImpl

import android.annotation.SuppressLint
import android.util.Log
import com.example.githatch.data.model.repo.Repo
import com.example.githatch.data.repository.owner.dataSource.OwnerRemoteDataSource
import com.example.githatch.domain.repository.OwnerRepository

class OwnerRepositoryImpl(private val ownerRemoteDataSource: OwnerRemoteDataSource) :
    OwnerRepository {
    private var reposFromAuthor: List<Repo> = emptyList()
    private var pageNum: Int = 1
    private var pageLength: Int = 50
    private lateinit var ownerName: String

    @SuppressLint("LogNotTimber")
    override suspend fun getReposFromAuthor(ownerName: String): List<Repo>? {
        resetData()
        this.ownerName = ownerName
        try {
            val response =
                ownerRemoteDataSource.getReposFromAuthor(ownerName, pageLength, pageNum)
            val body = response.body()

            if (body != null) {
                reposFromAuthor = body
            }
        } catch (exception: Exception) {
            Log.i("MYTAG", exception.message.toString())
        }
        return reposFromAuthor
    }

    @SuppressLint("LogNotTimber")
    override suspend fun loadMoreRepos(): List<Repo>? {
        try {
            this.pageNum++
            val response =
                ownerRemoteDataSource.getReposFromAuthor(ownerName, pageLength, pageNum)
            val body = response.body()

            if (body != null) {
                reposFromAuthor = body
            }
        } catch (exception: java.lang.Exception) {
            Log.i("MYTAG", exception.message.toString())
        }
        return reposFromAuthor
    }

    private fun resetData() {
        pageLength = 50
        pageNum = 1
    }
}