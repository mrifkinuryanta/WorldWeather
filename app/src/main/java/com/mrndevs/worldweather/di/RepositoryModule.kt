package com.mrndevs.worldweather.di

import com.mrndevs.worldweather.data.mapper.WeatherMapper
import com.mrndevs.worldweather.data.repository.WeatherRepository
import com.mrndevs.worldweather.data.repository.WeatherRepositoryImpl
import com.mrndevs.worldweather.data.source.local.datasource.LocalDataSource
import com.mrndevs.worldweather.data.source.network.datasource.WeatherApiDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideWeatherRepository(
        localDataSource: LocalDataSource,
        weatherApiDataSource: WeatherApiDataSource,
        weatherMapper: WeatherMapper,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): WeatherRepository {
        return WeatherRepositoryImpl(
            localDataSource = localDataSource,
            weatherApiDataSource = weatherApiDataSource,
            weatherMapper = weatherMapper,
            ioDispatcher = ioDispatcher
        )
    }
}