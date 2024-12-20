package com.aleksandrgenrihs.sovcombanktest.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProvidesModule {

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext app: Context): Context = app as Application
}