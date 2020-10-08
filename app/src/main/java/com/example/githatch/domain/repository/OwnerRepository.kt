package com.example.githatch.domain.repository

import com.example.githatch.data.model.repo.Repo

interface OwnerRepository {
    suspend fun getReposFromAuthor(ownerName:String): List<Repo>?
    suspend fun loadMoreReposFromAuthor(): List<Repo>?
}