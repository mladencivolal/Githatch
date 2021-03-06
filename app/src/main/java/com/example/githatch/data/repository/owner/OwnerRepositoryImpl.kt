package com.example.githatch.data.repository.owner

import android.util.Log
import com.example.githatch.data.api.GitHubService
import com.example.githatch.data.model.owner.Owner
import com.example.githatch.data.model.repo.Repo
import com.example.githatch.domain.repository.OwnerRepository

class OwnerRepositoryImpl(private val gitHubService: GitHubService) :
    OwnerRepository {
    private var authorRepos: List<Repo> = emptyList()
    private lateinit var author: Owner
    private var pageNum: Int = 1
    private lateinit var ownerName: String

    override suspend fun getAuthor(ownerName: String): Owner {
        try {
            val response = gitHubService.getAuthor(ownerName)
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
            val response = gitHubService.getAuthorRepositories(ownerName, pageNum)
            val body = response.body()
            if (body != null) authorRepos = body
        } catch (exception: Exception) {
            Log.i("MYTAG", exception.message.toString())
        }
        return authorRepos
    }

    override suspend fun loadMoreAuthorRepos(): List<Repo>? {
        this.pageNum++
        return getAuthorReposFromAPI()
    }

    private fun resetData() {
        pageNum = 1
    }
}