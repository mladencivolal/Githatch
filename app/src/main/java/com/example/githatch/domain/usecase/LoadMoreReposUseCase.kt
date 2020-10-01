package com.example.githatch.domain.usecase

import com.example.githatch.data.model.repo.Repo
import com.example.githatch.domain.repository.RepoRepository

class LoadMoreReposUseCase(private val repoRepository: RepoRepository) {
    suspend fun execute(): List<Repo>? =
        repoRepository.loadMoreRepos()
}