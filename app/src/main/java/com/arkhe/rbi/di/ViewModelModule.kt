package com.arkhe.rbi.di

import com.arkhe.rbi.presentation.login.LoginViewModel
import com.arkhe.rbi.presentation.main.MainViewModel
import com.arkhe.rbi.presentation.register.RegisterViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { RegisterViewModel(get(), get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { MainViewModel(get()) }
}