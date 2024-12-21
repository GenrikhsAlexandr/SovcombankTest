package com.aleksandrgenrihs.sovcombanktest.di

import com.aleksandrgenrihs.sovcombanktest.data.OtpRepositoryImpl
import com.aleksandrgenrihs.sovcombanktest.domain.OtpRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface BindsModule {

    @Binds
    @Singleton
    fun bindOtpRepository(impl: OtpRepositoryImpl): OtpRepository
}