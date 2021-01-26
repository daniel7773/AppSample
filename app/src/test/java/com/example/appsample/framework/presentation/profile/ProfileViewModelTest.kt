package com.example.appsample.framework.presentation.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.appsample.business.data.network.DataFactory
import com.example.appsample.business.domain.model.Comment
import com.example.appsample.business.interactors.common.GetUserUseCase
import com.example.appsample.business.interactors.profile.GetAlbumListUseCase
import com.example.appsample.business.interactors.profile.GetCommentListUseCase
import com.example.appsample.business.interactors.profile.GetPostListUseCase
import com.example.appsample.framework.base.presentation.SessionManager
import com.example.appsample.framework.presentation.common.model.State
import com.example.appsample.framework.presentation.profile.screens.main.ProfileViewModel
import com.example.appsample.rules.InstantExecutorExtension
import com.example.appsample.rules.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions


@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
class ProfileViewModelTest {

    val getUserUseCase: GetUserUseCase = mockk()
    val getPostListUseCase: GetPostListUseCase = mockk()
    val getAlbumListUseCase: GetAlbumListUseCase = mockk()
    val getCommentListUseCase: GetCommentListUseCase = mockk()
    val savedStateHandle: SavedStateHandle = mockk()

    @get:Extensions
    val mainCoroutineRule: MainCoroutineRule = MainCoroutineRule()

    val sessionManager: SessionManager = SessionManager(mainCoroutineRule.testDispatcher)
    val profileViewModel: ProfileViewModel

    init {
        sessionManager.user = DataFactory.produceUserModel()
        every { savedStateHandle.getLiveData<Int>(any()) } returns MutableLiveData(1)
        every { savedStateHandle.get<Any>(any()) } returns null
        every { savedStateHandle.set<Any>(any(), any()) } returns Unit

        profileViewModel = ProfileViewModel(
            mainDispatcher = mainCoroutineRule.testDispatcher,
            ioDispatcher = mainCoroutineRule.testDispatcher,
            sessionManager,
            getPostListUseCase,
            getUserUseCase,
            getAlbumListUseCase,
            savedStateHandle
        )
    }

    @Nested
    inner class UserData {

        @Test
        fun `Loading Success data came`() =
            mainCoroutineRule.testDispatcher.runBlockingTest {
                // Given

                coEvery {
                    getUserUseCase.getUser(any())
                } returns flowOf(DataFactory.produceUser())

                // When
                profileViewModel.startSearch()

                // Then
                assertThat(profileViewModel.user).isNotNull
                assertThat(profileViewModel.user).isInstanceOf(State.Success::class.java)
                assertThat(profileViewModel.user.exception).isNull()
            }

        @Test
        fun `Loading Error exception received correctly`() =
            mainCoroutineRule.testDispatcher.runBlockingTest {

                val user = DataFactory.produceUser()
                // Given
                coEvery {
                    getUserUseCase.getUser(any())
                } throws Exception()

                // When

                profileViewModel.startSearch()

                // Then
                assertThat(profileViewModel.user.exception).isNotNull

            }
    }

    @Nested
    inner class AlbumsData {

        @Test
        fun `Loading Success data came`() =
            mainCoroutineRule.testDispatcher.runBlockingTest {
                // Given

                coEvery {
                    getAlbumListUseCase.getAlbumList(any())
                } returns flowOf(DataFactory.produceListOfAlbums(4))

                // When
                profileViewModel.startSearch()

                // Then
                assertThat(profileViewModel.albumList).isNotNull
                assertThat(profileViewModel.albumList).isInstanceOf(State.Success::class.java)
                assertThat(profileViewModel.albumList.exception).isNull()
            }

        @Test
        fun `Loading Error exception received correctly`() =
            mainCoroutineRule.testDispatcher.runBlockingTest {

                // Given
                val resourceError =
                    DataFactory.produceListOfAlbums(4)
                coEvery {
                    getAlbumListUseCase.getAlbumList(any())
                } throws Exception()

                // When
                profileViewModel.startSearch()

                // Then
                assertThat(profileViewModel.albumList.exception).isNotNull
            }
    }

    @Nested
    inner class PostsData {

        @Test
        fun `Loading Success data came`() =
            mainCoroutineRule.testDispatcher.runBlockingTest {
                // Given
                val exception = Exception("My exception")
                coEvery {
                    getPostListUseCase.getPostList(any())
                } returns flowOf(DataFactory.produceListOfPosts(4))
                coEvery {
                    getCommentListUseCase.getCommentList(any())
                } returns flowOf(listOf(Comment()))
                // When
                profileViewModel.startSearch()

                // Then
                assertThat(profileViewModel.postList).isNotNull
                assertThat(profileViewModel.postList).isInstanceOf(State.Success::class.java)
                assertThat(profileViewModel.postList.exception).isNull()
            }

        @Test
        fun `Loading Error exception received correctly`() =
            mainCoroutineRule.testDispatcher.runBlockingTest {

                // Given
                coEvery {
                    getPostListUseCase.getPostList(any())
                } throws Exception()
                coEvery {
                    getCommentListUseCase.getCommentList(any())
                } returns flowOf(listOf<Comment>())
                // When
                profileViewModel.startSearch()

                // Then
                assertThat(profileViewModel.postList.exception).isNotNull

            }
    }
}