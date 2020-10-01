package com.example.githatch.domain.usecase

import android.util.Log
import com.example.githatch.data.model.repo.Repo
import com.example.githatch.domain.repository.RepoRepository

class GetReposUseCase(private val repoRepository: RepoRepository) {
    suspend fun execute(searchPhrase: String, sortBy: String, orderBy: String): List<Repo>? {
        return repoRepository.getRepos(searchPhrase, sortBy, orderBy)
    }
}