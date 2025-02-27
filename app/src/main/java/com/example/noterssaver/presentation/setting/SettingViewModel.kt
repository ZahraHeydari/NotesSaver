package com.example.noterssaver.presentation.setting

import androidx.biometric.BiometricManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noterssaver.domain.usecase.settings.SettingUseCases
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingViewModel(
    private val useCases: SettingUseCases,
    private val biometricManager: BiometricManager
) : ViewModel() {

    val currentThemeState = useCases.getCurrentThemeStatusUseCase.invoke()
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily, ThemeStyle.LIGHT
        )
    val appLockState =
        useCases.getAppLockStatusUseCase.invoke()
            .stateIn(viewModelScope, SharingStarted.Lazily, false)

    init {
        viewModelScope.launch {
            useCases.emptySettingUseCase()
        }
    }

    fun canAuthenticate() : Int = biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
                    BiometricManager.Authenticators.DEVICE_CREDENTIAL)

    fun updateTheme(value: ThemeStyle) = viewModelScope.launch {
        useCases.updateThemeUseCase(value)
    }

    fun updateAppLock(value: Boolean) = viewModelScope.launch {
        useCases.updateAppLockerUseCase.invoke(value)
    }
}