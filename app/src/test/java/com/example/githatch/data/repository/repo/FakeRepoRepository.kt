package com.example.githatch.data.repository.repo

import com.example.githatch.data.model.owner.Owner
import com.example.githatch.data.model.repo.Repo
import com.example.githatch.domain.repository.RepoRepository

class FakeRepoRepository : RepoRepository {
    private val repos = mutableListOf<Repo>()
    private val owner = Owner(
        "owner1",
        0,
        "avatarUrl",
        "url",
        "htmlUrl",
        "user",
        false,
        1,
        "Croatia",
        "bio",
        "twitter",
        "name",
        "email",
        1,
        2,
        3,
        4
    )

    init {
        repos.add(
            Repo(
                "repo1",
                "repository one",
                owner,
                "url",
                "desc",
                1,
                2,
                3,
                "createdAt",
                "updatedAt",
                "hr"
            )
        )
        repos.add(
            Repo(
                "repo2",
                "repository two",
                owner,
                "url",
                "desc",
                1,
                2,
                3,
                "createdAt",
                "updatedAt",
                "hr"
            )
        )
        repos.add(
            Repo(
                "repo3",
                "repository three",
                owner,
                "url",
                "desc",
                1,
                2,
                3,
                "createdAt",
                "updatedAt",
                "hr"
            )
        )
    }

    override suspend fun getRepos(
        searchPhrase: String,
        sortBy: String,
        orderBy: String
    ): List<Repo>? {
        return repos
    }

    override suspend fun loadMoreRepos(): List<Repo>? {
        repos.add(
            Repo(
                "repo4",
                "repository four",
                owner,
                "url",
                "desc",
                1,
                2,
                3,
                "createdAt",
                "updatedAt",
                "hr"
            )
        )
        repos.add(
            Repo(
                "repo5",
                "repository five",
                owner,
                "url",
                "desc",
                1,
                2,
                3,
                "createdAt",
                "updatedAt",
                "hr"
            )
        )
        repos.add(
            Repo(
                "repo6",
                "repository six",
                owner,
                "url",
                "desc",
                1,
                2,
                3,
                "createdAt",
                "updatedAt",
                "hr"
            )
        )
        return repos
    }
}