package com.example.githatch.data.repository.detail

import com.example.githatch.data.model.owner.Owner
import com.example.githatch.domain.repository.DetailRepository

class FakeDetailRepository: DetailRepository {
    private val contributors = mutableListOf<Owner>()
    init {
       contributors.add(Owner(
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
       ))
        contributors.add(Owner(
           "owner2",
           1,
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
       ))
        contributors.add(Owner(
           "owner3",
           2,
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
       ))
    }

    override suspend fun getContributors(ownerName: String, repoName: String): List<Owner>? {
        return contributors
    }
}