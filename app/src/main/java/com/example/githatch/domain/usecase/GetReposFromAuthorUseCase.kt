package com.example.githatch.domain.usecase

import com.example.githatch.data.model.repo.Repo
import com.example.githatch.domain.repository.OwnerRepository

class GetReposFromAuthorUseCase(private val ownerRepository: OwnerRepository) {
    suspend fun execute(ownerName:String): List<Repo>? {
        return ownerRepository.getAuthorRepos(ownerName)
    }
}