package com.example.githatch.data.repository.owner

import com.example.githatch.data.model.owner.Owner
import com.example.githatch.data.model.repo.Repo
import com.example.githatch.domain.repository.OwnerRepository

class FakeOwnerRepository : OwnerRepository {
    private val authorRepos = mutableListOf<Repo>()
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
        authorRepos.add(
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
        authorRepos.add(
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
        authorRepos.add(
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

    override suspend fun getAuthorRepos(ownerName: String): List<Repo>? {
        return authorRepos.filter { owner.login == ownerName }
    }

    override suspend fun loadMoreAuthorRepos(): List<Repo>? {
        authorRepos.add(
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
        authorRepos.add(
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
        authorRepos.add(
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
        return authorRepos
    }

    override suspend fun getAuthor(ownerName: String): Owner {
        return owner
    }

}
