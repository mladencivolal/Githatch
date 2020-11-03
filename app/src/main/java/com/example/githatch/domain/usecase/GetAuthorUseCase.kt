package com.example.githatch.domain.usecase

import com.example.githatch.data.model.owner.Owner
import com.example.githatch.data.model.repo.Repo
import com.example.githatch.domain.repository.OwnerRepository

class GetAuthorUseCase(private val ownerRepository: OwnerRepository) {
    suspend fun execute(ownerName:String): Owner {
        return ownerRepository.getAuthor(ownerName)
    }
}