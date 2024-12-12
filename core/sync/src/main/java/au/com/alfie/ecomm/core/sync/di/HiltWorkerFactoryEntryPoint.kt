package au.com.alfie.ecomm.core.sync.di

import androidx.hilt.work.HiltWorkerFactory
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
internal interface HiltWorkerFactoryEntryPoint {

    fun hiltWorkerFactory(): HiltWorkerFactory
}
