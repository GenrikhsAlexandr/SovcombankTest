package com.aleksandrgenrihs.sovcombanktest.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aleksandrgenrihs.sovcombanktest.domain.OtpInteractor
import com.aleksandrgenrihs.sovcombanktest.domain.model.OtpVerify
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class OtpViewModel
@Inject constructor(private val interactor: OtpInteractor) : ViewModel() {

    private var currentInput = ""
    private var result: OtpVerify? = null
    var viewState by mutableStateOf(OtpUiState())

    init {
        viewModelScope.launch {
            otpRequest()
        }
    }

    fun otpRequest() {
        viewModelScope.launch {
            viewState = viewState.copy(
                loading = true
            )
            try {
                if (!interactor.canSendRequest()) {
                    startTimerIfNeeded()
                    return@launch
                }
                val response = interactor.otpRequest()
                    .onSuccess {
                        viewState = viewState.copy(
                            canResend = false,
                        )
                    }
                    .getOrThrow()
                setCanResendIn(response.canResendIn, response.codeLength)
            } catch (e: Exception) {
                e.printStackTrace()
                viewState = viewState.copy(
                    isError = true,
                    loading = false
                )
            }
        }.invokeOnCompletion {
            viewState = viewState.copy(
                loading = false
            )
        }
    }

    private suspend fun setCanResendIn(canResendIn: Duration, codeLength: Int) {
        try {
            interactor.setCanResendIn(canResendIn, codeLength)
            startTimerIfNeeded()
            viewState = viewState.copy(isError = false)
        } catch (e: Exception) {
            e.printStackTrace()
            viewState = viewState.copy(
                isError = true,
            )
        }
    }

    private suspend fun startTimerIfNeeded() {
        val secondsLeft = interactor.getCanResendInSeconds()
        val codeLength = interactor.getCodeLength()
        viewState = viewState.copy(
            codeLength = codeLength,
        )
        updateResendButtonState(secondsLeft)
        startTimer(secondsLeft)
    }

    private fun startTimer(secondsLeft: Int) {
        if (secondsLeft <= 0) {
            return
        }

        viewModelScope.launch {
            var secondsLeftInternal = secondsLeft
            while (isActive && secondsLeftInternal >= 0) {

                updateResendButtonState(secondsLeftInternal)

                delay(1.seconds)
                secondsLeftInternal--
            }
        }
    }

    private fun updateResendButtonState(secondsLeft: Int) {
        viewState = if (secondsLeft <= 0) {
            viewState.copy(canResend = true)
        } else {
            viewState.copy(
                secondsLeft = secondsLeft,
                canResend = false
            )
        }
    }

    fun onInputChange(newValue: String) {
        currentInput = newValue.replace(" ", "")
        val canVerify = validateInput()
        viewState = viewState.copy(
            userInput = newValue,
            correctCode = true,
            canVerify = canVerify,
        )
        if (!canVerify) return
        otpVerify()
    }

    private fun otpVerify() {
        viewModelScope.launch {
            viewState = viewState.copy(loading = true)
            try {
                result = interactor.otpVerify(currentInput).getOrThrow()
                val success = result?.success ?: false
                viewState = viewState.copy(
                    correctCode = success,
                )
            } catch (e: Exception) {
                viewState = viewState.copy(
                    isError = true,
                )

            } finally {
                viewState = viewState.copy(loading = false)
            }
        }
    }

    private fun validateInput(): Boolean {
        return currentInput.length == viewState.codeLength
    }
}