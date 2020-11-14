package com.example.githatch.domain.usecase

import com.example.githatch.data.model.owner.Owner
import com.example.githatch.domain.repository.DetailRepository

class GetContribsUseCase(private val detailRepository: DetailRepository) {
    suspend fun execute(ownerName:String, repoName:String): List<Owner>? {
        return detailRepository.getContributors(ownerName, repoName)
    }
}