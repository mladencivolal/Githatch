package com.example.githatch.domain.repository

import com.example.githatch.data.model.owner.Owner

interface DetailRepository {
    suspend fun getContributors(ownerName: String, repoName: String): List<Owner>?
}