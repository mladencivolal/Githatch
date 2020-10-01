package com.example.githatch.domain.repository

import com.example.githatch.data.model.repo.Repo

interface RepoRepository {
    suspend fun getRepos(searchPhrase:String, sortBy: String, orderBy: String): List<Repo>?
    suspend fun loadMoreRepos(): List<Repo>?
}