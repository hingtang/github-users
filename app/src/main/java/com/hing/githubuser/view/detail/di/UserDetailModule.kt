package com.hing.githubuser.view.detail.di

import com.hing.githubuser.data.GitHubApi
import com.hing.githubuser.data.userdetail.UserDetailMapperImpl
import com.hing.githubuser.data.userdetail.UserDetailRepositoryImpl
import com.hing.githubuser.domain.userdetail.UserDetailApiService
import com.hing.githubuser.domain.userdetail.UserDetailRepository
import com.hing.githubuser.domain.userdetail.UserDetailUseCase
import com.hing.githubuser.domain.userdetail.UserDetailUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object UserDetailModule {

    @Provides
    fun provideRepository(gitHubApi: GitHubApi): UserDetailRepository {
        return UserDetailRepositoryImpl(
            UserDetailApiService(gitHubApi),
            UserDetailMapperImpl()
        )
    }

    @Provides
    fun provideUseCase(repository: UserDetailRepository): UserDetailUseCase {
        return UserDetailUseCaseImpl(repository)
    }
}
