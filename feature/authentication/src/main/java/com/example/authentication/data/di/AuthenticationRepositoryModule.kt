package com.example.authentication.data.di

import com.example.authentication.data.repository.AuthenticationRepositoryImpl
import com.example.authentication.domain.repository.AuthenticationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class AuthenticationRepositoryModule {
    @Singleton
    @Binds
    abstract fun bindAuthRepository(impl: AuthenticationRepositoryImpl): AuthenticationRepository
}