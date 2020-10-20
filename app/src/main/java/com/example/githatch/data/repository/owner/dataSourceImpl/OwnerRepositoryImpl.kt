package com.example.githatch.data.repository.owner.dataSourceImpl

import android.annotation.SuppressLint
import android.util.Log
import com.example.githatch.data.model.owner.Owner
import com.example.githatch.data.model.repo.Repo
import com.example.githatch.data.repository.owner.dataSource.OwnerRemoteDataSource
import com.example.githatch.domain.repository.OwnerRepository
import timber.log.Timber

class OwnerRepositoryImpl(private val ownerRemoteDataSource: OwnerRemoteDataSource) :
    OwnerRepository {
    private var reposFromAuthor: List<Repo> = emptyList()
    private lateinit var author: Owner
    private var pageNum: Int = 1
    private lateinit var ownerName: String

    override suspend fun getAuthor(ownerName: String): Owner {
        try {
            val response = ownerRemoteDataSource.getAuthor(ownerName)
            val body = response.body()
            if (body != null) author = body
        } catch (exception: Exception) {
            Timber.i(exception.message.toString())
        }
        return author
    }

    @SuppressLint("LogNotTimber")
    override suspend fun getReposFromAuthor(ownerName: String): List<Repo>? {
        resetData()
        this.ownerName = ownerName

        try {
            val response = ownerRemoteDataSource.getReposFromAuthor(ownerName, pageNum)
            val body = response.body()
            if (body != null) reposFromAuthor = body
        } catch (exception: Exception) {
            Log.i("MYTAG", exception.message.toString())
        }
        return reposFromAuthor
    }

    @SuppressLint("LogNotTimber")
    override suspend fun loadMoreReposFromAuthor(): List<Repo>? {
        try {
            this.pageNum++
            val response = ownerRemoteDataSource.getReposFromAuthor(ownerName, pageNum)
            val body = response.body()
            if (body != null) reposFromAuthor = body
        } catch (exception: java.lang.Exception) {
            Log.i("MYTAG", exception.message.toString())
        }
        return reposFromAuthor
    }

    private fun resetData() {
        pageNum = 1
    }
}