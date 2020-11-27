package com.example.githatch.presentation.owner

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.githatch.data.model.owner.Owner
import com.example.githatch.data.model.repo.Repo
import com.example.githatch.data.repository.owner.FakeOwnerRepository
import com.example.githatch.data.repository.repo.FakeRepoRepository
import com.example.githatch.domain.usecase.*
import com.example.githatch.getOrAwaitValue
import com.example.githatch.presentation.repo.RepoViewModel
import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule

class OwnerViewModelTest {
    private lateinit var viewModel: OwnerViewModel
    private var ownerRepos = mutableListOf<Repo>()
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

    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        val fakeOwnerRepository = FakeOwnerRepository()
        val getReposFromAuthorUseCase = GetReposFromAuthorUseCase(fakeOwnerRepository)
        val loadMoreReposFromAuthorUseCase = LoadMoreReposFromAuthorUseCase(fakeOwnerRepository)
        val getAuthorUseCase = GetAuthorUseCase(fakeOwnerRepository)

        viewModel = OwnerViewModel(getReposFromAuthorUseCase, loadMoreReposFromAuthorUseCase, getAuthorUseCase)

        ownerRepos.add(
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
        ownerRepos.add(
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
        ownerRepos.add(
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

    @Test
    fun getReposFromAuthor() {
        val currentRepoList = viewModel.getReposFromAuthor("owner1").getOrAwaitValue()
        Truth.assertThat(currentRepoList).isEqualTo(ownerRepos)
    }

    @Test
    fun loadMoreReposFromAuthor() {
        ownerRepos.add(
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
        ownerRepos.add(
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
        ownerRepos.add(
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
        val currentRepoList = viewModel.loadMoreReposFromAuthor().getOrAwaitValue()
        Truth.assertThat(currentRepoList).isEqualTo(ownerRepos)
    }

    @Test
    fun getOwner() {
        val currentOwner = viewModel.getOwner("owner1").getOrAwaitValue()
        Truth.assertThat(currentOwner).isEqualTo(owner)
    }
}