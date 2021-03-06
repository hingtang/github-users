package com.hing.githubuser.view.search.di

import com.hing.githubuser.data.GitHubApi
import com.hing.githubuser.data.getusers.GetUsersMapperImpl
import com.hing.githubuser.data.getusers.GetUsersRepositoryImpl
import com.hing.githubuser.data.search.SearchUserMapperImpl
import com.hing.githubuser.data.search.SearchUserRepositoryImpl
import com.hing.githubuser.domain.search.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object SearchUserModule {

    @Provides
    fun provideGetUsersRepository(gitHubApi: GitHubApi): GetUsersRepository {
        return GetUsersRepositoryImpl(
            GetUsersApiService(gitHubApi),
            GetUsersMapperImpl()
        )
    }

    @Provides
    fun provideGetUsersUseCase(repository: GetUsersRepository): GetUsersUseCase {
        return GetUsersUseCaseImpl(repository)
    }

    @Provides
    fun provideSearchUserRepository(gitHubApi: GitHubApi): SearchUserRepository {
        return SearchUserRepositoryImpl(
            SearchUserApiService(gitHubApi),
            SearchUserMapperImpl()
        )
    }

    @Provides
    fun provideSearchUserUseCase(repository: SearchUserRepository): SearchUserUseCase {
        return SearchUserUseCaseImpl(repository)
    }
}
