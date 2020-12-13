package com.example.appsample.framework.presentation.profile

import com.example.appsample.business.data.network.DataFactory
import com.example.appsample.business.interactors.common.GetUserUseCase
import com.example.appsample.business.interactors.profile.GetAlbumsUseCase
import com.example.appsample.business.interactors.profile.GetPostsUseCase
import com.example.appsample.framework.base.presentation.SessionManager
import com.example.appsample.framework.presentation.common.model.State
import com.example.appsample.rules.InstantExecutorExtension
import com.example.appsample.rules.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
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
    val getPostsUseCase: GetPostsUseCase = mockk()
    val getAlbumsUseCase: GetAlbumsUseCase = mockk()

    @get:Extensions
    val mainCoroutineRule: MainCoroutineRule = MainCoroutineRule()

    val sessionManager: SessionManager = SessionManager(mainCoroutineRule.testDispatcher)
    val profileViewModel: ProfileViewModel

    init {
        sessionManager.user = DataFactory.produceUserModel()
        profileViewModel = ProfileViewModel(
            mainCoroutineRule.testDispatcher,
            sessionManager,
            getPostsUseCase,
            getUserUseCase,
            getAlbumsUseCase
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
                } returns DataFactory.provideResourceSuccess(DataFactory.produceUser())

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

                // Given
                coEvery {
                    getUserUseCase.getUser(any())
                } returns DataFactory.provideResourceError(DataFactory.produceUser())

                // When
                profileViewModel.startSearch()

                // Then
                assertThat(profileViewModel.user.exception).isNotNull
                assertThat(profileViewModel.user.exception).isInstanceOf(getUserUseCase.getUser(3).exception!!::class.java)

            }
    }

    @Nested
    inner class AlbumsData {

        @Test
        fun `Loading Success data came`() =
            mainCoroutineRule.testDispatcher.runBlockingTest {
                // Given

                coEvery {
                    getAlbumsUseCase.getAlbums(any())
                } returns DataFactory.provideResourceSuccess(DataFactory.produceListOfAlbums(4))

                // When
                profileViewModel.startSearch()

                // Then
                assertThat(profileViewModel.albums).isNotNull
                assertThat(profileViewModel.albums).isInstanceOf(State.Success::class.java)
                assertThat(profileViewModel.albums.exception).isNull()
            }

        @Test
        fun `Loading Error exception received correctly`() =
            mainCoroutineRule.testDispatcher.runBlockingTest {

                // Given
                coEvery {
                    getAlbumsUseCase.getAlbums(any())
                } returns DataFactory.provideResourceError(DataFactory.produceListOfAlbums(4))

                // When
                profileViewModel.startSearch()

                // Then
                assertThat(profileViewModel.albums.exception).isNotNull
                assertThat(profileViewModel.albums.exception).isInstanceOf(
                    getAlbumsUseCase.getAlbums(
                        3
                    ).exception!!::class.java
                )

            }
    }

    @Nested
    inner class PostsData {

        @Test
        fun `Loading Success data came`() =
            mainCoroutineRule.testDispatcher.runBlockingTest {
                // Given

                coEvery {
                    getPostsUseCase.getPosts(any())
                } returns DataFactory.provideResourceSuccess(DataFactory.produceListOfPosts(4))

                // When
                profileViewModel.startSearch()

                // Then
                assertThat(profileViewModel.posts).isNotNull
                assertThat(profileViewModel.posts).isInstanceOf(State.Success::class.java)
                assertThat(profileViewModel.posts.exception).isNull()
            }

        @Test
        fun `Loading Error exception received correctly`() =
            mainCoroutineRule.testDispatcher.runBlockingTest {

                // Given
                coEvery {
                    getPostsUseCase.getPosts(any())
                } returns DataFactory.provideResourceError(DataFactory.produceListOfPosts(4))

                // When
                profileViewModel.startSearch()

                // Then
                assertThat(profileViewModel.posts.exception).isNotNull
                assertThat(profileViewModel.posts.exception).isInstanceOf(
                    getPostsUseCase.getPosts(
                        3
                    ).exception!!::class.java
                )

            }
    }
}