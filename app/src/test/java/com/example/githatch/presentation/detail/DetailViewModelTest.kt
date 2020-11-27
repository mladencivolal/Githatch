package com.example.githatch.presentation.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.githatch.data.model.owner.Owner
import com.example.githatch.data.repository.detail.FakeDetailRepository
import com.example.githatch.domain.usecase.GetContribsUseCase
import com.example.githatch.getOrAwaitValue
import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DetailViewModelTest {
    private lateinit var viewModel: DetailViewModel
    private var contributors = mutableListOf<Owner>()

    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        val fakeDetailRepository = FakeDetailRepository()
        val getContribsUseCase = GetContribsUseCase(fakeDetailRepository)
        viewModel = DetailViewModel(getContribsUseCase)

        contributors.add(
            Owner(
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
        )
        contributors.add(
            Owner(
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
            )
        )
        contributors.add(
            Owner(
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
            )
        )
    }

    @Test
    fun getContribs() {
        val contribs = viewModel.getContribs("owner", "repo").getOrAwaitValue()
        Truth.assertThat(contribs).isEqualTo(contributors)
    }
}