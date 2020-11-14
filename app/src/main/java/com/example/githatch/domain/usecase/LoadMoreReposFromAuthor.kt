package com.example.githatch.domain.usecase

import com.example.githatch.data.model.repo.Repo
import com.example.githatch.domain.repository.OwnerRepository

class LoadMoreReposFromAuthor(private val ownerRepository: OwnerRepository) {
    suspend fun execute(): List<Repo>? =
        ownerRepository.loadMoreAuthorRepos()
}