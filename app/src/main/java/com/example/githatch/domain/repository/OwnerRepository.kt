package com.example.githatch.domain.repository

import com.example.githatch.data.model.owner.Owner
import com.example.githatch.data.model.repo.Repo

interface OwnerRepository {
    suspend fun getAuthorRepos(ownerName:String): List<Repo>?
    suspend fun loadMoreAuthorRepos(): List<Repo>?
    suspend fun getAuthor(ownerName: String): Owner
}