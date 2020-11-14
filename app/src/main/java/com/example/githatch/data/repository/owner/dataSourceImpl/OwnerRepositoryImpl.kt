package com.example.githatch.data.repository.owner.dataSourceImpl

import android.util.Log
import com.example.githatch.data.model.owner.Owner
import com.example.githatch.data.model.repo.Repo
import com.example.githatch.data.repository.owner.dataSource.OwnerRemoteDataSource
import com.example.githatch.domain.repository.OwnerRepository

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
            Log.i("MYTAG", exception.message.toString())
        }
        return author
    }

    override suspend fun getAuthorRepos(ownerName: String): List<Repo>? {
        resetData()
        this.ownerName = ownerName
        return getAuthorReposFromAPI()
    }

    private suspend fun getAuthorReposFromAPI(): List<Repo> {
        try {
            val response = ownerRemoteDataSource.getReposFromAuthor(ownerName, pageNum)
            val body = response.body()
            if (body != null) reposFromAuthor = body
        } catch (exception: Exception) {
            Log.i("MYTAG", exception.message.toString())
        }
        return reposFromAuthor
    }

    override suspend fun loadMoreAuthorRepos(): List<Repo>? {
        this.pageNum++
        return getAuthorReposFromAPI()
    }

    private fun resetData() {
        pageNum = 1
    }
}