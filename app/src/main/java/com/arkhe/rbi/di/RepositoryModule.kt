package com.arkhe.rbi.di

import com.arkhe.rbi.domain.repository.AuthRepository
import com.arkhe.rbi.utils.DeviceInfoProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    single { AuthRepository(get(), get()) }
    single { DeviceInfoProvider(androidContext()) }
}