package com.aleksandrgenrihs.sovcombanktest.di

import com.aleksandrgenrihs.sovcombanktest.data.SmsCodeRepositoryImpl
import com.aleksandrgenrihs.sovcombanktest.domain.SmsCodeRepository
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
    fun bindSmsCodeRepository(impl: SmsCodeRepositoryImpl): SmsCodeRepository
}