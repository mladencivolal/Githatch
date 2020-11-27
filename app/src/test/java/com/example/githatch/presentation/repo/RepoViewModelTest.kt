package com.example.githatch.presentation.repo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.githatch.data.model.owner.Owner
import com.example.githatch.data.model.repo.Repo
import com.example.githatch.data.repository.repo.FakeRepoRepository
import com.example.githatch.domain.usecase.GetReposUseCase
import com.example.githatch.domain.usecase.LoadMoreReposUseCase
import com.example.githatch.getOrAwaitValue
import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RepoViewModelTest {
    private lateinit var viewModel: RepoViewModel
    private var repos = mutableListOf<Repo>()
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
        val fakeRepoRepository = FakeRepoRepository()
        val getReposUseCase = GetReposUseCase(fakeRepoRepository)
        val loadMoreReposUseCase = LoadMoreReposUseCase(fakeRepoRepository)

        viewModel = RepoViewModel(getReposUseCase, loadMoreReposUseCase)

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

    @Test
    fun getRepos() {
        val currentRepoList = viewModel.getRepos("question", "stars", "asc").getOrAwaitValue()
        Truth.assertThat(currentRepoList).isEqualTo(repos)
    }

    @Test
    fun loadMoreRepos() {
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
        val currentRepoList = viewModel.loadMoreRepos().getOrAwaitValue()
        Truth.assertThat(currentRepoList).isEqualTo(repos)
    }
}