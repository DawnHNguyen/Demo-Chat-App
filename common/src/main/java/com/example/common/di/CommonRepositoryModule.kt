package com.example.common.di

import com.example.common.data.repository.CommonRepositoryImpl
import com.example.common.domain.repository.CommonRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class CommonRepositoryModule {
    @Singleton
    @Binds
    abstract fun bindCommonRepository(impl: CommonRepositoryImpl): CommonRepository
}